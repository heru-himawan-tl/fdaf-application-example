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

import fdaf.base.CommonConfigurationInterface;
import fdaf.webapp.base.AbstractDummyWebAppBean;
import java.io.Serializable;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

@SessionScoped
@Named
public class ExpirationPageWebAppBean extends AbstractDummyWebAppBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB(lookup = "java:global/__EJB_LOOKUP_DIR__/CommonConfigurationService")
    private CommonConfigurationInterface commonConfiguration;
    
    private String referer;

    public ExpirationPageWebAppBean() {
        referer = "";
    }

    public void initExpirationPage(ComponentSystemEvent event) throws AbortProcessingException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext();
        String currentPath = externalContext.getRequestServletPath();
        Map<String, String[]> headers = externalContext.getRequestHeaderValuesMap();
        String key = "Referer";
        if ((currentPath != null) && (headers != null) && !headers.isEmpty() && headers.containsKey(key)) {
            String[] currentReferer = headers.get(key);
            if ((currentReferer != null) && (currentReferer.length != 0) && !currentReferer[0].equals(referer)
                && !currentReferer[0].replaceAll(".*\\/", "").equals(currentPath.replace("/", ""))) {
                referer = currentReferer[0];
            }
        }
    }
    
    protected CommonConfigurationInterface getCommonConfiguration() {
        return commonConfiguration;
    }

    public String getReferer() {
        return referer;
    }
}
