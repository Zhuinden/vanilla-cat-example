package com.zhuinden.vanillacatexample.data.repository;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.zhuinden.vanillacatexample.data.datasource.local.dao.CatDao;
import com.zhuinden.vanillacatexample.data.datasource.mapper.CatMapper;
import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatBO;
import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatsBO;
import com.zhuinden.vanillacatexample.data.datasource.remote.service.CatService;
import com.zhuinden.vanillacatexample.domain.object.Cat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatRepository {
    public interface DataLoadedCallback {
        void dataLoaded(List<Cat> cats);
    }

    private CatDao catDao;
    private CatService catService;
    private CatMapper catMapper;

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    public CatRepository(CatDao catDao, CatService catService, CatMapper catMapper) {
        this.catDao = catDao;
        this.catService = catService;
        this.catMapper = catMapper;
    }

    public void getAllCats(DataLoadedCallback callback) {
        executor.execute(() -> {
            List<Cat> cats = catDao.findAll();
            handler.post(() -> callback.dataLoaded(cats));
        });
    }

    public void loadMoreCats(DataLoadedCallback callback) {
        executor.execute(() -> {
            List<Cat> cats = new ArrayList<>();
            try {
                CatsBO catsBO = catService.getCats().execute().body();
                if(catsBO == null) {
                    throw new IOException("Could not download cats");
                }
                for(CatBO catBO : catsBO.getCats()) {
                    cats.add(catMapper.from(catBO));
                }
                catDao.insert(cats);
            } catch(IOException e) {
                e.printStackTrace();
            }
            handler.post(() -> callback.dataLoaded(cats));
        });
    }
}
