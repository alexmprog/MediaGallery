package com.renovavision.mediagallery.model;

// basic interface for selectable media items
public abstract class SelectableMediaItem implements MediaItem {

    public int selectedPosition = -1;
    public boolean isSelected = false;

    // sometimes we can not some photos and videos - file not found exception issue
    public boolean isValid = true;

    public boolean isLocal = true;
}
