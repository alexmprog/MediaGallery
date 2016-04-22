package com.renovavision.mediagallery;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


// configuration for photo and video galleries
public class GalleryConfig implements Serializable {

    public static final String GALLERY_CONFIG_EXTRAS = "GalleryConfig";

    public static final int GRID_SIZE = 3;

    // gallery type selection - PHOTO or VIDEO
    public static final int PHOTO = 0;
    public static final int VIDEO = 1;

    @IntDef({PHOTO, VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private int type = PHOTO;

    private int maxSelectionCount = 0;

    @Type
    public int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public void setMaxSelectionCount(int mMaxSelectionCount) {
        this.maxSelectionCount = mMaxSelectionCount;
    }

    public int getMaxSelectionCount() {
        return maxSelectionCount;
    }
}
