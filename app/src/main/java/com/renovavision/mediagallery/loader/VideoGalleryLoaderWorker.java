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
import com.renovavision.mediagallery.model.VideoItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

// loader for videos
public class VideoGalleryLoaderWorker extends GalleryLoaderWorker {

    private static final String[] VIDEO_PROJECTION =
            new String[]{
                    MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.DATE_TAKEN,
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.VideoColumns.DATE_MODIFIED,
                    MediaStore.Video.VideoColumns.MIME_TYPE,
                    MediaStore.Video.VideoColumns.DURATION,
                    "0 AS " + MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.VideoColumns.BUCKET_ID,

            };

    private static final String[] VIDEO_BUCKET_PROJECTION =
            new String[]{
                    MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.VideoColumns.BUCKET_ID,
            };

    public VideoGalleryLoaderWorker(@NonNull GalleryConfig galleryConfig, @NonNull GalleryFilter galleryFilter) {
        super(galleryConfig, galleryFilter);
    }

    @NonNull
    @Override
    public List<MediaItem> getMediaItemList(@NonNull Context context) {
        return query(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns.DATA, MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.MIME_TYPE, MediaStore.Video.VideoColumns.DURATION, MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, MediaStore.Video.VideoColumns.BUCKET_ID);
    }

    @NonNull
    @Override
    public List<GalleryFilter> getGalleryFilters(@NonNull Context context) {
        List<GalleryFilter> filters = new ArrayList<>();
        // add all photos filter
        filters.add(defaultFilter);
        filters.addAll(queryFilters(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_BUCKET_PROJECTION, MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, MediaStore.Video.VideoColumns.BUCKET_ID));
        Collections.sort(filters, FILTER_COMPARATOR);
        return filters;
    }

    @NonNull
    private List<MediaItem> query(@NonNull Context context, @NonNull Uri contentUri, @NonNull String[] projection, @NonNull String sortByCol,
                                  @NonNull String idCol, @NonNull String dateTakenCol, @Nullable String dataCol, @NonNull String dateModifiedCol, @NonNull String mimeTypeCol, @NonNull String durationCol,
                                  @NonNull String orientationCol, @NonNull String bucketNameCol, @NonNull String bucketIdCol) {
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
            final int durationColNum = cursor.getColumnIndexOrThrow(durationCol);
            final int bucketNameColNum = cursor.getColumnIndexOrThrow(bucketNameCol);
            final int bucketIdColNum = cursor.getColumnIndexOrThrow(bucketIdCol);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColNum);
                long dateTaken = cursor.getLong(dateTakenColNum);
                String data = cursor.getString(dateCol);
                String mimeType = cursor.getString(mimeTypeColNum);
                long dateModified = cursor.getLong(dateModifiedColNum);
                int orientation = cursor.getInt(orientationColNum);
                final long msec = cursor.getLong(durationColNum);
                final int seconds = (int) (msec / 1000) % 60;
                final int minutes = (int) ((msec / (1000 * 60)) % 60);

                String duration = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                String bucketName = cursor.getString(bucketNameColNum);
                String bucketId = cursor.getString(bucketIdColNum);

                if (!TextUtils.equals(this.galleryFilter.bucketId, GalleryFilter.ALL_VIDEOS_BUCKET_ID) &&
                        !TextUtils.equals(this.galleryFilter.bucketId, bucketId)) {
                    continue;
                }

                if (paths.contains(data)) {
                    // duplicated item
                    continue;
                }

                paths.add(data);

                items.add(new VideoItem(id, Uri.withAppendedPath(contentUri, Long.toString(id)), data,
                        mimeType, bucketId, bucketName, dateTaken, dateModified, orientation, duration));
            }
        } finally {
            cursor.close();
        }

        return items;

    }
}
