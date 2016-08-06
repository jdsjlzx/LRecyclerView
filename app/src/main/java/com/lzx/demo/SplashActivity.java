package com.lzx.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import static com.lzx.demo.R.id.textView;

public class SplashActivity extends AppCompatActivity implements Runnable {

    FloatingActionButton fab;
    TextView nameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        nameTextView = (TextView) findViewById(R.id.textView);

        fab.postDelayed(this, 200);
    }

    @Override
    public void run() {
        final View parentView = (View) fab.getParent();
        float scale = (float) (Math.sqrt(parentView.getHeight() * parentView.getHeight() + parentView.getWidth() * parentView.getWidth()) / fab.getHeight());
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", scale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", scale);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(fab, scaleX, scaleY).setDuration(1800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parentView.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.colorPrimary));
                fab.setVisibility(View.GONE);
                nameTextView.setVisibility(View.VISIBLE);
            }
        });
        PropertyValuesHolder holderA = PropertyValuesHolder.ofFloat("alpha", 0, 1);
        PropertyValuesHolder holderYm = PropertyValuesHolder.ofFloat("translationY", 0, 300);
        ObjectAnimator textAnimator = ObjectAnimator.ofPropertyValuesHolder(textView, holderA, holderYm).setDuration(1000);
        textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        textAnimator.setStartDelay(800);

        textAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        objectAnimator.start();
        textAnimator.start();
    }
}
