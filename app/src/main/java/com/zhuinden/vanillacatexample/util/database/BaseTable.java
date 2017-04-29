package com.zhuinden.vanillacatexample.util.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Owner on 2017. 04. 29..
 */

public abstract class BaseTable
        implements DatabaseManager.Table {
    @Override
    public void onCreate(SQLiteDatabase database) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ");
        stringBuilder.append(getTableName());
        stringBuilder.append("(");
        DatabaseManager.Fields[] fields = getFields();
        int size = fields.length;
        int i = 0;
        for(DatabaseManager.Fields field : fields) {
            stringBuilder.append(field.getFieldName());
            stringBuilder.append(" ");
            stringBuilder.append(field.getFieldType());
            stringBuilder.append(" ");
            if(field.getFieldAdditional() != null) {
                stringBuilder.append(field.getFieldAdditional());
            }
            if(i < size - 1) {
                stringBuilder.append(",");
            }
            i++;
        }
        stringBuilder.append(");");
        database.execSQL(stringBuilder.toString());
    }

    @Override // TODO: implement migration
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(getTableName(), "Upgrading from version " + oldVersion + " to " + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + getTableName());
        onCreate(database);
    }
}
