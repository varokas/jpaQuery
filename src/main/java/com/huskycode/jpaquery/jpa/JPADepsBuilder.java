package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.jpa.persister.JPARowPersister;
import com.huskycode.jpaquery.jpa.util.JPAUtil;
import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.Link;
import com.huskycode.jpaquery.types.db.Table;
import com.huskycode.jpaquery.types.db.factory.TableFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.*;

public class JPADepsBuilder {
    private final List<Link> links;
    private final Set<Table> enumTables;
    private final Set<Table> triggeredTables;

    private Map<String, TableAndEntity> collectedTablesByName = new HashMap<String, TableAndEntity>();
    private Map<Column, Field> collectedColumnAndField = new HashMap<Column, Field>();

    private final TableFactory tableFactory = new TableFactory();

    public JPADepsBuilder() {
        links = new LinkedList<Link>();
        enumTables = new HashSet<Table>();
        triggeredTables = new HashSet<Table>();
    }

    public JPADepsBuilder withLink(final com.huskycode.jpaquery.link.Link<?,?,?> link) {
        links.add(createLinkFromJPALink(collectedTablesByName, link));
        return this;
    }

    public JPADepsBuilder withLinks(final com.huskycode.jpaquery.link.Link<?,?,?> links[]) {
        for(com.huskycode.jpaquery.link.Link link : links) {
           withLink(link);
        }
        return this;
    }

    public JPADepsBuilder withEnumTable(final Class<?> e) {
        enumTables.add(getOrCreateTable(collectedTablesByName, e));
        return this;
    }

    public JPADepsBuilder withEnumTables(final Class<?>[] es) {
        for(Class<?> e : es) {
            withEnumTable(e);
        }
        return this;
    }

    public JPADepsBuilder withTriggeredTable(final Class<?> e) {
        triggeredTables.add(getOrCreateTable(collectedTablesByName, e));
        return this;
    }

    public JPADepsBuilder withTriggeredTables(final Class<?>[] es) {
        for(Class<?> e : es) {
            withTriggeredTable(e);
        }
        return this;
    }

    public DependenciesContext build(EntityManager entityManager) {
        return new DependenciesContext(new GenericDependenciesDefinition(
                links,
                enumTables,
                triggeredTables
        ), new JPARowPersister(
                entityManager,
                createTableToEntityClassMap(collectedTablesByName.values())
        ));
    }



    private Map<Table, Class<?>> createTableToEntityClassMap(Collection<TableAndEntity> tableAndEntities) {
        Map<Table, Class<?>> map = new HashMap<Table, Class<?>>();
        for(TableAndEntity tableAndEntity : tableAndEntities) {
            map.put(tableAndEntity.table, tableAndEntity.entityClass);
        }

        return map;
    }

    private com.huskycode.jpaquery.types.db.Link createLinkFromJPALink(
            Map<String, TableAndEntity> collectedTablesByName, com.huskycode.jpaquery.link.Link link) {
        Table fromTable = getOrCreateTable(collectedTablesByName, link.getFrom().getEntityClass());
        Table toTable = getOrCreateTable(collectedTablesByName, link.getTo().getEntityClass());

        Column fromColumn = fromTable.column( JPAUtil.getColumnNameOrDefault(link.getFrom().getField()) );
        Column toColumn = toTable.column( JPAUtil.getColumnNameOrDefault(link.getTo().getField()) );

        return new com.huskycode.jpaquery.types.db.Link(fromColumn, toColumn);
    }

    private Table getOrCreateTable(Map<String, TableAndEntity> collectedTablesByName, Class entityClass) {
        String tableName = JPAUtil.getTableName(entityClass);
        if(!collectedTablesByName.containsKey(tableName)) {
            collectedTablesByName.put(tableName,
                    new TableAndEntity(tableFactory.createFromJPAEntity(entityClass), entityClass)
            );
        }

        return collectedTablesByName.get(tableName).table;
    }

    class TableAndEntity {
        private Table table;
        private Class entityClass;

        TableAndEntity(Table table, Class entityClass) {
            this.table = table;
            this.entityClass = entityClass;
        }
    }
}
