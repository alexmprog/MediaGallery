package com.renovavision.mediagallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.GalleryFilter;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.adapter.GalleryFilterAdapter;
import com.renovavision.mediagallery.fragment.GalleryFragment;
import com.renovavision.mediagallery.fragment.VideoGalleryFragment;
import com.renovavision.mediagallery.loader.VideoGalleryLoaderWorker;
import com.renovavision.mediagallery.model.VideoItem;
import com.renovavision.mediagallery.selector.GallerySelector;
import com.renovavision.mediagallery.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

// video gallery
public class VideoGalleryActivity extends GalleryActivity {

    // default filter for gallery
    private GalleryFilter defaultFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.addFragment(getSupportFragmentManager(), R.id.container,
                VideoGalleryFragment.getInstance(), false);

        defaultFilter = new GalleryFilter(GalleryFilter.ALL_VIDEOS_BUCKET_ID, getString(R.string.all_videos));
        final List<GalleryFilter> galleryFilters = getGalleryLoaderWorker().getGalleryFilters(this);
        GalleryFilterAdapter spinnerAdapter = new GalleryFilterAdapter(this, galleryFilters);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setSelection(galleryFilters.indexOf(defaultFilter));
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                GalleryFilter galleryFilter = galleryFilters.get(position);
                getGalleryLoaderWorker().setGalleryFilter(galleryFilter);
                final String tag = VideoGalleryFragment.class.getSimpleName();
                final Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment instanceof GalleryFragment) {
                    ((GalleryFragment) fragment).reloadData();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(null);
        titleView.setVisibility(View.GONE);
        itemsDataChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_gallery;
    }

    @NonNull
    @Override
    public VideoGalleryLoaderWorker getGalleryLoaderWorker() {
        if (galleryLoaderWorker == null) {
            galleryLoaderWorker = new VideoGalleryLoaderWorker(galleryConfig, defaultFilter);
        }
        return (VideoGalleryLoaderWorker) galleryLoaderWorker;
    }

    @NonNull
    @Override
    public GallerySelector getGallerySelector() {
        if (gallerySelector == null) {
            gallerySelector = new GallerySelector(galleryConfig,
                    new ArrayList<>(), this);
        }
        return gallerySelector;
    }

    @NonNull
    @Override
    public GalleryConfig getGalleryConfig() {
        return galleryConfig;
    }

    @Override
    public void itemsDataChanged() {
        // do something
    }

}
