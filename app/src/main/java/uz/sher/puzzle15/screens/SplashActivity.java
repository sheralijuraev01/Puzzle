package uz.sher.puzzle15.screens;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.imageview.ShapeableImageView;

import uz.sher.puzzle15.R;
import uz.sher.puzzle15.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private SharedPreferences destroyShared;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());






//            startAnimation();


            CountDownTimer countDownTimer=new CountDownTimer(1200,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(binding.getRoot().getContext(), MainActivity.class));
                    finish();
                }
            }.start();





    }

    private void startAnimation(){
        Animation animationZoomIn= AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.zoom_in_animation);
        binding.splashImageContainer.startAnimation(animationZoomIn);
        binding.splashText.startAnimation(animationZoomIn);
    }

    @Override
    public void onBackPressed() {

    }
}