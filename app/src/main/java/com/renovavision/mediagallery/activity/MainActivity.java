package com.renovavision.mediagallery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.renovavision.mediagallery.GalleryUtils;
import com.renovavision.mediagallery.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.photo_button,R.id.video_button})
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.photo_button:
                GalleryUtils.openPhotoGallery(this,8);
                break;
            case R.id.video_button:
                GalleryUtils.openVideoGallery(this,1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
