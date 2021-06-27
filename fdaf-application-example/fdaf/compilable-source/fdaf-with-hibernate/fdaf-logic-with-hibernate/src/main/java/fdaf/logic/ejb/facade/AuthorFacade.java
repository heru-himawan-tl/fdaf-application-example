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
package fdaf.logic.ejb.facade;

import fdaf.base.EditStateIndexingInterface;
import fdaf.base.FacadeInterface;
import fdaf.logic.base.AbstractFacade;
import fdaf.logic.base.Specification;
import fdaf.logic.ejb.repository.AuthorRepository;
import fdaf.logic.entity.Author;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Remote({FacadeInterface.class})
@Stateful
public class AuthorFacade extends AbstractFacade<AuthorRepository, Author>
        implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB(lookup = "java:global/fdaf/fdaf-logic/EditStateIndexing")
    EditStateIndexingInterface editStateIndexing;
    
    @EJB
    private AuthorRepository repository;

    public AuthorFacade() {
        super(Author.class);
    }

    protected AuthorRepository getRepository() {
        return repository;
    }

    protected Author newEntity() {
        return new Author();
    }
    
    @Override
    protected EditStateIndexingInterface getEditStateIndexing() {
        return editStateIndexing;
    }

    protected void setUuid() {
        entity.setUuid(uuid);
    }

    public Object getNewRecordId() {
        return entity.getId();
    }

    @Override
    protected void updateDataProperties(boolean updateMode) {
        updateRecordDate();
        entity.setModificationDate(recordDate);
        if (!updateMode) {
            entity.setAuthorId(authorId);
            modifierId = authorId;
            entity.setCreatedDate(recordDate);
        }
        entity.setModifierId(modifierId);
    }
}