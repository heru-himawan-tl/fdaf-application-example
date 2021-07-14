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

import fdaf.base.UserType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import fdaf.base.Permission;

import java.io.Serializable;

@Table(name = "user")
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "{user.userName.invalid.Pattern}")
    @Size(min = 4, max = 128, message = "{user.userName.invalid.Size}")
    @NotBlank(message = "{user.userName.invalid.NotBlank}")
    @Column(name = "user_name")
    private String userName;
    
    @Transient
    private String currentPassword;
    
    @Transient
    private String newPassword;
    
    @Transient
    private String passwordConfirm;
    
    private String password;
    
    private String email;
    
    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    
    @Column(name = "time_stamp")
    private Long timeStamp;
    
    @Column(name = "employee_id", nullable = true)
    private Long employeeId;
    
    private Boolean enabled;
    
    @OneToOne
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;    
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
    @OneToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;
    @OneToOne
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    private UserGroup userGroup;
    @OneToOne
    @JoinColumn(name = "modifier_id", insertable = false, updatable = false)
    private Modifier modifier;

    public User(String userName,
            String currentPassword,
            String newPassword,
            String passwordConfirm,
            String password,
            String email,
            UserType userType,
            Long timeStamp,
            Long employeeId,
            Boolean enabled,
            Long authorId,
            Long modifierId,
            Long userGroupId,
            String createdDate,
            String modificationDate,
            Permission permission,
            String uuid) {
        this.userName = userName;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.passwordConfirm = passwordConfirm;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.timeStamp = timeStamp;
        this.employeeId = employeeId;
        this.enabled = enabled;
        this.authorId = authorId;
        this.userGroupId = userGroupId;
        this.modifierId = modifierId;
        this.createdDate = createdDate;
        this.modificationDate = modificationDate;
        this.permission = permission;
    }

    public User() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
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
            + "[userName=" + userName + "]\n"
            + "[currentPassword=" + currentPassword + "]\n"
            + "[newPassword=" + newPassword + "]\n"
            + "[passwordConfirm=" + passwordConfirm + "]\n"
            + "[password=" + password + "]\n"
            + "[email=" + email + "]\n"
            + "[userType=" + userType + "]\n"
            + "[timeStamp=" + timeStamp + "]\n"
            + "[employeeId=" + employeeId + "]\n"
            + "[enabled=" + enabled + "]\n"
            + "[authorId=" + authorId + "]\n"
            + "[modifierId=" + modifierId + "]\n"
            + "[userGroupId=" + userGroupId + "]\n"
            + "[createdDate=" + createdDate + "]\n"
            + "[modificationDate=" + modificationDate + "]\n"
            + "[permission=" + permission + "]\n"
            + "[uuid=" + uuid + "]";
    }
}