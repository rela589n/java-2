package com.company.database.generic;

import java.io.IOException;
import java.util.HashMap;

public interface DatabaseInterface extends DatabaseEntity {
    void create(DatabaseEntity entity);

    DatabaseEntity read(Integer id) throws EntityNotFoundException;

    HashMap<Integer, DatabaseEntity> readAll();

    void update(Integer id, DatabaseEntity setEntity) throws EntityNotFoundException;

    void delete(Integer id) throws EntityNotFoundException;

    Integer lastInsertId();

    boolean isEmpty();

    void save() throws IOException;
}
