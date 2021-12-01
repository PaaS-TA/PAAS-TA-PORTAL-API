package org.openpaas.paasta.portal.api.model;

import java.util.Date;
import java.util.Map;

public class UserDetail {
    private String userId;
    private String userName;
    private String status;
    private String tellPhone;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String adminYn;
    private String imgPath;
    private boolean active;
    private int count;

    private String password;
    private String refreshToken;

    private Date authAccessTime;
    private int authAccessCnt;

    public UserDetail() {
        //empty
    }

    public UserDetail(Map user) {
        this.userId     = (user.containsKey("userId"))? (String) user.get("userId"):null;
        this.userName   = (user.containsKey("userName"))? (String) user.get("userName"):null;
        this.status     = (user.containsKey("status"))? (String) user.get("status"):null;
        this.addressDetail = (user.containsKey("addressDetail"))? (String) user.get("addressDetail"):null;
        this.address    = (user.containsKey("address"))? (String) user.get("address"):null;
        this.tellPhone  = (user.containsKey("tellPhone"))? (String) user.get("tellPhone"):null;
        this.zipCode    = (user.containsKey("zipCode"))? (String) user.get("zipCode"):null;
        this.adminYn    = (user.containsKey("adminYn"))? (String) user.get("adminYn"):null;
        this.imgPath    = (user.containsKey("imgPath"))? (String) user.get("imgPath"):null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAdminYn() {
        return adminYn;
    }

    public void setAdminYn(String adminYn) {
        this.adminYn = adminYn;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Date getAuthAccessTime() {
        return authAccessTime == null ? null : new Date(authAccessTime.getTime());
    }

    public void setAuthAccessTime(Date authAccessTime) {
        this.authAccessTime = authAccessTime == null ? null : new Date(authAccessTime.getTime());
    }

    public int getAuthAccessCnt() {
        return authAccessCnt;
    }

    public void setAuthAccessCnt(int authAccessCnt) {
        this.authAccessCnt = authAccessCnt;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
