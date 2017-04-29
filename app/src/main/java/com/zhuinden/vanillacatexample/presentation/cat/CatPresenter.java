package com.zhuinden.vanillacatexample.presentation.cat;

import com.zhuinden.vanillacatexample.data.repository.CatRepository;
import com.zhuinden.vanillacatexample.domain.object.Cat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatPresenter {
    public interface ViewContract {
        void updateData(List<Cat> cats);

        void appendData(List<Cat> cats);
    }

    private CatRepository catRepository;

    private List<Cat> cats;

    private ViewContract catView;

    public CatPresenter(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public void attachView(ViewContract view) {
        this.catView = view;
    }

    public void detachView() {
        this.catView = null;
    }

    public void scrolledToBottom() {
        if(areAnyCatsLoaded()) {
            loadMoreCats();
        }
    }


    private boolean areAnyCatsLoaded() {
        return cats != null && cats.size() > 0;
    }

    private void loadMoreCats() {
        catRepository.loadMoreCats(new CatPresenter.CatLoadedCallback(CatPresenter.this));
    }

    private void handleLoadedData(List<Cat> cats) {
        if(this.cats == null) {
            this.cats = new ArrayList<>(cats);
        } else {
            this.cats.addAll(cats);
        }
        if(!areAnyCatsLoaded()) {
            if(cats.isEmpty()) { // database was empty, force network download
                loadMoreCats();
            } else {
                if(catView != null) {
                    catView.updateData(cats);
                }
            }
        } else {
            if(catView != null) {
                catView.appendData(cats);
            }
        }
    }

    public void onStart() {
        if(cats == null) { // activity starting for the first time
            catRepository.getAllCats(new CatLoadedCallback(this));
        }
    }

    private static class CatLoadedCallback
            implements CatRepository.DataLoadedCallback {
        private WeakReference<CatPresenter> reference;

        public CatLoadedCallback(CatPresenter catPresenter) {
            this.reference = new WeakReference<>(catPresenter);
        }

        @Override
        public void dataLoaded(List<Cat> cats) {
            if(reference.get() == null) {
                return;
            }
            CatPresenter catPresenter = reference.get();
            catPresenter.handleLoadedData(cats);
        }
    }
}
