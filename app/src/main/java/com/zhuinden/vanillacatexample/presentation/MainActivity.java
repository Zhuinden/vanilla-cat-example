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
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.presentation.cat.CatAdapter;
import com.zhuinden.vanillacatexample.presentation.cat.CatPresenter;
import com.zhuinden.vanillacatexample.util.RecyclerViewPaginationScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements CatPresenter.ViewContract {
    private static final String TAG = "MainActivity";

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

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

    private CatAdapter catAdapter;

    private CatPresenter catPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catPresenter = ObjectGraph.get().catPresenter();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        catAdapter = new CatAdapter();
        recyclerView.setAdapter(catAdapter);
        catPresenter.attachView(this);
        recyclerView.addOnScrollListener(new RecyclerViewPaginationScrollListener(() -> {
            catPresenter.scrolledToBottom();
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        catPresenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catPresenter.detachView();
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

    @Override
    public void updateData(List<Cat> cats) {
        catAdapter.updateData(cats);
    }

    @Override
    public void appendData(List<Cat> cats) {
        catAdapter.appendData(cats);
    }
}
