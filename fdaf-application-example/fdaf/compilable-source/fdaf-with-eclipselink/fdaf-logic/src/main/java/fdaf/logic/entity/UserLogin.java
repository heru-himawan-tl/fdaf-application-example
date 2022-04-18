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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import fdaf.base.Permission;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "user_login")
@Entity
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = true)
    private Long userId;
    
    @Column(name = "out_epoch_time_stamp")
    private Long outEpochTimeStamp;
    
    @Column(name = "out_time_stamp")
    private String outTimeStamp;
    
    @Column(name = "in_epoch_time_stamp")
    private Long inEpochTimeStamp;
    
    @Column(name = "in_time_stamp")
    private String inTimeStamp;
    
    @Column(name = "user_session_id", nullable = false)
    private String userSessionID;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "logout_state", nullable = false)
    private Boolean logoutState;
    
    @Column(name = "logout_flag")
    private Integer logoutFlag;
    
    @Column(name = "live_time")
    private Integer liveTime;
    
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
    @OneToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Author author;
    @OneToOne
    @JoinColumn(name = "modifier_id", insertable = false, updatable = false)
    private Modifier modifier;

    public UserLogin(Long userId,
            Long outEpochTimeStamp,
            String outTimeStamp,
            Long inEpochTimeStamp,
            String inTimeStamp,
            String userSessionID,
            String userAgent,
            Boolean logoutState,
            Integer logoutFlag,
            Integer liveTime,
            Long authorId,
            Long modifierId,
            String createdDate,
            String modificationDate,
            String uuid) {
        this.userId = userId;
        this.outEpochTimeStamp = outEpochTimeStamp;
        this.outTimeStamp = outTimeStamp;
        this.inEpochTimeStamp = inEpochTimeStamp;
        this.inTimeStamp = inTimeStamp;
        this.userSessionID = userSessionID;
        this.userAgent = userAgent;
        this.logoutState = logoutState;
        this.logoutFlag = logoutFlag;
        this.liveTime = liveTime;
        this.authorId = authorId;
        this.modifierId = modifierId;
        this.createdDate = createdDate;
        this.modificationDate = modificationDate;
    }

    public UserLogin() {
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

    public void setOutEpochTimeStamp(Long outEpochTimeStamp) {
        this.outEpochTimeStamp = outEpochTimeStamp;
    }

    public Long getOutEpochTimeStamp() {
        return outEpochTimeStamp;
    }

    public void setOutTimeStamp(String outTimeStamp) {
        this.outTimeStamp = outTimeStamp;
    }

    public String getOutTimeStamp() {
        return outTimeStamp;
    }

    public void setInEpochTimeStamp(Long inEpochTimeStamp) {
        this.inEpochTimeStamp = inEpochTimeStamp;
    }

    public Long getInEpochTimeStamp() {
        return inEpochTimeStamp;
    }

    public void setInTimeStamp(String inTimeStamp) {
        this.inTimeStamp = inTimeStamp;
    }

    public String getInTimeStamp() {
        return inTimeStamp;
    }

    public void setUserSessionID(String userSessionID) {
        this.userSessionID = userSessionID;
    }

    public String getUserSessionID() {
        return userSessionID;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setLogoutState(Boolean logoutState) {
        this.logoutState = logoutState;
    }

    public Boolean getLogoutState() {
        return logoutState;
    }

    public void setLogoutFlag(Integer logoutFlag) {
        this.logoutFlag = logoutFlag;
    }

    public Integer getLogoutFlag() {
        return logoutFlag;
    }

    public void setLiveTime(Integer liveTime) {
        this.liveTime = liveTime;
    }

    public Integer getLiveTime() {
        return liveTime;
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
        if (!(object instanceof UserLogin)) {
            return false;
        }
        UserLogin other = (UserLogin) object;
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
            + "[outEpochTimeStamp=" + outEpochTimeStamp + "]\n"
            + "[outTimeStamp=" + outTimeStamp + "]\n"
            + "[inEpochTimeStamp=" + inEpochTimeStamp + "]\n"
            + "[inTimeStamp=" + inTimeStamp + "]\n"
            + "[userSessionID=" + userSessionID + "]\n"
            + "[userAgent=" + userAgent + "]\n"
            + "[logoutState=" + logoutState + "]\n"
            + "[logoutFlag=" + logoutFlag + "]\n"
            + "[liveTime=" + liveTime + "]\n"
            + "[authorId=" + authorId + "]\n"
            + "[modifierId=" + modifierId + "]\n"
            + "[createdDate=" + createdDate + "]\n"
            + "[modificationDate=" + modificationDate + "]\n"
            + "[uuid=" + uuid + "]";
    }
}