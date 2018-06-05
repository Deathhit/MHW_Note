package tw.com.deathhit.mhw_note.utils;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import tw.com.deathhit.mhw_note.core.BaseActivity;
import tw.com.deathhit.mhw_note.utility.widget.ZoomFrameLayout;

public final class ImageActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ZoomFrameLayout zoomFrameLayout = new ZoomFrameLayout(this);

        ImageView imageView = new ImageView(this);

        imageView.setBackgroundColor(Color.BLACK);

        imageView.setImageURI(null);

        zoomFrameLayout.addView(imageView);

        Glide.with(imageView).load(getIntent().getData()).into(imageView);

        setContentView(zoomFrameLayout);
    }
}
