package com.huskycode.jpaquery.jpa;

import com.huskycode.jpaquery.GenericDependenciesDefinition;
import com.huskycode.jpaquery.jpa.persister.JPARowPersister;
import com.huskycode.jpaquery.jpa.util.JPAUtil;
import com.huskycode.jpaquery.link.Link;
import com.huskycode.jpaquery.types.db.Column;
import com.huskycode.jpaquery.types.db.JPAEntityTable;
import com.huskycode.jpaquery.types.db.Table;
import com.huskycode.jpaquery.types.db.factory.TableFactory;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class JPADepsBuilder {
    private final List<Link<?,?,?>> links;
    private final List<Class<?>> enumTables;
    private final List<Class<?>> triggeredTables;

    private final TableFactory tableFactory = new TableFactory();

    public JPADepsBuilder() {
        links = new LinkedList<Link<?,?,?>>();
        enumTables = new LinkedList<Class<?>>();
        triggeredTables = new LinkedList<Class<?>>();
    }

    public JPADepsBuilder withLink(final Link<?,?,?> link) {
        links.add(link);
        return this;
    }

    public JPADepsBuilder withLinks(final Link<?, ?, ?>[] linkArray) {
        links.addAll(Arrays.asList(linkArray));
        return this;
    }

    public JPADepsBuilder withEnumTable(final Class<?> e) {
        enumTables.add(e);
        return this;
    }

    public JPADepsBuilder withEnumTables(final Class<?>[] es) {
        enumTables.addAll(Arrays.asList(es));
        return this;
    }

    public JPADepsBuilder withTriggeredTable(final Class<?> e) {
        triggeredTables.add(e);
        return this;
    }

    public JPADepsBuilder withTriggeredTables(final Class<?>[] es) {
        triggeredTables.addAll(Arrays.asList(es));
        return this;
    }

    public DependenciesContext build() {
        List<com.huskycode.jpaquery.types.db.Link> genericLinks = createLinkFromJPALink(links);

        return new DependenciesContext(new GenericDependenciesDefinition(
                genericLinks,
                new HashSet<Table>(),
                new HashSet<Table>()
        ), new JPARowPersister());
    }

    private List<com.huskycode.jpaquery.types.db.Link> createLinkFromJPALink(List<Link<?, ?, ?>> links) {
        //Map<String, Table> collectedTablesByName = new HashMap<String, Table>();

        List<com.huskycode.jpaquery.types.db.Link> result = new ArrayList<com.huskycode.jpaquery.types.db.Link>();
        for(Link link : links) {
            Table fromTable = tableFactory.createFromJPAEntity(link.getFrom().getEntityClass());
            Table toTable = tableFactory.createFromJPAEntity(link.getTo().getEntityClass());

            Column fromColumn = fromTable.column( JPAUtil.getColumnNameOrDefault(link.getFrom().getField()) );
            Column toColumn = toTable.column( JPAUtil.getColumnNameOrDefault(link.getTo().getField()) );

            result.add( new com.huskycode.jpaquery.types.db.Link(fromColumn, toColumn));
        }

        return result;
    }


}
