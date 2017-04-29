package com.zhuinden.vanillacatexample.data.mapper;

import android.database.Cursor;

import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatBO;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.util.database.DatabaseManager;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatMapper
        implements DatabaseManager.Mapper<Cat> {
    public Cat from(CatBO catBO) {
        return Cat.create(catBO.getId(), catBO.getUrl(), catBO.getSourceUrl());
    }

    @Override
    public Cat from(Cursor cursor) {
        return Cat.create(cursor.getString(0), cursor.getString(1), cursor.getString(2));
    }
}
