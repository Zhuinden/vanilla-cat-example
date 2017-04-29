package com.zhuinden.vanillacatexample.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhuinden.vanillacatexample.R;
import com.zhuinden.vanillacatexample.application.injection.ObjectGraph;
import com.zhuinden.vanillacatexample.data.repository.CatRepository;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.presentation.cat.CatAdapter;
import com.zhuinden.vanillacatexample.util.RecyclerViewPaginationScrollListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CatRepository catRepository;

    private CatAdapter catAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = item -> {
        switch(item.getItemId()) {
            case R.id.navigation_home:
                setTitle(R.string.title_home);
                return true;
            case R.id.navigation_dashboard:
                setTitle(R.string.title_dashboard);
                return true;
            case R.id.navigation_notifications:
                setTitle(R.string.title_notifications);
                return true;
        }
        return false;
    };

    private List<Cat> cats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catRepository = ObjectGraph.get().catRepository();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        catAdapter = new CatAdapter();
        recyclerView.setAdapter(catAdapter);
        recyclerView.addOnScrollListener(new RecyclerViewPaginationScrollListener(() -> {
            if(areAnyCatsLoaded()) {
                loadMoreCats();
            }
        }));
    }

    private boolean areAnyCatsLoaded() {
        return catAdapter.getItemCount() > 0;
    }

    private void loadMoreCats() {
        catRepository.loadMoreCats(new CatLoadedCallback(MainActivity.this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(cats == null) { // activity starting for the first time
            catRepository.getAllCats(new CatLoadedCallback(this));
        }
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
                catAdapter.updateData(cats);
            }
        } else {
            catAdapter.appendData(cats);
        }
    }

    public static MainActivity get(Context context) {
        // noinspection ResourceType
        return (MainActivity) context.getSystemService(TAG);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if(TAG.equals(name)) {
            return this;
        }
        return super.getSystemService(name);
    }

    private static class CatLoadedCallback
            implements CatRepository.DataLoadedCallback {
        private WeakReference<MainActivity> reference;

        public CatLoadedCallback(MainActivity mainActivity) {
            this.reference = new WeakReference<>(mainActivity);
        }

        @Override
        public void dataLoaded(List<Cat> cats) {
            if(reference.get() == null) {
                return;
            }
            MainActivity mainActivity = reference.get();
            mainActivity.handleLoadedData(cats);
        }
    }
}
