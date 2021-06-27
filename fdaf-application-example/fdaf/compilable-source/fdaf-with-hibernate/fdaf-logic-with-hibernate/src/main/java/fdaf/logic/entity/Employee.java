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

import fdaf.base.Gender;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.NotFound;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import fdaf.base.Permission;

import java.io.Serializable;

@Table(name = "employee")
@Entity
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "{employee.firstName.invalid.Pattern}")
    @Size(min = 2, max = 24, message = "{employee.firstName.invalid.Size}")
    @NotBlank(message = "{employee.firstName.invalid.NotBlank}")
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "{employee.lastName.invalid.Pattern}")
    @Size(min = 2, max = 24, message = "{employee.lastName.invalid.Size}")
    @NotBlank(message = "{employee.lastName.invalid.NotBlank}")
    @Column(name = "last_name")
    private String lastName;
    
    @Max(value = 31, message = "{employee.dobDay.invalid.Max}")
    @Min(value = 1, message = "{employee.dobDay.invalid.Min}")
    @Positive(message = "{employee.dobDay.invalid.Positive}")
    @Column(name = "dob_day")
    private Integer dobDay;
    
    @Column(name = "dob_month")
    private Integer dobMonth;
    
    @Positive(message = "{employee.dobYear.invalid.Positive}")
    @Column(name = "dob_year")
    private Integer dobYear;
    
    @Column(name = "employment_id")
    private String employmentId;
    
    private String ssn;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "department_id", nullable = true)
    private Long departmentId;
    
    @Column(name = "role_id", nullable = true)
    private Long roleId;
    
    private String address1;
    private String address2;
    
    @Pattern(regexp = "^(\\+[0-9\\-]+|[0-9\\-]+||[ ]+)$", message = "{employee.phone1.invalid.Pattern}")
    private String phone1;
    
    @Pattern(regexp = "^(\\+[0-9\\-]+|[0-9\\-]+||[ ]+)$", message = "{employee.phone2.invalid.Pattern}")
    private String phone2;
    
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;
    
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;    
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

    public Employee(String firstName,
            String middleName,
            String lastName,
            Integer dobDay,
            Integer dobMonth,
            Integer dobYear,
            String employmentId,
            String ssn,
            Gender gender,
            Long departmentId,
            Long roleId,
            String address1,
            String address2,
            String phone1,
            String phone2,
            Long authorId,
            Long modifierId,
            Long userGroupId,
            String createdDate,
            String modificationDate,
            Permission permission,
            String uuid) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dobDay = dobDay;
        this.dobMonth = dobMonth;
        this.dobYear = dobYear;
        this.employmentId = employmentId;
        this.ssn = ssn;
        this.gender = gender;
        this.departmentId = departmentId;
        this.roleId = roleId;
        this.address1 = address1;
        this.address2 = address2;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.authorId = authorId;
        this.userGroupId = userGroupId;
        this.modifierId = modifierId;
        this.createdDate = createdDate;
        this.modificationDate = modificationDate;
        this.permission = permission;
    }

    public Employee() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDobDay(Integer dobDay) {
        this.dobDay = dobDay;
    }

    public Integer getDobDay() {
        return dobDay;
    }

    public void setDobMonth(Integer dobMonth) {
        this.dobMonth = dobMonth;
    }

    public Integer getDobMonth() {
        return dobMonth;
    }

    public void setDobYear(Integer dobYear) {
        this.dobYear = dobYear;
    }

    public Integer getDobYear() {
        return dobYear;
    }

    public void setEmploymentId(String employmentId) {
        this.employmentId = employmentId;
    }

    public String getEmploymentId() {
        return employmentId;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress2() {
        return address2;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
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
            + "[firstName=" + firstName + "]\n"
            + "[middleName=" + middleName + "]\n"
            + "[lastName=" + lastName + "]\n"
            + "[dobDay=" + dobDay + "]\n"
            + "[dobMonth=" + dobMonth + "]\n"
            + "[dobYear=" + dobYear + "]\n"
            + "[employmentId=" + employmentId + "]\n"
            + "[ssn=" + ssn + "]\n"
            + "[gender=" + gender + "]\n"
            + "[departmentId=" + departmentId + "]\n"
            + "[roleId=" + roleId + "]\n"
            + "[address1=" + address1 + "]\n"
            + "[address2=" + address2 + "]\n"
            + "[phone1=" + phone1 + "]\n"
            + "[phone2=" + phone2 + "]\n"
            + "[authorId=" + authorId + "]\n"
            + "[modifierId=" + modifierId + "]\n"
            + "[userGroupId=" + userGroupId + "]\n"
            + "[createdDate=" + createdDate + "]\n"
            + "[modificationDate=" + modificationDate + "]\n"
            + "[permission=" + permission + "]\n"
            + "[uuid=" + uuid + "]";
    }
}