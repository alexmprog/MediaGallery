package com.renovavision.mediagallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.GalleryFilter;
import com.renovavision.mediagallery.model.MediaItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// basic class for gallery loader
public abstract class GalleryLoaderWorker {

    protected static final Comparator<GalleryFilter> FILTER_COMPARATOR = new Comparator<GalleryFilter>() {
        @Override
        public int compare(GalleryFilter lhs, GalleryFilter rhs) {
            return lhs.bucketName.compareTo(rhs.bucketName);
        }
    };

    @NonNull
    protected GalleryConfig galleryConfig;

    protected GalleryFilter defaultFilter;
    protected GalleryFilter galleryFilter;

    public GalleryLoaderWorker(@NonNull GalleryConfig galleryConfig, @NonNull GalleryFilter defaultFilter) {
        this.galleryConfig = galleryConfig;
        this.defaultFilter = defaultFilter;
        this.galleryFilter = defaultFilter;
    }

    public void setGalleryFilter(@NonNull GalleryFilter galleryFilter) {
        this.galleryFilter = galleryFilter;
    }

    @NonNull
    protected List<GalleryFilter> queryFilters(@NonNull Context context, @NonNull Uri contentUri, @NonNull String[] projection,
                                               @Nullable String sortByCol, @NonNull String bucketNameCol, @NonNull String bucketIdCol) {
        HashMap<String, GalleryFilter> filters = new HashMap<>();
        Cursor cursor = context.getContentResolver()
                .query(contentUri, projection, null, null, sortByCol + " ASC");

        if (cursor == null) {
            return new ArrayList<>();
        }

        try {
            final int bucketNameColNum = cursor.getColumnIndexOrThrow(bucketNameCol);
            final int bucketIdColNum = cursor.getColumnIndexOrThrow(bucketIdCol);

            while (cursor.moveToNext()) {
                String bucketName = cursor.getString(bucketNameColNum);
                String bucketId = cursor.getString(bucketIdColNum);

                if (!Character.isUpperCase(bucketName.charAt(0))) {
                    // custom ordering should skip folder, if name is not starting with Upper case
                    continue;
                }

                if (!filters.containsKey(bucketId)) {
                    filters.put(bucketId, new GalleryFilter(bucketId, bucketName));
                }
            }
        } finally {
            cursor.close();
        }

        return new ArrayList<>(filters.values());
    }

    @NonNull
    public abstract List<MediaItem> getMediaItemList(@NonNull Context context);

    @NonNull
    public abstract List<GalleryFilter> getGalleryFilters(@NonNull Context context);
}
