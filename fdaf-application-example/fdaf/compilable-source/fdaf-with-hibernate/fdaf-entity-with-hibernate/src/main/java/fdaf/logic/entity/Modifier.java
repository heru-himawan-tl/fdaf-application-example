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
package fdaf.logic.entity;

import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.NotFound;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import fdaf.base.Permission;

import java.io.Serializable;

@Table(name = "modifier")
@Entity
public class Modifier implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;    
    private String uuid;

    @Column(name = "author_id", nullable = true)
    private Long authorId;
    @Column(name = "modifier_id", nullable = true)
    private Long modifierId;
    @Column(name = "created_date", nullable = true)
    private String createdDate;
    @Column(name = "modification_date", nullable = true)
    private String modificationDate;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "modifier_id", insertable = false, updatable = false)
    private Modifier modifier;

    public Modifier(Long userId,
            Long authorId,
            Long modifierId,
            String createdDate,
            String modificationDate,
            String uuid) {
        this.userId = userId;
        this.authorId = authorId;
        this.modifierId = modifierId;
        this.createdDate = createdDate;
        this.modificationDate = modificationDate;
    }

    public Modifier() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null) ? id.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Modifier)) {
            return false;
        }
        Modifier other = (Modifier) object;
        if (((this.getId() == null) && (other.getId() != null))
                || ((this.getId() != null) && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName()
            + "[id=" + id + "]\n"
            + "[userId=" + userId + "]\n"
            + "[authorId=" + authorId + "]\n"
            + "[modifierId=" + modifierId + "]\n"
            + "[createdDate=" + createdDate + "]\n"
            + "[modificationDate=" + modificationDate + "]\n"
            + "[uuid=" + uuid + "]";
    }
}