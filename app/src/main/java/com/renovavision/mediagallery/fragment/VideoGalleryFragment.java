package com.renovavision.mediagallery.fragment;

import com.renovavision.mediagallery.R;

public class VideoGalleryFragment extends GalleryFragment {

    public static VideoGalleryFragment getInstance() {
        return new VideoGalleryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_grid;
    }
}
