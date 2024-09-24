package com.huskycode.jpaquery.testmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Just a generic annotated entity class for testing
 *
 * @author Varokas Panusuwan
 */
@Entity
@Table(name = "table_b")
public class ClassB extends BaseClass {
}