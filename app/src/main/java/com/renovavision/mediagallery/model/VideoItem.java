package com.renovavision.mediagallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

// gallery video item
public class VideoItem extends MediaDataStoreItem {

    @Nullable
    public String duration;

    public VideoItem(long rowId, @NonNull Uri uri, @NonNull String filePath, @NonNull String mimeType,
                     @NonNull String bucketId, @NonNull String bucketName, long dateTaken, long dateModified,
                     int orientation, @Nullable String duration) {
        super(rowId, uri, filePath, mimeType, bucketId, bucketName, dateTaken, dateModified, orientation);
        this.duration = duration;
    }

    protected VideoItem(Parcel in) {
        super(in);
        duration = in.readString();
    }

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(duration);
    }
}
