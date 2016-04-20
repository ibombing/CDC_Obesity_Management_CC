package edu.gatech.johndoe.carecoordinator.care_plan;

import java.util.Date;

public class CarePlan {
    private String id;
    private String fhirId;
    private String patientID;
    private String title;
    private String detail;
    private boolean pending;
    private Date issueDate;
    private Date dateOfimport;
    private int status; /* number coding
                            1: unopened
                            2: opened
                            3: sent recommendation
                            4: sent E-referral to community

                        */

    public CarePlan() {}

    public CarePlan(String id, String fhirId, String patientID, String title, String detail, boolean pending, Date issueDate) {
        this.id = id;
        this.fhirId = fhirId;
        this.patientID = patientID;
        this.title = title;
        this.detail = detail;
        this.pending = pending;
        this.issueDate = issueDate;
        this.dateOfimport = new Date();
        this.status = 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFhirId() {
        return fhirId;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDateOfimport() {
        return dateOfimport;
    }

    public void setDateOfimport(Date dateOfimport) {
        this.dateOfimport = dateOfimport;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "CarePlan{" +
                "id='" + id + '\'' +
                ", patientID='" + patientID + '\'' +
                ", title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", pending=" + pending +
                ", issueDate=" + issueDate +
                ", dateOfimport=" + dateOfimport +
                ", Status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CarePlan) {
            if (hashCode() == o.hashCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(id);
    }
}