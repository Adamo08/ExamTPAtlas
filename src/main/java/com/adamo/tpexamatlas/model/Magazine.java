package com.adamo.tpexamatlas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class Magazine extends Document {
    private String publisher;
    private String issueNumber;

    @Temporal(TemporalType.DATE)
    private Date dateIssue;

    // Constructors
    public Magazine() {}

    public Magazine(String title, Date dateCreate, String publisher, String issueNumber, Date dateIssue) {
        super(title, dateCreate);
        this.publisher = publisher;
        this.issueNumber = issueNumber;
        this.dateIssue = dateIssue;
    }

    // Getters and Setters
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getIssueNumber() { return issueNumber; }
    public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }
    public Date getDateIssue() { return dateIssue; }
    public void setDateIssue(Date dateIssue) { this.dateIssue = dateIssue; }
}