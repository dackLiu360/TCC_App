package com.br.tcc.controllers;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.tcc.R;

public class Test extends AppCompatActivity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final TextView buttonGoToRegister = (TextView) findViewById(R.id.buttonGoToRegister);

        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("toggle_switch.json");
    }
    public void onClick(View v) {
        startCheckAnimation();
    }



    private void startCheckAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(animationView.getProgress(), 0.4f).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator animator2 = ValueAnimator.ofFloat(animationView.getProgress(), 0f).setDuration(500);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (animationView.getProgress() == 0f) {
            System.out.println("aqui1");
            animator.start();
        } else {
            System.out.println("aqui2");
            animator2.start();
        }
    }
}
