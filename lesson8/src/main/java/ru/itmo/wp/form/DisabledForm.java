package ru.itmo.wp.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DisabledForm {
    @NotNull
    private long userId;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "enable|disable", message = "Button value can be only enable/disable")
    private String value;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getBooleanValue() {
        return value.equals("enable");
    }
}
