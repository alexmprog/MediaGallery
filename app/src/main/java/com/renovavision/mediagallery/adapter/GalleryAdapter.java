package com.renovavision.mediagallery.adapter;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.renovavision.mediagallery.model.MediaItem;
import com.renovavision.mediagallery.model.PhotoItem;
import com.renovavision.mediagallery.model.SelectableMediaItem;

import java.lang.ref.WeakReference;
import java.util.List;

// adapter for gallery fragment
public abstract class GalleryAdapter extends RecyclerView.Adapter<MediaViewHolder> {

    protected final int gridItemSize;

    @Nullable
    protected TypedArray selectionIcons;

    @NonNull
    protected List<MediaItem> mediaItems;

    protected int[] preloadedParams;

    protected WeakReference<RecyclerView> recyclerViewReference;

    public GalleryAdapter(@Nullable TypedArray selectionIcons, @NonNull List<MediaItem> mediaItems,
                          int gridItemSize) {
        this.mediaItems = mediaItems;
        this.gridItemSize = gridItemSize;
        this.selectionIcons = selectionIcons;
        this.preloadedParams = new int[]{this.gridItemSize, this.gridItemSize};
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerViewReference = new WeakReference<>(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerViewReference.clear();
    }

    public void updateMediaItems(@NonNull List<SelectableMediaItem> itemsList) {
        if (!itemsList.isEmpty()) {
            for (MediaItem mediaItem : itemsList) {
                int indexOf = mediaItems.indexOf(mediaItem);
                if (indexOf >= 0) {
                    PhotoViewHolder photoViewHolder = getViewHolderByPosition(indexOf);
                    if (photoViewHolder != null && mediaItem instanceof PhotoItem) {
                        PhotoItem photoItem = (PhotoItem) mediaItem;
                        photoViewHolder.setSelected(photoItem, selectionIcons);
                    } else {
                        notifyItemChanged(indexOf);
                    }
                }
            }
        }
    }

    public MediaItem getMediaItemByPosition(int position) {
        return mediaItems.get(position);
    }

    @Nullable
    protected PhotoViewHolder getViewHolderByPosition(int position) {
        RecyclerView recyclerView = recyclerViewReference.get();
        if (recyclerView != null) {
            for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                View childAt = recyclerView.getChildAt(i);
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(childAt);
                if (viewHolder instanceof PhotoViewHolder) {
                    PhotoViewHolder photoViewHolder = (PhotoViewHolder) viewHolder;
                    if (photoViewHolder.getAdapterPosition() == position) {
                        return photoViewHolder;
                    }
                }
            }
        }
        return null;
    }
}
