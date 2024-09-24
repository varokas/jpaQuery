package com.huskycode.jpaquery.types.db.factory;

import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.ColumnImpl;
import com.huskycode.jpaquery.types.db.Table;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.equalTo;

public class TableFactoryTest {

    private TableFactory tableFactory;

    @BeforeEach
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
        System.out.println(table.getColumns());
        assertThat(table.getColumns(), hasSize(2));
        assertThat(table.getColumns(), hasItem((Column)new ColumnImpl(table, "someColumn", Integer.class)));
        assertThat(table.column("someColumn"), Matchers.equalTo((Column)new ColumnImpl(table, "someColumn", Integer.class)));
    }

    @Test
    public void createFromJPAEntityReturnCorrectMapping() throws Exception {
        TableFactory.TableWithMapping tableWithMapping = tableFactory.createWithMappingFromJPAEntity(JPAEntity.class);
        Table table = tableWithMapping.getTable();
        Map<Column, Field> map = tableWithMapping.getColumnFieldMap();

        assertThat(map.size(), is(2));
        assertThat(map, hasEntry(table.column("someColumn"), JPAEntity.class.getDeclaredField("columnA")));
        assertThat(map, hasEntry(table.column("columnB"), JPAEntity.class.getDeclaredField("columnB")));
    }

    @Test
    public void createFromJPAEntityUseClassNameAsTableName() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntityWithoutTable.class);

        assertThat(table.getName(), equalTo("JPAEntityWithoutTable"));
    }

    @Test
    public void createFromJPAEntityUseFieldAsColumnNameAndAType() throws Exception {
        Table table = tableFactory.createFromJPAEntity(JPAEntity.class);

        assertThat(table.getColumns(), hasItem((Column) new ColumnImpl(table, "columnB", String.class)));
        assertThat(table.column("columnB"), Matchers.equalTo((Column) new ColumnImpl(table, "columnB", String.class)));
    }

    @Test
    public void createFromJPAEntityThrowsExceptionIfInputIsNotAnEntity() {
		assertThrows(TableFactory.NotJPAEntityException.class, () ->
			tableFactory.createFromJPAEntity(String.class));
	}


    @Entity
    public class JPAEntityWithoutTable {
        @Id
        private Integer columnA;
    }

    @Entity
    @jakarta.persistence.Table(name = "someTable")
    public class JPAEntity {
        @jakarta.persistence.Column(name="someColumn")
        @Id
        private Integer columnA;

        @jakarta.persistence.Column
        private String columnB;
    }
}
