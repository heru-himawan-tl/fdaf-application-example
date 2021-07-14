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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.NotFound;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import fdaf.base.Permission;

import java.io.Serializable;

@Table(name = "staff_invitation")
@Entity
public class StaffInvitation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\.]+@[a-zA-Z0-9\\-\\.]+$", message = "{staffInvitation.email.invalid.Pattern}")
    @NotBlank(message = "{staffInvitation.email.invalid.NotBlank}")
    private String email;
    
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$", message = "{staffInvitation.signature.invalid.Pattern}")
    @NotBlank(message = "{staffInvitation.signature.invalid.NotBlank}")
    @Size(min = 6, max = 128, message = "{staffInvitation.signature.invalid.Size}")
    private String signature;
    
    private String subject;
    
    private String message;    
    private String uuid;
    @Column(name = "author_id", nullable = true)
    private Long authorId;
    @Column(name = "user_group_id", nullable = true)
    private Long userGroupId;
    @Column(name = "modifier_id", nullable = true)
    private Long modifierId;
    @Column(name = "created_date", nullable = true)
    private String createdDate;
    @Column(name = "modification_date", nullable = true)
    private String modificationDate;
    @Enumerated(EnumType.STRING)
    private Permission permission;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    private UserGroup userGroup;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "modifier_id", insertable = false, updatable = false)
    private Modifier modifier;

    public StaffInvitation(String email,
            String signature,
            String subject,
            String message,
            Long authorId,
            Long modifierId,
            Long userGroupId,
            String createdDate,
            String modificationDate,
            Permission permission,
            String uuid) {
        this.email = email;
        this.signature = signature;
        this.subject = subject;
        this.message = message;
        this.authorId = authorId;
        this.userGroupId = userGroupId;
        this.modifierId = modifierId;
        this.createdDate = createdDate;
        this.modificationDate = modificationDate;
        this.permission = permission;
    }

    public StaffInvitation() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getUserGroupId() {
        return userGroupId;
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

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
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
        if (!(object instanceof StaffInvitation)) {
            return false;
        }
        StaffInvitation other = (StaffInvitation) object;
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
            + "[email=" + email + "]\n"
            + "[signature=" + signature + "]\n"
            + "[subject=" + subject + "]\n"
            + "[message=" + message + "]\n"
            + "[authorId=" + authorId + "]\n"
            + "[modifierId=" + modifierId + "]\n"
            + "[userGroupId=" + userGroupId + "]\n"
            + "[createdDate=" + createdDate + "]\n"
            + "[modificationDate=" + modificationDate + "]\n"
            + "[permission=" + permission + "]\n"
            + "[uuid=" + uuid + "]";
    }
}