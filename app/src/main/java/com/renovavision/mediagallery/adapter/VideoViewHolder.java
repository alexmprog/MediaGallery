package com.renovavision.mediagallery.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.renovavision.mediagallery.R;
import com.renovavision.mediagallery.model.VideoItem;

import butterknife.Bind;
import butterknife.ButterKnife;

// photo gallery view holder
public class VideoViewHolder extends MediaViewHolder<VideoItem> {

    @Bind(R.id.main_photo)
    public ImageView mainPhoto;

    @Bind(R.id.selection_indicator)
    public ImageView selectionIndicator;

    @Bind(R.id.video_indicator)
    public ImageView videoIndicator;

    @Bind(R.id.duration_text)
    public TextView durationText;

    public VideoViewHolder(@NonNull View itemView, int gridItemSize) {
        super(itemView, gridItemSize);
        ButterKnife.bind(this, itemView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(this.gridItemSize,
                this.gridItemSize);
        mainPhoto.setLayoutParams(params);
    }

    @Override
    public void bindData(VideoItem mediaItem, @Nullable TypedArray selectionIcons) {
        int selectedPosition = mediaItem.selectedPosition;
        if (selectionIcons != null && selectedPosition >= 0 && selectedPosition < selectionIcons.length()) {
            selectionIndicator.setImageDrawable(selectionIcons.getDrawable(selectedPosition));
        }
        selectionIndicator.setVisibility(mediaItem.isSelected ? View.VISIBLE : View.GONE);
        durationText.setText(mediaItem.duration);
        Context context = mainPhoto.getContext();

        Key signature = new MediaStoreSignature(mediaItem.mimeType, mediaItem.dateModified,
                        mediaItem.orientation);

        Glide.with(context).fromMediaStore()
                .signature(signature)
                .load(mediaItem.uri)
                .centerCrop()
                .into(mainPhoto);
    }
}
