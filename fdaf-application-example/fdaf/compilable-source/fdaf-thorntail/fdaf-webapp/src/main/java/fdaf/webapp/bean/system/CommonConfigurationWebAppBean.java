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

import fdaf.base.AdministratorAccountCheckerInterface;
import fdaf.base.CommonConfigurationInterface;
import fdaf.base.DatabaseServiceCheckerInterface;
import fdaf.base.FacadeInterface;
import fdaf.base.UserSessionManagerInterface;
import fdaf.webapp.base.AbstractBaseWebAppBean;
import fdaf.webapp.base.WebAppOpMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ViewScoped
@Named
public class CommonConfigurationWebAppBean extends AbstractBaseWebAppBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CommonConfigurationWebAppBean.class.getName());
    
    private static final long serialVersionUID = 1L;
    
    private static final String MASTER_PASSWORD_FMT = "# SET ADMINISTRATOR ACCOUNT master password\n"
        +   "# Generated at: %s\n\n"
        +   "# WARNING:\n# Do not edit this file to avoid trouble in processing SET ADMINISTRATOR ACCOUNT.\n\n"
        +   "# Use the master password bellow to unlock SET ADMINISTRATOR ACCOUNT:\n\n%s";
        
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    private static final Random RANDOM = new SecureRandom();
    
    @EJB(lookup = "java:global/fdaf/AdministratorAccountCheckerFacade")
    private AdministratorAccountCheckerInterface admAccountChecker; 
      
    @EJB(lookup = "java:global/fdaf/DatabaseServiceCheckerFacade")
    private DatabaseServiceCheckerInterface dbServiceChecker;
    
    @EJB(lookup = "java:global/fdaf/UserSessionManagerFacade")
    private UserSessionManagerInterface userSessionManager;
    
    @EJB(lookup = "java:global/fdaf/CommonConfiguration")
    private CommonConfigurationInterface config;
    
    private String masterPasswordFileAddr;
    private String masterPassword;
    private String unlockPassword;
    
    private boolean offlineSite;
    
    @Size(min = 1, max = 128, message = "Site name length out of range (min = 1, max = 128).")
    @NotBlank(message = "Site name not specified.")
    private String siteName;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\-]+$", message = "Invalid format for domain name (allowed: a-z, A-Z, 0-9, ., and -).")
    @Size(min = 3, max = 128, message = "Domain name length out of range (min = 3, max = 128).")
    @NotBlank(message = "Domain name not specified.")
    private String domain;
    
    private boolean domainAsDefaultSite;
    
    private boolean allowPerUserMultipleLogins;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\-]+$", message = "Invalid format for web socket client secure key (allowed: a-z, A-Z, 0-9, ., and -).")
    @Size(min = 6, max = 128, message = "Web socket client secure key length out of range (min = 6, max = 128).")
    @NotBlank(message = "Web socket client secure key not specified.")
    private String webSocketClientSecureKey;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\.]+@[a-zA-Z0-9\\-\\.]+$", message = "Invalid format of e-mail address.")
    @NotBlank(message = "E-mail address not specified.")
    private String webmasterEmail;
    
    private String siteDescription;
    
    private String regionalLanguage;
    
    private String companyName;
    private String companyDescription;
    private String companyAddress1;
    private String companyAddress2;
    private String companyPhone1;
    private String companyPhone2;
    
    private boolean inSaving;
    
    public CommonConfigurationWebAppBean() {
        // NO-OP
    }
    
    public void initCommonConfiguration(ComponentSystemEvent event) throws AbortProcessingException {
    
        boolean isUNIX = (!System.getProperty("os.name").matches(".*Windows.*"));
        String fsp = File.separator;
        Path configDirPath = null;
        
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
            return;
        }
        
        config.loadConfig();
        
        if (config.isEnabled()) {
            setOfflineSite(config.getOfflineSite());
            setSiteName(config.getSiteName());
            setDomain(config.getDomain());
            setDomainAsDefaultSite(config.getDomainAsDefaultSite());
            setAllowPerUserMultipleLogins(config.getAllowPerUserMultipleLogins());
            setWebSocketClientSecureKey(config.getWebSocketClientSecureKey());
            setWebmasterEmail(config.getWebmasterEmail());
            setSiteDescription(config.getSiteDescription());
            setRegionalLanguage(config.getRegionalLanguage());
            setCompanyName(config.getCompanyName());
            setCompanyDescription(config.getCompanyDescription());
            setCompanyAddress1(config.getCompanyAddress1());
            setCompanyAddress2(config.getCompanyAddress2());
            setCompanyPhone1(config.getCompanyPhone1());
            setCompanyPhone2(config.getCompanyPhone2());
        }
    }
    
    protected CommonConfigurationInterface getCommonConfiguration() {
        return config;
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

    protected AdministratorAccountCheckerInterface getAdministratorAccountChecker() {
        return admAccountChecker;
    }
    
    @Override
    protected DatabaseServiceCheckerInterface getDatabaseServiceChecker() {
        return dbServiceChecker;
    }
    
    public UserSessionManagerInterface getUserSessionManager() {
        return userSessionManager;
    }
    
    public void save(AjaxBehaviorEvent event) throws AbortProcessingException {
        boolean abort = false;
        inSaving = false;
        if (!loggedOn) {
            if (unlockPassword.trim().isEmpty()) {
                addMessage(SV_ERROR, "unlockPasswordEmpty");
                abort = true;
            }
            if (!unlockPassword.trim().isEmpty() && !unlockPassword.equals(masterPassword)) {
                addMessage(SV_ERROR, "unlockPasswordIncorrect");
                abort = true;
            }
        }
        if (abort) {
            return;
        }
        config.setOfflineSite(offlineSite);
        config.setSiteName(siteName);
        config.setDomain(domain);
        config.setDomainAsDefaultSite(domainAsDefaultSite);
        config.setAllowPerUserMultipleLogins(allowPerUserMultipleLogins);
        config.setWebSocketClientSecureKey(webSocketClientSecureKey);
        config.setWebmasterEmail(webmasterEmail);
        config.setSiteDescription(siteDescription);
        config.setRegionalLanguage(regionalLanguage);
        config.setCompanyName(companyName);
        config.setCompanyDescription(companyDescription);
        config.setCompanyAddress1(companyAddress1);
        config.setCompanyAddress2(companyAddress2);
        config.setCompanyPhone1(companyPhone1);
        config.setCompanyPhone2(companyPhone2);
        if (!config.saveConfig()) {
            addMessage(SV_ERROR, "commonConfigurationServiceError");
            return;
        }
        inSaving = true;
    }
    
    public boolean getInSaving() {
        return inSaving;
    }
    
    public CommonConfigurationInterface getConfig() {
        return config;
    }
    
    public void setOfflineSite(boolean offlineSite) {
        this.offlineSite = offlineSite;
    }

    public boolean getOfflineSite() {
        return offlineSite;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
    
    public void setWebSocketClientSecureKey(String webSocketClientSecureKey) {
        this.webSocketClientSecureKey = webSocketClientSecureKey;
    }

    public String getWebSocketClientSecureKey() {
        return webSocketClientSecureKey;
    }
    
    public void setAllowPerUserMultipleLogins(boolean allowPerUserMultipleLogins) {
        this.allowPerUserMultipleLogins = allowPerUserMultipleLogins;
    }
    
    public boolean getAllowPerUserMultipleLogins() {
        return allowPerUserMultipleLogins;
    }

    public void setDomainAsDefaultSite(boolean domainAsDefaultSite) {
        this.domainAsDefaultSite = domainAsDefaultSite;
    }

    public boolean getDomainAsDefaultSite() {
        return domainAsDefaultSite;
    }

    public void setWebmasterEmail(String webmasterEmail) {
        this.webmasterEmail = webmasterEmail;
    }

    public String getWebmasterEmail() {
        return webmasterEmail;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setRegionalLanguage(String regionalLanguage) {
        this.regionalLanguage = regionalLanguage;
    }

    public String getRegionalLanguage() {
        return regionalLanguage;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyAddress1(String companyAddress1) {
        this.companyAddress1 = companyAddress1;
    }

    public String getCompanyAddress1() {
        return companyAddress1;
    }

    public void setCompanyAddress2(String companyAddress2) {
        this.companyAddress2 = companyAddress2;
    }

    public String getCompanyAddress2() {
        return companyAddress2;
    }

    public void setCompanyPhone1(String companyPhone1) {
        this.companyPhone1 = companyPhone1;
    }

    public String getCompanyPhone1() {
        return companyPhone1;
    }

    public void setCompanyPhone2(String companyPhone2) {
        this.companyPhone2 = companyPhone2;
    }

    public String getCompanyPhone2() {
        return companyPhone2;
    }
}