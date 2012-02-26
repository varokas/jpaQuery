package com.huskycode.jpaquery.testmodel;

import javax.persistence.*;

/**
 * Just a generic annotated entity class for testing.
 * Defines id and optional foreign id. All test classes
 * extends this one.
 *
 * @author Varokas Panusuwan
 */
@MappedSuperclass
public abstract class BaseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "foreign_id", nullable = true)
    private Integer foreignId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getForeignId() {
        return foreignId;
    }

    public void setForeignId(Integer foreignId) {
        this.foreignId = foreignId;
    }
}
