package com.zhuinden.vanillacatexample.util.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
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
        database.beginTransaction();
        transaction.execute(database);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public <T> List<T> findAll(Table table, Mapper<T> mapper) {
        List<T> list = new ArrayList<>();
        String[] fields = extractFieldsFromTable(table);
        Cursor cursor = database.query(table.getTableName(), fields, null, null, null, null, null);
        collectObjectFromCursor(mapper, list, cursor);
        cursor.close();
        return list;
    }

    private <T> void collectObjectFromCursor(Mapper<T> mapper, List<T> list, Cursor cursor) {
        if(cursor.moveToFirst()) {
            do {
                T object = mapper.from(cursor);
                list.add(object);
            } while(cursor.moveToNext());
        }
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