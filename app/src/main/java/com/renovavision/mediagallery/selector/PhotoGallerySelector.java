package com.renovavision.mediagallery.selector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.model.PhotoItem;

import java.util.List;

public class PhotoGallerySelector extends GallerySelector<PhotoItem> {

    public PhotoGallerySelector(@NonNull Context context, @NonNull GalleryConfig galleryConfig,
                                @NonNull List<PhotoItem> selectedItems,
                                @Nullable UpdateObserver updateObserver) {
        super(galleryConfig, selectedItems, updateObserver);
        this.selectionIcons = context.getResources().obtainTypedArray(R.array.photo_selection_icons);
    }
}
