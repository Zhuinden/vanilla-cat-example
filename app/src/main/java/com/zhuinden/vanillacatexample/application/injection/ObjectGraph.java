package com.zhuinden.vanillacatexample.application.injection;

import android.annotation.SuppressLint;
import android.content.Context;

import com.zhuinden.vanillacatexample.data.database.CatTable;
import com.zhuinden.vanillacatexample.data.database.Tables;
import com.zhuinden.vanillacatexample.data.datasource.local.dao.CatDao;
import com.zhuinden.vanillacatexample.data.datasource.remote.service.CatService;
import com.zhuinden.vanillacatexample.data.mapper.CatMapper;
import com.zhuinden.vanillacatexample.data.repository.CatRepository;
import com.zhuinden.vanillacatexample.presentation.cat.CatPresenter;
import com.zhuinden.vanillacatexample.util.database.DatabaseManager;
import com.zhuinden.vanillacatexample.util.schedulers.BackgroundScheduler;
import com.zhuinden.vanillacatexample.util.schedulers.MainThreadScheduler;
import com.zhuinden.vanillacatexample.util.schedulers.Scheduler;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class ObjectGraph {
    @SuppressLint("StaticFieldLeak") // app context does not leak.
    private static ObjectGraph INSTANCE;

    public static ObjectGraph get() {
        return INSTANCE;
    }

    private volatile Context appContext;

    private volatile CatTable catTable;

    private volatile CatDao catDao;

    private volatile CatMapper catMapper;

    private volatile CatRepository catRepository;

    private volatile CatService catService;

    private volatile Retrofit retrofit;

    private volatile DatabaseManager databaseManager;

    private volatile Scheduler mainThreadScheduler;

    private volatile Scheduler backgroundThreadScheduler;

    public ObjectGraph(Context appContext) {
        INSTANCE = this;
        this.appContext = appContext;
        resolveGraph();
    }

    protected void resolveGraph() {
        this.mainThreadScheduler = new MainThreadScheduler();
        this.backgroundThreadScheduler = new BackgroundScheduler();
        this.retrofit = new Retrofit.Builder() //
                .baseUrl("http://thecatapi.com/") //
                .addConverterFactory(SimpleXmlConverterFactory.create()) //
                .build();
        this.catTable = Tables.CAT.getTable();
        this.databaseManager = new DatabaseManager(appContext, Tables.getTables());
        this.catMapper = new CatMapper();
        this.catService = retrofit.create(CatService.class);
        this.catDao = new CatDao(catTable, catMapper, databaseManager);
        this.catRepository = new CatRepository(catDao, catService, catMapper, backgroundThreadScheduler, mainThreadScheduler);
    }

    public Context appContext() {
        return appContext;
    }

    public CatTable catTable() {
        return catTable;
    }

    public CatDao catDao() {
        return catDao;
    }

    public CatRepository catRepository() {
        return catRepository;
    }

    public CatService catService() {
        return catService;
    }

    public CatMapper catMapper() {
        return catMapper;
    }

    public Retrofit retrofit() {
        return retrofit;
    }

    public DatabaseManager databaseManager() {
        return databaseManager;
    }

    public CatPresenter catPresenter() {
        return new CatPresenter(catRepository);
    }

    public Scheduler mainThreadScheduler() {
        return mainThreadScheduler;
    }

    public Scheduler backgroundThreadScheduler() {
        return backgroundThreadScheduler;
    }
}
