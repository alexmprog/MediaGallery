package com.renovavision.mediagallery.selector;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.renovavision.mediagallery.GalleryConfig;
import com.renovavision.mediagallery.model.SelectableMediaItem;

import java.util.ArrayList;
import java.util.List;

// basic class for gallery selector
public class GallerySelector<T extends SelectableMediaItem> {

    public interface UpdateListener {

        void itemsDataUpdated(@NonNull List<SelectableMediaItem> itemsList);

        void maxCountSelected();
    }

    public interface UpdateObserver {

        void itemsDataChanged();
    }

    @NonNull
    protected GalleryConfig galleryConfig;

    @NonNull
    protected List<T> selectedItems;

    @NonNull
    protected TypedArray selectionIcons;

    @Nullable
    protected UpdateObserver updateObserver;

    public GallerySelector(@NonNull GalleryConfig galleryConfig, @NonNull List<T> selectedItems,
                           @Nullable UpdateObserver updateObserver) {
        this.galleryConfig = galleryConfig;
        this.selectedItems = selectedItems;
        this.updateObserver = updateObserver;
    }

    public int getMaxCount() {
        return galleryConfig.getMaxSelectionCount();
    }

    public void processMediaItem(@NonNull T mediaItem, @NonNull UpdateListener listener) {
        List<SelectableMediaItem> notifiedList = new ArrayList<>();
        int selectedPosition = mediaItem.selectedPosition;
        if (selectedPosition < 0) {
            if (selectedItems.size() < getMaxCount()) {
                mediaItem.selectedPosition = selectedItems.size();
                mediaItem.isSelected = true;
                selectedItems.add(mediaItem);
                notifiedList.addAll(selectedItems);
            } else {
                listener.maxCountSelected();
                return;
            }
        } else {
            int index = selectedItems.indexOf(mediaItem);
            notifiedList.addAll(selectedItems);
            if (index >= 0) {
                int currentSelection = mediaItem.selectedPosition;
                mediaItem.selectedPosition = -1;
                mediaItem.isSelected = false;
                selectedItems.remove(index);
                for (T item : selectedItems) {
                    int position = item.selectedPosition;
                    if (position > currentSelection) {
                        item.selectedPosition = position - 1;
                    }
                }
            }
        }

        if (updateObserver != null) {
            updateObserver.itemsDataChanged();
        }
        listener.itemsDataUpdated(notifiedList);
    }

    @NonNull
    public List<T> getSelectedMediaItemList() {
        return selectedItems;
    }

    @NonNull
    public TypedArray getSelectionIconsList() {
        return selectionIcons;
    }
}
