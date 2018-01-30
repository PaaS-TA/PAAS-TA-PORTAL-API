package org.openpaas.paasta.portal.api.model;

import java.util.Date;

/**
 * Created by Dojun on 2016-06-07.
 */
public class Question {

    private int no;
    private String classification;
    private String title;
    private String userId;
    private String content;
    private String cellPhone;
    private String status;
    private String fileName;
    private String filePath;
    private Date created;
    private Date lastModified;

    public Question(){
        //empty
    }

/*
    public Question(Map question) {

        String title = ""+question.get("title");
        String userId = ""+question.get("userId");
        String content = ""+question.get("content");
        String cellPhone = ""+question.get("cellPhone");
        String status = ""+question.get("status");
        Date created = (Date)question.get("created");
        Date lastModified = (Date)question.get("lastModified");

        if (title != null && !"".equals(title))                this.title = title;
        if (userId != null && !"".equals(userId))              this.userId = userId;
        if (content != null && !"".equals(content))            this.content = content;
        if (cellPhone != null && !"".equals(cellPhone))        this.cellPhone = cellPhone;
        if (status != null && !"".equals(status))              this.status = status;
        if (created != null && !"".equals(created))            this.created = created;
        if (lastModified != null && !"".equals(lastModified))  this.lastModified = lastModified;

    }
*/


    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
