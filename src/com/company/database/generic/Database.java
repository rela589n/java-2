package com.company.database.generic;

import java.io.*;
import java.util.HashMap;

public class Database implements DatabaseInterface {
    private HashMap<Integer, DatabaseEntity> elements;
    private Integer lastId;
    private transient String dataStorage;

    public Database(String fileStorage) throws ClassNotFoundException, IOException {
        this.dataStorage = fileStorage;
        try {
            __copy__(receive());
        } catch (InvalidObjectException e) {
            throw e;
        } catch (FileNotFoundException ignored) {
            this.elements = new HashMap<>();
            this.lastId = 0;
        }
    }

    public void __copy__(@org.jetbrains.annotations.NotNull Database source) {
        this.elements = source.elements;
        this.lastId = source.lastId;
        this.dataStorage = (source.dataStorage != null) ? source.dataStorage : this.dataStorage;
    }

    @Override
    public void create(DatabaseEntity entity) {
        this.elements.put(newId(), entity);
    }

    @Override
    public DatabaseEntity read(Integer id) throws EntityNotFoundException {
        checkExistence(id);
        return elements.get(id);
    }

    @Override
    public HashMap<Integer, DatabaseEntity> readAll() {
        return this.elements;
    }

    @Override
    public void update(Integer id, DatabaseEntity setEntity) throws EntityNotFoundException {
        checkExistence(id);
        elements.replace(id, setEntity);
    }

    @Override
    public void delete(Integer id) throws EntityNotFoundException {
        checkExistence(id);
        elements.remove(id);
    }

    @Override
    public Integer lastInsertId() {
        return lastId;
    }

    @Override
    public void save() throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.dataStorage))) {
            stream.writeObject(this);
        }
    }

    private Database receive() throws IOException, ClassNotFoundException {
        Database ret = null;

        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(this.dataStorage))) {
            Object db = stream.readObject();

            if (!(db instanceof Database)) {
                throw new InvalidObjectException("Data source does not Database object");
            }

            ret = (Database) db;
        }
        return ret;
    }

    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    private Integer newId() {
        return ++lastId;
    }

    private void checkExistence(Integer id) throws EntityNotFoundException {
        if (!elements.containsKey(id)) {
            throw new EntityNotFoundException("Database does not contain entity with id " + id);
        }
    }
}
