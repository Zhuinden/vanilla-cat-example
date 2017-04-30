package com.zhuinden.vanillacatexample.util.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseManager
        extends SQLiteOpenHelper {
    public interface Table {
        String getTableName();

        Fields[] getFields();

        void onCreate(SQLiteDatabase database);

        void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion);
    }

    public interface Fields {
        String getFieldName();

        String getFieldType();

        String getFieldAdditional();
    }

    public interface QueryDefinition {
        Cursor query(SQLiteDatabase database, Table table, String[] allFields);
    }

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    private final List<Table> tables;

    private SQLiteDatabase database;

    public DatabaseManager(Context appContext, List<Table> tables) {
        super(appContext, DATABASE_NAME, null, DATABASE_VERSION);
        this.tables = tables;
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        for(Table table : tables) {
            table.onCreate(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        for(Table table : tables) {
            table.onUpgrade(database, oldVersion, newVersion);
        }
    }

    public interface Transaction {
        void execute(SQLiteDatabase sqLiteDatabase);
    }

    public interface Mapper<T> {
        T from(Cursor cursor);
    }

    public void executeTransaction(Transaction transaction) {
        try {
            database.beginTransaction();
            transaction.execute(database);
            database.setTransactionSuccessful();
        } finally {
            if(database.inTransaction()) {
                database.endTransaction();
            }
        }
    }

    public <T> List<T> findAll(Table table, Mapper<T> mapper) {
        return findAll(table,
                mapper,
                (database, _table, allFields) -> database.query(_table.getTableName(), allFields, null, null, null, null, null));
    }

    public <T> List<T> findAll(Table table, Mapper<T> mapper, QueryDefinition queryDefinition) {
        String[] allFields = extractFieldsFromTable(table);
        Cursor cursor = queryDefinition.query(database, table, allFields);
        List<T> list = collectObjectFromCursor(mapper, cursor);
        cursor.close();
        return list;
    }

    private <T> List<T> collectObjectFromCursor(Mapper<T> mapper, Cursor cursor) {
        List<T> list = new LinkedList<>();
        if(cursor.moveToFirst()) {
            do {
                T object = mapper.from(cursor);
                list.add(object);
            } while(cursor.moveToNext());
        }
        return new ArrayList<>(list);
    }

    @NonNull
    private String[] extractFieldsFromTable(Table table) {
        Fields[] _fields = table.getFields();
        String[] fields = new String[_fields.length];
        int i = 0;
        for(Fields field : _fields) {
            fields[i++] = field.getFieldName();
        }
        return fields;
    }
}