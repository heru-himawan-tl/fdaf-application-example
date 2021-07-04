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
package fdaf.logic.ejb.callback;

import fdaf.logic.base.AbstractUpdateCallback;
import fdaf.logic.base.Specification;
import fdaf.logic.base.UpdateCallbackInterface;
import fdaf.logic.ejb.repository.RoleRepository;
import fdaf.logic.entity.Role;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Remote({UpdateCallbackInterface.class})
@Stateful
public class RoleUpdateCallback extends AbstractUpdateCallback
        implements UpdateCallbackInterface<RoleRepository, Role>, Serializable {
        
    private static final long serialVersionUID = 1L;
    private RoleRepository repository;
    private Role entity;

    public RoleUpdateCallback() {
        // NO-OP
    }

    public void setRepository(RoleRepository repository) {
        this.repository = repository;
    }

    public void setEntity(Role entity) {
        this.entity = entity;
    }
    
    public Role getEntity() {
        return entity;
    }

    public boolean preCreateCheck() {
        Specification<Role> spec = repository.presetSpecification();
        spec.setPredicate(spec.getBuilder().equal(spec.getRoot().get("name"), entity.getName()));
        if (repository.find(spec) == null) {
            return true;
        }
        setMessage("newRoleDuplicated");
        return false;
    }

    public boolean preUpdateCheck() {
        Specification<Role> spec = repository.presetSpecification();
        spec.setPredicate(spec.getBuilder().and(
            spec.getBuilder().notEqual(spec.getRoot().get("uuid"), entity.getUuid()),
            spec.getBuilder().equal(spec.getRoot().get("name"), entity.getName())));
        if (repository.find(spec) == null) {
            return true;
        }
        setMessage("updateRoleDuplicated");
        return false;
    }
}