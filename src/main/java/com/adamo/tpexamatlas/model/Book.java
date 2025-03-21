package com.adamo.tpexamatlas.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Book extends Document {
    private String author;
    private String isbn;

    @Temporal(TemporalType.DATE)
    private Date datePubl;


    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "ID", foreignKey = @ForeignKey(name = "FK_BOOK_DOCUMENT_ID"))
    public Long getId() {
        return super.getId();
    }


    // Constructors
    public Book() {}

    public Book(String title, Date dateCreate, String author, String isbn, Date datePubl) {
        super(title, dateCreate);
        this.author = author;
        this.isbn = isbn;
        this.datePubl = datePubl;
    }

    // Getters and Setters
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Date getDatePubl() { return datePubl; }
    public void setDatePubl(Date datePubl) { this.datePubl = datePubl; }
}


