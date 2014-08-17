package com.huskycode.jpaquery.types.db;

public class Link {
    private final Column from;
    private final Column to;

    public Link(Column from, Column to) {
        this.from = from;
        this.to = to;
    }

    public Column getFrom() {
        return from;
    }

    public Column getTo() {
        return to;
    }
}
