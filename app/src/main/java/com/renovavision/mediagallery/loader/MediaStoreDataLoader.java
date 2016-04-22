package com.renovavision.mediagallery.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;


import com.renovavision.mediagallery.GalleryManager;
import com.renovavision.mediagallery.model.MediaItem;
import com.renovavision.mediagallery.model.SelectableMediaItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

// base class for loader framework
public class MediaStoreDataLoader extends AsyncTaskLoader<List<MediaItem>> {

    @Nullable
    private List<MediaItem> cached;

    private boolean observerRegistered = false;

    private final ForceLoadContentObserver forceLoadContentObserver = new ForceLoadContentObserver();

    @NonNull
    private WeakReference<GalleryManager> galleryManager;

    public MediaStoreDataLoader(@NonNull Context context, @NonNull GalleryManager galleryManager) {
        super(context);
        this.galleryManager = new WeakReference<>(galleryManager);
    }

    @Override
    public void deliverResult(List<MediaItem> data) {
        if (!isReset() && isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (cached != null) {
            deliverResult(cached);
        }
        if (takeContentChanged() || cached == null) {
            forceLoad();
        }
        registerContentObserver();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        cached = null;
        unregisterContentObserver();
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        unregisterContentObserver();
    }

    @Override
    public List<MediaItem> loadInBackground() {
        GalleryManager galleryManager = this.galleryManager.get();
        if (galleryManager == null) {
            return new ArrayList<>();
        }

        GalleryLoaderWorker galleryLoaderWorker = galleryManager.getGalleryLoaderWorker();
        List<MediaItem> mediaItemList = galleryLoaderWorker.getMediaItemList(getContext());

        // update previous selectable state
        List<SelectableMediaItem> selectedMediaItemList = galleryManager.getGallerySelector().getSelectedMediaItemList();
        if (!selectedMediaItemList.isEmpty()) {
            for (SelectableMediaItem selectedMediaItem : selectedMediaItemList) {
                // need remove all selection items from new list - will insert later
                mediaItemList.remove(selectedMediaItem);
            }
            for (SelectableMediaItem selectedMediaItem : selectedMediaItemList) {
                // add only local items for selected list
                if (!selectedMediaItem.isLocal) {
                    continue;
                }
                mediaItemList.add(0, selectedMediaItem);
            }
        }

        return mediaItemList;
    }

    private void registerContentObserver() {
        if (!observerRegistered) {
            ContentResolver cr = getContext().getContentResolver();
            cr.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false,
                    forceLoadContentObserver);
            cr.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, false,
                    forceLoadContentObserver);

            observerRegistered = true;
        }
    }

    private void unregisterContentObserver() {
        if (observerRegistered) {
            observerRegistered = false;

            getContext().getContentResolver().unregisterContentObserver(forceLoadContentObserver);
        }
    }
}
