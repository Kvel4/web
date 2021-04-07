package ru.itmo.wp.form;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CommentForm {
    @NotEmpty
    @Size(min = 1, max = 1000, message = "Field can't be empty")
    @Lob
    private String text;

    @NotEmpty
    private String jwt;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
