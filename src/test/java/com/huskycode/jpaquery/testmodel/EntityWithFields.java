package com.huskycode.jpaquery.testmodel;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author Varokas Panusuwan
 */
public class EntityWithFields extends BaseClass {
    @Column
    private Integer intField;

    @Column(length = 50)
    private String stringField;

    @Column
    private Date dateField;
}
