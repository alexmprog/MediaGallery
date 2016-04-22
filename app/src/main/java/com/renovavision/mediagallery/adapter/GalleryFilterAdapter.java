package com.renovavision.mediagallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.renovavision.mediagallery.GalleryFilter;
import com.renovavision.mediagallery.R;

import java.util.List;

// base adapter for gallery filter
public class GalleryFilterAdapter extends BaseAdapter {

    private static final String DROP_DOWN_TAG = "DROP_DOWN";
    private static final String NON_DROP_DOWN_TAG = "NON_DROP_DOWN";

    @NonNull
    private Context context;

    @NonNull
    private List<GalleryFilter> galleryFilters;

    public GalleryFilterAdapter(@NonNull Context context, @NonNull List<GalleryFilter> galleryFilters) {
        this.context = context;
        this.galleryFilters = galleryFilters;
    }

    @Override
    public int getCount() {
        return galleryFilters.size();
    }

    @Override
    public Object getItem(int position) {
        return galleryFilters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !TextUtils.equals(view.getTag().toString(), DROP_DOWN_TAG)) {
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item_dropdown, parent, false);
            view.setTag(DROP_DOWN_TAG);
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !TextUtils.equals(view.getTag().toString(), NON_DROP_DOWN_TAG)) {
            view = LayoutInflater.from(context).inflate(R.layout.
                    spinner_item_actionbar, parent, false);
            view.setTag(NON_DROP_DOWN_TAG);
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < galleryFilters.size() ? galleryFilters.get(position).bucketName : "";
    }
}

