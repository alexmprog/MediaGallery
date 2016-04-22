package com.renovavision.mediagallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.renovavision.mediagallery.GalleryFilter;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.adapter.GalleryFilterAdapter;
import com.renovavision.mediagallery.fragment.GalleryFragment;
import com.renovavision.mediagallery.fragment.PhotoGalleryFragment;
import com.renovavision.mediagallery.loader.PhotoGalleryLoaderWorker;
import com.renovavision.mediagallery.model.PhotoItem;
import com.renovavision.mediagallery.selector.GallerySelector;
import com.renovavision.mediagallery.selector.PhotoGallerySelector;
import com.renovavision.mediagallery.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

// photo gallery
public class PhotoGalleryActivity extends GalleryActivity {

    // default filter for gallery
    private GalleryFilter defaultFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.addFragment(getSupportFragmentManager(), R.id.container,
                PhotoGalleryFragment.getInstance(), false);

        defaultFilter = new GalleryFilter(GalleryFilter.ALL_PHOTOS_BUCKET_ID,
                getString(R.string.all_photos));
        final List<GalleryFilter> galleryFilters = getGalleryLoaderWorker().getGalleryFilters(this);
        GalleryFilterAdapter spinnerAdapter = new GalleryFilterAdapter(this, galleryFilters);
        filterSpinner.setAdapter(spinnerAdapter);
        filterSpinner.setSelection(galleryFilters.indexOf(defaultFilter));
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                GalleryFilter galleryFilter = galleryFilters.get(position);
                getGalleryLoaderWorker().setGalleryFilter(galleryFilter);
                final String tag = PhotoGalleryFragment.class.getSimpleName();
                Fragment registeredFragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (registeredFragment instanceof GalleryFragment) {
                    ((GalleryFragment) registeredFragment).reloadData();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_gallery;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(null);
        titleView.setVisibility(View.GONE);
        itemsDataChanged();
    }

    @NonNull
    @Override
    public PhotoGalleryLoaderWorker getGalleryLoaderWorker() {
        if (galleryLoaderWorker == null) {
            galleryLoaderWorker = new PhotoGalleryLoaderWorker(galleryConfig, defaultFilter);
        }
        return (PhotoGalleryLoaderWorker) galleryLoaderWorker;
    }

    @NonNull
    @Override
    public GallerySelector getGallerySelector() {
        if (gallerySelector == null) {
            gallerySelector = new PhotoGallerySelector(this, galleryConfig,
                    new ArrayList<PhotoItem>(), this);
        }
        return gallerySelector;
    }

    @Override
    public void itemsDataChanged() {
        // do something
    }
}
