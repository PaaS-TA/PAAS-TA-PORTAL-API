package org.openpaas.paasta.portal.api.model;

import java.util.Date;

/**
 * 클래스 설명
 *
 * @author ijlee
 * @version 1.0
 * @since 2016.11.01
 */

public class InviteOrgSpace extends Entity {

    private int id;
    private String token;
    private String gubun;
    private int inviteId;
    private String roleName;
    private String inviteUserId;
    private String userId;
    private Date createTime;
    private int accessCnt;
    private String inviteName;
    private String setyn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public int getInviteId() {
        return inviteId;
    }

    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(String inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getAccessCnt() {
        return accessCnt;
    }

    public void setAccessCnt(int accessCnt) {
        this.accessCnt = accessCnt;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public String getSetyn() {
        return setyn;
    }

    public void setSetyn(String setyn) {
        this.setyn = setyn;
    }



}
