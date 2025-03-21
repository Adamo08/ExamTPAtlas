package com.adamo.tpexamatlas.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Magazine extends Document {
    private String publisher;
    private String issueNumber;

    @Temporal(TemporalType.DATE)
    private Date dateIssue;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "ID", foreignKey = @ForeignKey(name = "FK_MAGAZINE_DOCUMENT_ID"))
    public Long getId() {
        return super.getId();
    }

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