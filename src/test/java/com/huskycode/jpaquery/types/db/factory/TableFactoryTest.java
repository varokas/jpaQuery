package com.huskycode.jpaquery.types.db.factory;

import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.ColumnImpl;
import com.huskycode.jpaquery.types.db.JPAEntityTable;
import com.huskycode.jpaquery.types.db.Table;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableFactoryTest {

    private TableFactory tableFactory;

    @Before
    public void setUp() throws Exception {
        tableFactory = new TableFactory();
    }

    @Test
    public void createFromJPAEntityWithACorrectName() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntity.class);

        assertThat(table.getName(), equalTo("someTable"));
    }

    @Test
    public void createFromJPAEntityWithAColumn() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntity.class);

        assertThat(table.getName(), equalTo("someTable"));
        assertThat(table.getColumns(), hasItem((Column)new ColumnImpl("someColumn", Integer.class)));
    }

    @Test
    public void createFromJPAEntityUseClassNameAsTableName() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntityWithoutTable.class);

        assertThat(table.getName(), equalTo("JPAEntityWithoutTable"));
    }

    @Test
    public void createFromJPAEntityUseFieldAsColumnNameAndAType() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntity.class);

        assertThat(table.getColumns(), hasItem((Column)new ColumnImpl("columnB", String.class)));
    }

    @Test
    public void createFromJPAEntityReturnsEntityWithOriginalClass() throws Exception {
        JPAEntityTable<JPAEntity> table = tableFactory.createFromJPAEntity(JPAEntity.class);

        assertThat(table.getJpaEntity(), equalTo(JPAEntity.class));
    }

    @Test(expected = TableFactory.NotJPAEntityException.class)
    public void createFromJPAEntityThrowsExceptionIfInputIsNotAnEntity() throws Exception {
        tableFactory.createFromJPAEntity(String.class);
    }


    @Entity
    public class JPAEntityWithoutTable {
        @Id
        private Integer columnA;
    }

    @Entity
    @javax.persistence.Table(name = "someTable")
    public class JPAEntity {
        @javax.persistence.Column(name="someColumn")
        @Id
        private Integer columnA;

        @javax.persistence.Column
        private String columnB;
    }
}
