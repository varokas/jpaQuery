package com.huskycode.jpaquery.types.db;

import java.util.List;

/**
 * Created by varokas on 5/10/14.
 */
public interface Table {
    String getName();

    List<Column> getColumns();
}
