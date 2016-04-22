package com.renovavision.mediagallery;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.renovavision.mediagallery.loader.GalleryLoaderWorker;
import com.renovavision.mediagallery.selector.GallerySelector;

// basic interface for gallery
public interface GalleryManager {

    @NonNull
    GalleryLoaderWorker getGalleryLoaderWorker();

    @NonNull
    GallerySelector getGallerySelector();

    @NonNull
    GalleryConfig getGalleryConfig();

    void showMessage(@NonNull String message);

    void showMessage(@StringRes int resId);
}
