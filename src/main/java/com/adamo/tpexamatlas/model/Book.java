package com.adamo.tpexamatlas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class Book extends Document {
    private String author;
    private String isbn;

    @Temporal(TemporalType.DATE)
    private Date datePubl;

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


