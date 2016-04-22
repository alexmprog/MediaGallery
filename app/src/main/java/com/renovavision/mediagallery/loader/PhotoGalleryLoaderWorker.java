package com.renovavision.mediagallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.GalleryFilter;
import com.renovavision.mediagallery.model.MediaItem;
import com.renovavision.mediagallery.model.PhotoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

// loader for photos
public class PhotoGalleryLoaderWorker extends GalleryLoaderWorker {

    private static final String[] IMAGE_PROJECTION =
            new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.BUCKET_ID,
            };

    private static final String[] IMAGE_BUCKET_PROJECTION =
            new String[]{
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.BUCKET_ID,
            };

    private static final Comparator<GalleryFilter> FILTER_COMPARATOR = new Comparator<GalleryFilter>() {
        @Override
        public int compare(GalleryFilter lhs, GalleryFilter rhs) {
            return lhs.bucketName.compareTo(rhs.bucketName);
        }
    };

    public PhotoGalleryLoaderWorker(@NonNull GalleryConfig galleryConfig, @NonNull GalleryFilter defaultFilter) {
        super(galleryConfig, defaultFilter);
    }

    @NonNull
    @Override
    public List<MediaItem> getMediaItemList(@NonNull Context context) {
        return query(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DATE_MODIFIED, MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.ORIENTATION, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_ID);
    }

    @NonNull
    private List<MediaItem> query(@NonNull Context context, @NonNull Uri contentUri, @NonNull String[] projection, @Nullable String sortByCol,
                                  @Nullable String idCol, @Nullable String dateTakenCol, @Nullable String dataCol, @Nullable String dateModifiedCol, @Nullable String mimeTypeCol,
                                  @Nullable String orientationCol, @NonNull String bucketNameCol, @NonNull String bucketIdCol) {
        // create items
        List<MediaItem> items = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(contentUri, projection, null, null, sortByCol + " DESC");

        if (cursor == null) {
            return items;
        }

        HashSet<String> paths = new HashSet<>();

        try {
            final int idColNum = cursor.getColumnIndexOrThrow(idCol);
            final int dateTakenColNum = cursor.getColumnIndexOrThrow(dateTakenCol);
            final int dateCol = cursor.getColumnIndexOrThrow(dataCol);
            final int dateModifiedColNum = cursor.getColumnIndexOrThrow(dateModifiedCol);
            final int mimeTypeColNum = cursor.getColumnIndex(mimeTypeCol);
            final int orientationColNum = cursor.getColumnIndexOrThrow(orientationCol);
            final int bucketNameColNum = cursor.getColumnIndexOrThrow(bucketNameCol);
            final int bucketIdColNum = cursor.getColumnIndexOrThrow(bucketIdCol);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColNum);
                long dateTaken = cursor.getLong(dateTakenColNum);
                String data = cursor.getString(dateCol);
                String mimeType = cursor.getString(mimeTypeColNum);
                long dateModified = cursor.getLong(dateModifiedColNum);
                int orientation = cursor.getInt(orientationColNum);
                String bucketName = cursor.getString(bucketNameColNum);
                String bucketId = cursor.getString(bucketIdColNum);

                if (!TextUtils.equals(this.galleryFilter.bucketId, GalleryFilter.ALL_PHOTOS_BUCKET_ID) &&
                        !TextUtils.equals(this.galleryFilter.bucketId, bucketId)) {
                    continue;
                }

                try {
                    File file = new File(data);
                    if (!file.exists()) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                if (paths.contains(data)) {
                    // duplicated item
                    continue;
                }

                paths.add(data);

                items.add(new PhotoItem(id, Uri.withAppendedPath(contentUri, Long.toString(id)), data,
                        mimeType, bucketId, bucketName, dateTaken, dateModified, orientation));
            }
        } finally {
            cursor.close();
        }

        return items;

    }

    @NonNull
    public List<GalleryFilter> getGalleryFilters(@NonNull Context context) {
        List<GalleryFilter> filters = new ArrayList<>();
        // add all photos filter
        filters.add(defaultFilter);
        filters.addAll(queryFilters(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_BUCKET_PROJECTION, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.BUCKET_ID));
        Collections.sort(filters, FILTER_COMPARATOR);
        return filters;
    }
}
