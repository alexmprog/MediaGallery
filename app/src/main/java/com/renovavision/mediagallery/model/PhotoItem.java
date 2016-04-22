package com.renovavision.mediagallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;

// photo gallery item
public class PhotoItem extends MediaDataStoreItem {

    public PhotoItem(long rowId, Uri uri, String filePath, String mimeType, @NonNull String bucketId, @NonNull String bucketName, long dateTaken, long dateModified,
                     int orientation) {
        super(rowId, uri, filePath, mimeType, bucketId, bucketName, dateTaken, dateModified, orientation);
    }

    protected PhotoItem(Parcel in) {
        super(in);
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
