package com.renovavision.mediagallery.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

// basic class for media data store item
public abstract class MediaDataStoreItem extends SelectableMediaItem implements Parcelable {

    public long rowId = 0L;

    @NonNull
    public final Uri uri;

    @NonNull
    public String filePath;

    @NonNull
    public String bucketId;

    @NonNull
    public String bucketName;

    @NonNull
    public final String mimeType;
    public final long dateModified;
    public final int orientation;
    public final long dateTaken;

    public MediaDataStoreItem(long rowId, @NonNull Uri uri, @NonNull String filePath,
                              @NonNull String mimeType, @NonNull String bucketId,
                              @NonNull String bucketName, long dateTaken, long dateModified,
                              int orientation) {
        this.rowId = rowId;
        this.uri = uri;
        this.filePath = filePath;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.dateTaken = dateTaken;
        this.bucketId = bucketId;
        this.bucketName = bucketName;
    }

    protected MediaDataStoreItem(Parcel in) {
        rowId = in.readLong();
        uri = in.readParcelable(Uri.class.getClassLoader());
        filePath = in.readString();
        mimeType = in.readString();
        dateModified = in.readLong();
        orientation = in.readInt();
        dateTaken = in.readLong();
        selectedPosition = in.readInt();
        isSelected = in.readByte() != 0;
        bucketId = in.readString();
        bucketName = in.readString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MediaDataStoreItem)) {
            return false;
        }

        MediaDataStoreItem otherItem = (MediaDataStoreItem) o;
        return uri.compareTo(otherItem.uri) == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(rowId);
        dest.writeParcelable(uri, flags);
        dest.writeString(filePath);
        dest.writeString(mimeType);
        dest.writeLong(dateModified);
        dest.writeInt(orientation);
        dest.writeLong(dateTaken);
        dest.writeInt(selectedPosition);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(bucketId);
        dest.writeString(bucketName);
    }
}
