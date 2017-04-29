package com.zhuinden.vanillacatexample.data.datasource.local.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.zhuinden.vanillacatexample.data.database.CatTable;
import com.zhuinden.vanillacatexample.data.datasource.mapper.CatMapper;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.util.database.DatabaseManager;

import java.util.List;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatDao {
    CatTable catTable;

    CatMapper catMapper;

    DatabaseManager databaseManager;

    public CatDao(CatTable catTable, CatMapper catMapper, DatabaseManager databaseManager) {
        this.catTable = catTable;
        this.catMapper = catMapper;
        this.databaseManager = databaseManager;
    }

    public List<Cat> findAll() {
        return databaseManager.findAll(catTable, catMapper);
    }

    public void insert(List<Cat> cats) {
        databaseManager.executeTransaction(sqLiteDatabase -> {
            for(Cat cat : cats) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(CatTable.Fields.ID.getFieldName(), cat.id());
                contentValues.put(CatTable.Fields.URL.getFieldName(), cat.url());
                contentValues.put(CatTable.Fields.SOURCE_URL.getFieldName(), cat.sourceUrl());
                sqLiteDatabase.insertWithOnConflict(catTable.getTableName(), null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            }
        });
    }
}
