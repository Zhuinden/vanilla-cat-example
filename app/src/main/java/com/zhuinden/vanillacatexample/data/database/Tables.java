package com.zhuinden.vanillacatexample.data.database;

import com.zhuinden.vanillacatexample.util.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 2017. 04. 29..
 */

public enum Tables {
    CAT(new CatTable());

    private DatabaseManager.Table table;

    Tables(DatabaseManager.Table table) {
        this.table = table;
    }

    public <T extends DatabaseManager.Table> T getTable() {
        //noinspection unchecked
        return (T) table;
    }

    public static List<DatabaseManager.Table> getTables() {
        Tables[] _tables = Tables.values();
        List<DatabaseManager.Table> tables = new ArrayList<>(_tables.length);
        for(Tables table : _tables) {
            tables.add(table.table);
        }
        return tables;
    }
}
