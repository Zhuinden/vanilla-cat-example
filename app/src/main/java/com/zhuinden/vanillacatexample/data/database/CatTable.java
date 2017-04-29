package com.zhuinden.vanillacatexample.data.database;

import com.zhuinden.vanillacatexample.util.database.BaseTable;
import com.zhuinden.vanillacatexample.util.database.DatabaseManager;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatTable
        extends BaseTable {
    public static final String NAME = "cat";

    public enum Fields
            implements DatabaseManager.Fields {
        ID("_id", "text", "primary key"),
        //"integer", "primary key autoincrement"),
        URL("url", "text", "not null"),
        SOURCE_URL("sourceUrl", "text", "not null");

        private String fieldName;
        private String fieldType;
        private String fieldAdditional;

        Fields(String fieldName, String fieldType, String fieldAdditional) {
            this.fieldName = fieldName;
            this.fieldType = fieldType;
            this.fieldAdditional = fieldAdditional;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public String getFieldAdditional() {
            return fieldAdditional;
        }
    }

    @Override
    public String getTableName() {
        return NAME;
    }

    @Override
    public DatabaseManager.Fields[] getFields() {
        return Fields.values();
    }
}
