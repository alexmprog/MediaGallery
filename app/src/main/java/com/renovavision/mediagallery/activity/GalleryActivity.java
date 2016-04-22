package com.renovavision.mediagallery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.GalleryManager;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.loader.GalleryLoaderWorker;
import com.renovavision.mediagallery.selector.GallerySelector;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class GalleryActivity extends AppCompatActivity implements GalleryManager,
        GallerySelector.UpdateObserver {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.button_back)
    ImageView backView;

    @Bind(R.id.title_text)
    TextView titleView;

    @Bind(R.id.filter_spinner)
    Spinner filterSpinner;

    protected GalleryConfig galleryConfig;
    protected GalleryLoaderWorker galleryLoaderWorker;
    protected GallerySelector gallerySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleryConfig = (GalleryConfig) getIntent().getSerializableExtra(GalleryConfig.GALLERY_CONFIG_EXTRAS);
        if (galleryConfig == null) {
            // can not open gallery without configuration
            finish();
        }

        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ButterKnife.bind(toolbar);

        prepareToolbar();

        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(null);

        if (titleView != null) {
            titleView.setText(title);
        }
    }

    @NonNull
    @Override
    public GalleryConfig getGalleryConfig() {
        return galleryConfig;
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    protected void prepareToolbar() {
        setSupportActionBar(toolbar);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected abstract int getLayoutId();

    @Override
    public void showMessage(@NonNull String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_SHORT).show();
    }
}
