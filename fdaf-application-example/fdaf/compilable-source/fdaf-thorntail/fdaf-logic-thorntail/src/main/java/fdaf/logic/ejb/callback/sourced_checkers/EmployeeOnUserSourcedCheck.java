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
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSEp
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package fdaf.logic.ejb.callback.sourced_checkers;

import fdaf.logic.base.SourcedDataCheckerInterface;
import fdaf.logic.base.Specification;
import fdaf.logic.ejb.repository.UserRepository;
import fdaf.logic.entity.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Remote({SourcedDataCheckerInterface.class})
@Stateful(passivationCapable = false)
public class EmployeeOnUserSourcedCheck implements Serializable {

    private static final long serialVersionUID = 1L;    

    @EJB
    private UserRepository userRepository;

    public EmployeeOnUserSourcedCheck() {
        // NO-OP
    }
    
    public boolean isSourced(Object primaryKey) {
        Specification<User> spec = userRepository.presetSpecification();
        spec.setPredicate(spec.getBuilder().equal(spec.getRoot().get("employeeId"), primaryKey));
        if (userRepository.find(spec) != null) {
            return true;
        }
        return false;
    }
}