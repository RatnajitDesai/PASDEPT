package com.hecvd19.pasdept.models;

public class Department {

    private String departmentId, departmentLogo, departmentName, emailId;
    private boolean isApproved;
    private String departmentUserId;

    public Department() {

    }

    public Department(String departmentId, String departmentLogo, String departmentName, String emailId, boolean isApproved, String departmentUserId) {
        this.departmentId = departmentId;
        this.departmentLogo = departmentLogo;
        this.departmentName = departmentName;
        this.emailId = emailId;
        this.isApproved = isApproved;
        this.departmentUserId = departmentUserId;
    }

    public String getDepartmentUserId() {
        return departmentUserId;
    }

    public void setDepartmentUserId(String departmentUserId) {
        this.departmentUserId = departmentUserId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentLogo() {
        return departmentLogo;
    }

    public void setDepartmentLogo(String departmentLogo) {
        this.departmentLogo = departmentLogo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId='" + departmentId + '\'' +
                ", departmentLogo='" + departmentLogo + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", isApproved=" + isApproved +
                ", departmentUserId='" + departmentUserId + '\'' +
                '}';
    }


}
