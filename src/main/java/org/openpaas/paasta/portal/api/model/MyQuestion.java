package org.openpaas.paasta.portal.api.model;

/**
 * org.openpaas.paasta.portal.web.admin.model
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.06.17
 */
public class MyQuestion {

    private int no;
    private String title;
    private String classification;
    private String classificationValue;
    private String userId;
    private String content;
    private String cellPhone;
    private String status;
    private String statusValue;
    private String fileName;
    private String filePath;
    private int fileSize;
    private String created;
    private String lastModified;

    private int pageNo;
    private int pageSize;
    private String searchKeyword;
    private int totalCount;

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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getClassificationValue() {
        return classificationValue;
    }

    public void setClassificationValue(String classificationValue) {
        this.classificationValue = classificationValue;
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

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
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

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "MyQuestion{" +
                "no=" + no +
                ", title='" + title + '\'' +
                ", classification='" + classification + '\'' +
                ", classificationValue='" + classificationValue + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", cellPhone='" + cellPhone + '\'' +
                ", status='" + status + '\'' +
                ", statusValue='" + statusValue + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", created='" + created + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", searchKeyword='" + searchKeyword + '\'' +
                ", totalCount=" + totalCount +
                '}';
    }
}
