package com.zhuinden.vanillacatexample.util;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Owner on 2016.06.10.
 */
public class RecyclerViewPaginationScrollListener
        extends RecyclerView.OnScrollListener {
    public interface PaginationRequestListener {
        void onScrolledToBottom();
    }

    PaginationRequestListener paginationRequestListener;

    public RecyclerViewPaginationScrollListener(PaginationRequestListener paginationRequestListener) {
        this.paginationRequestListener = paginationRequestListener;
    }

    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if(!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop();
        } else if(!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom();
        } else if(dy < 0) {
            onScrolledUp();
        } else if(dy > 0) {
            onScrolledDown();
        }
    }

    public void onScrolledUp() {
    }

    public void onScrolledDown() {
    }

    public void onScrolledToTop() {
    }

    public void onScrolledToBottom() {
        paginationRequestListener.onScrolledToBottom();
    }
}
