/*
 * Copyright (c) Heru Himawan Tejo Laksono. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fdaf.webapp.bean.system;

import fdaf.base.AddAdministratorInterface;
import fdaf.base.AdministratorAccountCheckerInterface;
import fdaf.base.CommonConfigurationInterface;
import fdaf.base.MailerInterface;
import fdaf.base.UserSessionManagerInterface;
import fdaf.base.UserType;
import fdaf.webapp.base.AbstractWebAppBean;
import fdaf.webapp.base.WebAppOpMode;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ViewScoped
@Named
public class AddAdministratorWebAppBean extends AbstractWebAppBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AddAdministratorWebAppBean.class.getName());
    
    private static final long serialVersionUID = 1L;
    
    private static final String MASTER_PASSWORD_FMT = "# SET ADMINISTRATOR ACCOUNT master password\n"
        +   "# Generated at: %s\n\n"
        +   "# WARNING:\n# Do not edit this file to avoid trouble in processing SET ADMINISTRATOR ACCOUNT.\n\n"
        +   "# Use the master password bellow to unlock SET ADMINISTRATOR ACCOUNT:\n\n%s";
        
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    private static final Random RANDOM = new SecureRandom();
    
    @EJB(lookup = "java:global/fdaf/fdaf-logic/AdministratorAccountCheckerFacade")
    private AdministratorAccountCheckerInterface rootAccountChecker;
    
    @EJB(lookup = "java:global/fdaf/fdaf-logic/AddAdministratorFacade")
    private AddAdministratorInterface facade;
    
    @EJB(lookup = "java:global/fdaf/fdaf-logic/UserSessionManagerFacade")
    private UserSessionManagerInterface userSessionManager;
    
    @EJB(lookup = "java:global/fdaf/fdaf-logic/Mailer")
    private MailerInterface mailer;
    
    @EJB(lookup = "java:global/fdaf/fdaf-logic/CommonConfiguration")
    private CommonConfigurationInterface commonConfiguration;
    
    private String masterPasswordFileAddr;
    private String masterPassword;
    private String unlockPassword;
    
    private boolean mailerFailure;

    public AddAdministratorWebAppBean() {
        // NO-OP
    }

    protected AdministratorAccountCheckerInterface getAdministratorAccountChecker() {
        return rootAccountChecker;
    }
    
    protected CommonConfigurationInterface getCommonConfiguration() {
        return commonConfiguration;
    }
    
    public UserSessionManagerInterface getUserSessionManager() {
        return userSessionManager;
    }

    public void initAddAdministrator(ComponentSystemEvent event) throws AbortProcessingException {
        boolean isUNIX = (!System.getProperty("os.name").matches(".*Windows.*"));
        String fsp = File.separator;
        Path configDirPath = null;
        
        if (opMode != WebAppOpMode.CREATE && opMode != WebAppOpMode.UPDATE) {
            opMode = WebAppOpMode.CREATE;
            disableValidation = false;
            exitSearch(null);
            getFacade().prepareCreate();
            presetEntity();
        }
        
        try {
            if (isUNIX) {
                String userHome = System.getProperty("user.home");
                masterPasswordFileAddr  = userHome + fsp + "." + getApplicationCodeName() + fsp + "master-password.txt";
                configDirPath = Paths.get(userHome + fsp + "." + getApplicationCodeName());
            } else {
                masterPasswordFileAddr  = "C:\\" + getApplicationCodeName() + "\\master-password.txt";
                configDirPath = Paths.get("C:\\" + getApplicationCodeName());
            }
        } catch (Exception e) {
            addMessage(SV_ERROR, "readHomeDirectoryFailed");
            LOGGER.log(Level.SEVERE, null, e);
            return;
        }
        
        try {
            if (!Files.exists(configDirPath)) {
                Files.createDirectory(configDirPath);
            }
        } catch (Exception e) {
            addMessage(SV_ERROR, "createConfigDirectoryFailed");
            LOGGER.log(Level.SEVERE, null, e);
            return;
        }
        
        try {
            if (!Files.exists(Paths.get(masterPasswordFileAddr))) {
                generatePassword();
            } else {
                masterPassword = "";
                for (String line : Files.readAllLines(Paths.get(masterPasswordFileAddr))) {
                    if ((line.trim().length() == 12) && line.trim().matches(".*[a-z]+.*")
                        && line.trim().matches(".*[0-9]+.*") && !line.matches("^\\#.*")) {
                        masterPassword += line.trim();
                        break;
                    }
                }
                if ((masterPassword.length() != 12) || !masterPassword.matches(".*[a-z]+.*")
                    || !masterPassword.matches(".*[0-9]+.*")) {
                    generatePassword();
                }
            }
        } catch (Exception e) {
            addMessage(SV_ERROR, "createMasterPasswordFileFailed");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    private void generatePassword() throws Exception {
        StringBuilder password = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            password.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        masterPassword = new String(password);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        Date timeStamp = new Date();
        FileWriter fw = new FileWriter(masterPasswordFileAddr, false);
        fw.write(String.format(MASTER_PASSWORD_FMT, dateFormatter.format(timeStamp), masterPassword));
        fw.close();
    }

    public void setUnlockPassword(String unlockPassword) {
        this.unlockPassword = unlockPassword;
    }

    public String getUnlockPassword() {
        return unlockPassword;
    }

    public String getMasterPasswordFileAddr() {
        return masterPasswordFileAddr;
    }

    @Override
    public void executeCreate(AjaxBehaviorEvent event) throws AbortProcessingException {
        mailerFailure = false;
        if (userType != UserType.ADMINISTRATOR) {
            boolean abort = false;
            if (unlockPassword.trim().isEmpty()) {
                addMessage(SV_ERROR, "unlockPasswordEmpty");
                abort = true;
            }
            if (!unlockPassword.trim().isEmpty() && !unlockPassword.equals(masterPassword)) {
                addMessage(SV_ERROR, "unlockPasswordIncorrect");
                abort = true;
            }
            if (abort) {
                return;
            }
        }
        try {
            feedBackEntity();
            if (!getFacade().preCreateCheck()) {
                addCallbackMessage(SV_WARN, getFacade().getMessage());
            } else {
                getFacade().create();
                getFacade().reloadNewEntity();
                presetEntity();
                feedBackEntity();
                getFacade().postCreateTask();
                opMode = WebAppOpMode.UPDATE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
            addMessage(SV_ERROR, "serviceErrorWarning");
            rollbackCreateTask();
            return;
        }
        if (!mailer.isEnabled()) {
            return;
        }
        try {
            String subject = label.getString("addAdministratorMailNotificationSubject");
            String message = String.format(messageBundle.getString("addAdministratorAccountMailNotificationMessage"),
                getWebappURL(),
                getWebappURL(),
                facade.getUserName(),
                facade.getPassword(),
                facade.getEmail()
            );
            if (!mailer.send("noreply@" + mailer.getDomain(), facade.getEmail(), subject, message)) {
                if (!mailer.isValidAddress()) {
                    addMessage(SV_ERROR, "mailerErrorInvalidAddressWarning");
                    rollbackCreateTask();
                    opMode = WebAppOpMode.CREATE;
                    disableValidation = false;
                    exitSearch(null);
                    getFacade().prepareCreate();
                    presetEntity();
                } else {
                    addMessage(SV_ERROR, "mailerErrorWarning");
                }
                mailerFailure = true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
                if (!mailer.isValidAddress()) {
                    addMessage(SV_ERROR, "mailerErrorInvalidAddressWarning");
                    rollbackCreateTask();
                    opMode = WebAppOpMode.CREATE;
                    disableValidation = false;
                    exitSearch(null);
                    getFacade().prepareCreate();
                    presetEntity();
                } else {
                    addMessage(SV_ERROR, "mailerErrorWarning");
                }
            mailerFailure = true;
        }
    }
    
    public boolean getMailerFailure() {
        return mailerFailure;
    }

    @Override
    public void executeUpdate(AjaxBehaviorEvent event) throws AbortProcessingException {
        // NO-OP
    }

    public AddAdministratorInterface getFacade() {
        return facade;
    }
}