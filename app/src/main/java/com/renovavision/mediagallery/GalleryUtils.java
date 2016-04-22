package com.renovavision.mediagallery;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.renovavision.mediagallery.activity.PhotoGalleryActivity;
import com.renovavision.mediagallery.activity.VideoGalleryActivity;

// helper for opening photo and video galleries
public class GalleryUtils {

    // should open gallery for edit mode
    public static void openPhotoGallery(@NonNull Context context, int maxSelectionCount) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        GalleryConfig config = new GalleryConfig();
        config.setType(GalleryConfig.PHOTO);
        config.setMaxSelectionCount(maxSelectionCount);
        intent.putExtra(GalleryConfig.GALLERY_CONFIG_EXTRAS, config);
        context.startActivity(intent);
    }

    // should open gallery for edit mode
    public static void openVideoGallery(@NonNull Context context, int maxSelectionCount) {
        Intent intent = new Intent(context, VideoGalleryActivity.class);
        GalleryConfig config = new GalleryConfig();
        config.setType(GalleryConfig.VIDEO);
        config.setMaxSelectionCount(maxSelectionCount);
        intent.putExtra(GalleryConfig.GALLERY_CONFIG_EXTRAS, config);
        context.startActivity(intent);
    }
}
