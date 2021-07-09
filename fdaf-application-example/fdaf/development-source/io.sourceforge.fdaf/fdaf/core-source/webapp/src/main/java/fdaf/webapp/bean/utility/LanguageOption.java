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
package fdaf.webapp.bean.utility;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@SessionScoped
@Named
public class LanguageOption implements Serializable {

    private static final long serialVersionUID = 1L;
    private SelectItem[] items = new SelectItem[]{};
    private HashMap<String, String> info = new HashMap<String, String>();
    
    public LanguageOption() {
        // NO-OP
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void postConstruct() {
        List<SelectItem> itemsTemp = new ArrayList<SelectItem>();
        Locale locales[] = DateFormat.getAvailableLocales();
        for (Locale locale : locales) {
            info.put(locale.toString(), locale.getDisplayName());
        }
        Map<String, String> ltm = new TreeMap<String, String>(info);
        for (Map.Entry<String, String> entry : ltm.entrySet()) {
            SelectItem selectItem = new SelectItem();
            selectItem.setValue(entry.getKey());
            selectItem.setLabel(entry.getKey() + " (" + entry.getValue() + ")");
            itemsTemp.add(selectItem);
        }
        items = itemsTemp.toArray(new SelectItem[]{});
    }
    
    public SelectItem[] getItems() {
        return items;
    }
    
    public String getLanguageInfo(String code) {
        return info.get(code);
    }
}
