package tw.com.deathhit.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tw.com.deathhit.core.BaseActivity;
import tw.com.deathhit.utility.widget.ZoomFrameLayout;

public final class ImageActivity extends BaseActivity {
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        ZoomFrameLayout zoomFrameLayout = new ZoomFrameLayout(this);

        ImageView imageView = new ImageView(this);

        imageView.setBackgroundColor(Color.BLACK);

        imageView.setImageURI(null);

        zoomFrameLayout.addView(imageView);

        Glide.with(imageView).load(getIntent().getData()).into(imageView);

        setContentView(zoomFrameLayout);
    }

    @Override
    protected void onBindViewOnce() {

    }

    @Override
    protected Object request(int requestType, @Nullable Object... args) {
        return null;
    }
}
