package ru.itmo.wp.model.domain;

import java.util.Map;

public class Event extends Entity {
    private long userId;
    private Type type;

    public long getUserId() {
        return userId;
    }

    public Type getType() {
        return type;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public enum Type {
        ENTER("ENTER"), LOGOUT("LOGOUT");
        public static Map<String, Type> stringToTypeMather = Map.of("ENTER", ENTER, "LOGOUT", LOGOUT);
        String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
