package com.renovavision.mediagallery.adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.renovavision.mediagallery.model.PhotoItem;

// photo gallery view holder
public class PhotoViewHolder extends ImageViewHolder<PhotoItem> {

    public PhotoViewHolder(@NonNull View itemView, int gridItemSize) {
        super(itemView, gridItemSize);
    }

    @Override
    public void bindData(@NonNull PhotoItem mediaItem, @Nullable TypedArray selectionIcons) {
        this.mediaItem = mediaItem;

        setSelected(mediaItem, selectionIcons);

        Context context = mainPhoto.getContext();

        // load gif and bitmap images
        Glide.with(context)
                .load(mediaItem.filePath)
                .centerCrop()
                .listener(this)
                .into(mainPhoto);
    }
}
