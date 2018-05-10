package org.openpaas.paasta.portal.api.common;

/**
 * Created by indra on 2018-05-09.
 */
public class ChatObject {

    private String userName;
    private String message;

    public ChatObject() {
        super();
    }
    public ChatObject(String userName, String message) {
        super();
        this.userName = userName;
        this.message = message;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
