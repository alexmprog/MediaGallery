package com.renovavision.mediagallery.adapter;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.renovavision.mediagallery.model.MediaItem;

// basic class for all gallery view holders
public abstract class MediaViewHolder<T extends MediaItem> extends RecyclerView.ViewHolder {

    protected int gridItemSize;

    public MediaViewHolder(@NonNull View itemView, int gridItemSize) {
        super(itemView);
        this.gridItemSize = gridItemSize;
    }

    public abstract void bindData(T mediaItem, @Nullable TypedArray selectionIcons);
}
