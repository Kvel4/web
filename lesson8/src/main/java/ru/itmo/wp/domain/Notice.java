package ru.itmo.wp.domain;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@SuppressWarnings("unused")
@Entity
@Table(
        indexes = @Index(columnList = "creationTime")
)
public class Notice {
    @Id
    @GeneratedValue
    private long id;

    @Lob
    @NotNull
    @NotEmpty
    @Size
    private String Content;

    @CreationTimestamp
    private Date creationTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
