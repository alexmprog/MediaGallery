package com.renovavision.mediagallery.fragment;

import com.renovavision.mediagallery.R;

public class PhotoGalleryFragment extends GalleryFragment {

    public static PhotoGalleryFragment getInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_grid;
    }
}
