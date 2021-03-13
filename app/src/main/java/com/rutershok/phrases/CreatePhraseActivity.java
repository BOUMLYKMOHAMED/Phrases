package com.rutershok.phrases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ramotion.circlemenu.CircleMenuView;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.utils.Ad;
import com.rutershok.phrases.utils.Dialog;
import com.rutershok.phrases.utils.Key;
import com.rutershok.phrases.utils.Share;

public class CreatePhraseActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText mEditTextPhrase;
    private ImageView imageBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_phrase);
        Ad.showInterstitialWithFrequency(this);
        Ad.showBanner(this);

        setActionBar();
        setImage();

        setCircleMenu();

        setClickListener();
    }

    private void setActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(Key.TITLE));
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    private void setCircleMenu() {
        CircleMenuView circleMenu = findViewById(R.id.circle_menu);
        circleMenu.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int buttonIndex) {
                super.onButtonClickAnimationEnd(view, buttonIndex);
                switch (buttonIndex) {
                    case 0:
                        Dialog.modifyPhrase(CreatePhraseActivity.this, mEditTextPhrase);
                        break;
                    case 1:
                        Dialog.changeTextColor(CreatePhraseActivity.this, mEditTextPhrase);
                        break;
                    case 2:
                        Dialog.modifyFont(CreatePhraseActivity.this, mEditTextPhrase);
                        break;
                    case 3:
                        Dialog.changeBackground(CreatePhraseActivity.this, imageBackground);
                        break;
                    case 4:
                        Share.saveImage(CreatePhraseActivity.this);
                        break;
                }
            }
        });
    }

    private void setImage() {
        mEditTextPhrase = findViewById(R.id.text_phrase);
        mEditTextPhrase.setText(getIntent().getStringExtra(Key.PHRASE));

        imageBackground = findViewById(R.id.image_background);
    }

    private void setClickListener() {
        int[] itemsId = {
                R.id.image_background,
                R.id.image_logo,
                R.id.text_phrase,
                R.id.button_share_image,
                R.id.button_share_text,
        };

        for (int id : itemsId) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check if logo is removed with video
        if (Storage.getPremium(this)) {
            findViewById(R.id.image_logo).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        switch (view.getId()) {
            case R.id.button_share_text:
                if (mEditTextPhrase.getText() != null) {
                    Share.withText(context, mEditTextPhrase.getText().toString());
                }
                break;
            case R.id.button_share_image:
                Share.withImage(this);
                break;
            case R.id.image_background:
                Dialog.changeBackground(this, imageBackground);
                break;
            case R.id.text_phrase:
                Dialog.modifyPhrase(this, mEditTextPhrase);
                break;
            case R.id.image_logo:
                Dialog.premiumVersion(this);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Key.PICK_IMAGE) {
                Glide.with(this).load(data.getData()).apply(new RequestOptions().centerCrop()).into(imageBackground);
            } else if (requestCode == Key.CAMERA_REQUEST && resultCode == RESULT_OK) {
                Glide.with(this).load(data.getData()).apply(new RequestOptions().centerCrop()).into(imageBackground);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}