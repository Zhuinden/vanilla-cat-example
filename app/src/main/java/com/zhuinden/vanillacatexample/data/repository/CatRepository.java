package com.zhuinden.vanillacatexample.data.repository;

import com.zhuinden.vanillacatexample.data.datasource.local.dao.CatDao;
import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatBO;
import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatsBO;
import com.zhuinden.vanillacatexample.data.datasource.remote.service.CatService;
import com.zhuinden.vanillacatexample.data.mapper.CatMapper;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.util.schedulers.Scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private Scheduler backgroundThread;
    private Scheduler mainThread;

    public CatRepository(CatDao catDao, CatService catService, CatMapper catMapper, Scheduler backgroundThread, Scheduler mainThread) {
        this.catDao = catDao;
        this.catService = catService;
        this.catMapper = catMapper;
        this.backgroundThread = backgroundThread;
        this.mainThread = mainThread;
    }

    public void getAllCats(DataLoadedCallback callback) {
        backgroundThread.executeOnThread(() -> {
            List<Cat> cats = catDao.findAll();
            mainThread.executeOnThread(() -> callback.dataLoaded(cats));
        });
    }

    public void loadMoreCats(DataLoadedCallback callback) {
        backgroundThread.executeOnThread(() -> {
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
            mainThread.executeOnThread(() -> callback.dataLoaded(cats));
        });
    }
}
