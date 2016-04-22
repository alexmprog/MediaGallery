package com.renovavision.mediagallery.adapter;


import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.model.SelectableMediaItem;

import butterknife.Bind;
import butterknife.ButterKnife;

// photo gallery view holder
public abstract class ImageViewHolder<T extends SelectableMediaItem> extends MediaViewHolder<T>
        implements RequestListener<String, GlideDrawable> {

    @Bind(R.id.main_photo)
    public ImageView mainPhoto;

    @Bind(R.id.selection_indicator)
    public ImageView indicator;

    protected T mediaItem;

    @Override
    public abstract void bindData(@NonNull T mediaItem, @Nullable TypedArray selectionIcons);

    public ImageViewHolder(@NonNull View itemView, int gridItemSize) {
        super(itemView, gridItemSize);
        ButterKnife.bind(this, itemView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.gridItemSize,
                this.gridItemSize);
        mainPhoto.setLayoutParams(params);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                               boolean isFirstResource) {
        mediaItem.isValid = false;
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                   boolean isFromMemoryCache, boolean isFirstResource) {
        // all is good
        return false;
    }

    public void setSelected(@NonNull T mediaItem, @Nullable TypedArray selectionIcons) {
        if (selectionIcons == null) {
            return;
        }
        int selectedPosition = mediaItem.selectedPosition;
        if (selectedPosition >= 0 && selectedPosition < selectionIcons.length()) {
            indicator.setImageDrawable(selectionIcons.getDrawable(selectedPosition));
        }
        indicator.setVisibility(mediaItem.isSelected ? View.VISIBLE : View.GONE);
    }
}
