package com.renovavision.mediagallery.utils.imageprocessing.preloader;

import android.support.v7.widget.RecyclerView;


public final class RecyclerViewPreloader<T> extends RecyclerView.OnScrollListener {

    private RecyclerToListViewScrollListener recyclerScrollListener;

    /**
     * Constructor that accepts interfaces for providing the dimensions of images to preload, the list
     * of models to preload for a given position, and the request to use to load images.
     *
     * @param preloadModelProvider     Provides models to load and requests capable of loading them.
     * @param preloadDimensionProvider Provides the dimensions of images to load.
     * @param maxPreload               Maximum number of items to preload.
     */
    public RecyclerViewPreloader(ListPreloader.PreloadModelProvider<T> preloadModelProvider,
                                 ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider, int maxPreload) {

        ListPreloader<T> listPreloader = new ListPreloader<T>(preloadModelProvider, preloadDimensionProvider, maxPreload);
        recyclerScrollListener = new RecyclerToListViewScrollListener(listPreloader);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        recyclerScrollListener.onScrolled(recyclerView, dx, dy);
    }
}
