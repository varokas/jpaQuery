package com.huskycode.jpaquery.persister;

import com.huskycode.jpaquery.persister.types.Row;

/**
 * Persist a row into the database based on a specific implementation.
 *
 * @author Varokas
 */
public interface TablePersister {
    void save(Row row);
}
