package com.chaudhary.flickrapp;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);

        Intent intent = getIntent();
        Photos photos = (Photos) intent.getSerializableExtra(PHOTO_TRANSFER);
        if (photos!= null){
            TextView phototitle =  findViewById(R.id.photo_title);
//            phototitle.setText("Title :" +photos.getTitle());
            Resources resources = getResources();
            String text = resources.getString(R.string.photo_title_text,photos.getTitle());
            phototitle.setText(text);

            TextView phototags = findViewById(R.id.photo_tags);
            phototags.setText(resources.getString(R.string.photo_tags_text,photos.getTags()));
//            phototags.setText("Tags :" + photos.getTags());


            TextView photoauthor = findViewById(R.id.photo_author);
               photoauthor.setText("Author :" + photos.getAuthor());


            ImageView photoimage = findViewById(R.id.photo_image);

            Picasso.get().load(photos.getLink())
                    .error(R.drawable.baseline_image_black_48dp)
                    .placeholder(R.drawable.baseline_image_black_48dp)
                    .into(photoimage);



        }
    }

}
