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
package fdaf.logic.callback.sourced_checker_wrapper;

import fdaf.logic.tools.SourcedDataChecker;
import fdaf.logic.base.SourcedDataCheckerInterface;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Stateful(passivationCapable = false)
public class SourcedDataCheckWrapper extends SourcedDataChecker implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB(lookup = "java:global/fdaf-webapp/DepartmentOnEmployeeSourcedCheck")
    SourcedDataCheckerInterface sdc1;
    @EJB(lookup = "java:global/fdaf-webapp/EmployeeOnUserSourcedCheck")
    SourcedDataCheckerInterface sdc2;
    @EJB(lookup = "java:global/fdaf-webapp/RoleOnEmployeeSourcedCheck")
    SourcedDataCheckerInterface sdc3;
    @EJB(lookup = "java:global/fdaf-webapp/UserGroupOnUserGroupMemberSourcedCheck")
    SourcedDataCheckerInterface sdc4;
    @EJB(lookup = "java:global/fdaf-webapp/UserOnUserGroupMemberSourcedCheck")
    SourcedDataCheckerInterface sdc5;
    
    public SourcedDataCheckWrapper() {
    }
    
    @Override
    protected void mapSourcedDataCheckers() {
        sourceDataCheckerMap.put("DepartmentOnEmployeeSourcedCheck", sdc1);
        sourceDataCheckerMap.put("EmployeeOnUserSourcedCheck", sdc2);
        sourceDataCheckerMap.put("RoleOnEmployeeSourcedCheck", sdc3);
        sourceDataCheckerMap.put("UserGroupOnUserGroupMemberSourcedCheck", sdc4);
        sourceDataCheckerMap.put("UserOnUserGroupMemberSourcedCheck", sdc5);
    }
    
    @Override
    public void configure(Object callback) {
        super.configure(callback);
        mapSourcedDataCheckers();
    }   
}