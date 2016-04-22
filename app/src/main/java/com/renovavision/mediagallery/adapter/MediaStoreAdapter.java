package com.renovavision.mediagallery.adapter;

import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.model.MediaDataStoreItem;
import com.renovavision.mediagallery.model.MediaItem;
import com.renovavision.mediagallery.model.PhotoItem;
import com.renovavision.mediagallery.model.VideoItem;
import com.renovavision.mediagallery.utils.imageprocessing.preloader.ListPreloader;

import java.util.Collections;
import java.util.List;

// adapter for gallery fragment
public class MediaStoreAdapter extends GalleryAdapter implements ListPreloader.PreloadSizeProvider<MediaItem>,
        ListPreloader.PreloadModelProvider<MediaItem> {

    protected static final int PHOTO_ITEM = 1;
    protected static final int VIDEO_ITEM = 2;

    protected final BitmapTypeRequest<Uri> requestBuilder;

    protected int[] preloadedParams;

    public MediaStoreAdapter(@Nullable TypedArray selectionIcons, @NonNull List<MediaItem> mediaItems,
                             int gridItemSize,
                             @NonNull RequestManager requestManager) {
        super(selectionIcons, mediaItems, gridItemSize);
        this.preloadedParams = new int[]{this.gridItemSize, this.gridItemSize};

        requestBuilder = requestManager.fromMediaStore().asBitmap();
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case PHOTO_ITEM:
                itemView = View.inflate(parent.getContext(), R.layout.gallery_photo_item, null);
                return new PhotoViewHolder(itemView, gridItemSize);
            case VIDEO_ITEM:
                itemView = View.inflate(parent.getContext(), R.layout.gallery_video_item, null);
                return new VideoViewHolder(itemView, gridItemSize);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        MediaItem mediaItem = mediaItems.get(position);
        holder.bindData(mediaItem, selectionIcons);
    }

    @Override
    public int getItemViewType(int position) {
        MediaItem mediaItem = mediaItems.get(position);
        if (mediaItem instanceof PhotoItem) {
            return PHOTO_ITEM;
        } else if (mediaItem instanceof VideoItem) {
            return VIDEO_ITEM;
        }
        return 0;
    }

    @Override
    public List<MediaItem> getPreloadItems(int position) {
        return Collections.singletonList(mediaItems.get(position));
    }

    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(MediaItem item) {
        if (item instanceof MediaDataStoreItem) {
            MediaDataStoreItem mediaItem = (MediaDataStoreItem) item;
            MediaStoreSignature signature = new MediaStoreSignature(mediaItem.mimeType,
                    mediaItem.dateModified, mediaItem.orientation);
            return requestBuilder.clone()
                    .signature(signature)
                    .load(mediaItem.uri);
        }
        return requestBuilder.clone()
                .load(null);
    }

    @Override
    public int[] getPreloadSize(MediaItem item, int adapterPosition, int perItemPosition) {
        return preloadedParams;
    }

    public void setMediaItems(@NonNull List<MediaItem> mediaItems) {
        int mediaItemSize = this.mediaItems.size();
        MediaItem firstMediaItem = null;
        if (mediaItemSize > 0) {
            firstMediaItem = this.mediaItems.get(0);
        }
        this.mediaItems.clear();
        if (firstMediaItem != null) {
            this.mediaItems.add(firstMediaItem);
        }
        this.mediaItems.addAll(mediaItems);
        notifyDataSetChanged();
    }
}
