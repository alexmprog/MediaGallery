package com.renovavision.mediagallery.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.GalleryManager;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.adapter.MediaStoreAdapter;
import com.renovavision.mediagallery.loader.MediaStoreDataLoader;
import com.renovavision.mediagallery.model.MediaItem;
import com.renovavision.mediagallery.model.SelectableMediaItem;
import com.renovavision.mediagallery.selector.GallerySelector;
import com.renovavision.mediagallery.utils.imageprocessing.preloader.RecyclerViewPreloader;
import com.renovavision.mediagallery.utils.recycler.RectangleItemDecoration;
import com.renovavision.mediagallery.utils.recycler.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class GalleryFragment extends Fragment implements GallerySelector.UpdateListener,
        LoaderManager.LoaderCallbacks<List<MediaItem>> {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    protected MediaStoreAdapter mediaStoreAdapter;

    protected GalleryManager galleryManager;

    protected RecyclerViewPreloader<MediaItem> preLoader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View parent = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, parent);
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();

        int paddingSize = context.getResources().getDimensionPixelSize(R.dimen.fragment_gallery_item_space);
        recyclerView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        recyclerView.addItemDecoration(new RectangleItemDecoration(paddingSize));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // calculate size for item
        int imageSize = calculateGridItemSize(context);

        // The number of Columns
        recyclerView.setLayoutManager(new GridLayoutManager(context, GalleryConfig.GRID_SIZE));

        // Set value for
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                MediaItem mediaItem = mediaStoreAdapter.getMediaItemByPosition(position);
                if (mediaItem instanceof SelectableMediaItem) {
                    boolean isValid = ((SelectableMediaItem) mediaItem).isValid;
                    if (!isValid) {
                        galleryManager.showMessage(R.string.can_not_select_this_image);
                        return;
                    }

                    GallerySelector gallerySelector = galleryManager.getGallerySelector();
                    gallerySelector.processMediaItem((SelectableMediaItem) mediaItem, GalleryFragment.this);
                }
            }
        }));

        if (galleryManager == null) {
            FragmentActivity activity = getActivity();
            if (!(activity instanceof GalleryManager)) {
                // invalid situation - need throw exception
                throw new IllegalStateException("Can not start Gallery Fragment without GalleryManager");
            }
            galleryManager = (GalleryManager) activity;
        }

        // create items
        List<MediaItem> items = new ArrayList<>();

        RequestManager requestManager = Glide.with(this);
        GallerySelector gallerySelector = galleryManager.getGallerySelector();

        mediaStoreAdapter = new MediaStoreAdapter(gallerySelector.getSelectionIconsList(), items,
                imageSize, requestManager);
        preLoader = new RecyclerViewPreloader<>(mediaStoreAdapter, mediaStoreAdapter,
                GalleryConfig.GRID_SIZE);
        recyclerView.setAdapter(mediaStoreAdapter);

        getLoaderManager().initLoader(R.id.loader_id_media_store_data, null, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && galleryManager != null) {
            GallerySelector gallerySelector = galleryManager.getGallerySelector();
        }
    }

    @Override
    public void itemsDataUpdated(@NonNull List<SelectableMediaItem> itemsList) {
        if (mediaStoreAdapter != null) {
            mediaStoreAdapter.updateMediaItems(itemsList);
        }
    }

    @Override
    public void maxCountSelected() {
        galleryManager.showMessage(R.string.can_not_select_more_items);
    }

    @Override
    public void onResume() {
        super.onResume();
        final FragmentActivity context = getActivity();
        GallerySelector gallerySelector = galleryManager.getGallerySelector();
        if (!gallerySelector.getSelectedMediaItemList().isEmpty()) {
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected int calculateGridItemSize(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point p = new Point();
        defaultDisplay.getSize(p);
        int itemMargin = getResources().getDimensionPixelSize(R.dimen.fragment_gallery_item_space);
        return (p.x - 4 * itemMargin) / GalleryConfig.GRID_SIZE;
    }

    @Override
    public Loader<List<MediaItem>> onCreateLoader(int id, Bundle args) {
        return new MediaStoreDataLoader(getActivity(), galleryManager);
    }

    @Override
    public void onLoadFinished(Loader<List<MediaItem>> loader, List<MediaItem> data) {
        mediaStoreAdapter.setMediaItems(data);
        recyclerView.addOnScrollListener(preLoader);
    }

    @Override
    public void onLoaderReset(Loader<List<MediaItem>> loader) {
        // do nothing
    }

    public void reloadData() {
        getLoaderManager().restartLoader(R.id.loader_id_media_store_data, null, this);
    }

    protected abstract int getLayoutId();

}
