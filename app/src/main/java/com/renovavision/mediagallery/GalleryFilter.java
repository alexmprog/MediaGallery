package com.renovavision.mediagallery;

import android.support.annotation.NonNull;
import android.text.TextUtils;

// class for photo gallery filter
public class GalleryFilter {

    // default values for photo filter
    public static final String ALL_PHOTOS_BUCKET_ID = GalleryFilter.class.getSimpleName() + "ALL_PHOTOS_BUCKET_ID";

    // default values for photo filter
    public static final String ALL_VIDEOS_BUCKET_ID = GalleryFilter.class.getSimpleName() + "ALL_VIDEOS_BUCKET_ID";

    @NonNull
    public String bucketId;

    @NonNull
    public String bucketName;

    public GalleryFilter(@NonNull String bucketId, @NonNull String bucketName) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof GalleryFilter)) {
            return false;
        }

        GalleryFilter otherItem = (GalleryFilter) o;
        return TextUtils.equals(bucketId, otherItem.bucketId);
    }
}
