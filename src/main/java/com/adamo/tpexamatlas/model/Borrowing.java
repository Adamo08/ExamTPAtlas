package com.adamo.tpexamatlas.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "borrowing")
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_BORROWING_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "document_id", foreignKey = @ForeignKey(name = "FK_BORROWING_DOCUMENT"))
    private Document document;

    @Temporal(TemporalType.DATE)
    private Date dateBorrow;

    @Temporal(TemporalType.DATE)
    private Date returnDate;

    // Constructors
    public Borrowing() {}

    public Borrowing(User user, Document document, Date dateBorrow, Date returnDate) {
        this.user = user;
        this.document = document;
        this.dateBorrow = dateBorrow;
        this.returnDate = returnDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }
    public Date getDateBorrow() { return dateBorrow; }
    public void setDateBorrow(Date startDate) { this.dateBorrow = startDate; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date endDate) { this.returnDate = endDate; }
}
