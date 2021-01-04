package ru.dolinini.notebook.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "notebookentry")
public class NotebookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "dateofcreation")
    private Date dateOfCreation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public NotebookEntry() {
    }

    public NotebookEntry(String title, String content) {
        this.title = title;
        this.content = content;
        this.dateOfCreation = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotebookEntry that = (NotebookEntry) o;
        return id.equals(that.id) &&
                title.equals(that.title) &&
                Objects.equals(content, that.content) &&
                dateOfCreation.equals(that.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, dateOfCreation);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}