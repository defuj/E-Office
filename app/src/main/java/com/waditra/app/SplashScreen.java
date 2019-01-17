package com.waditra.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.waditra.app.Alat.BounceInterpolator;

public class SplashScreen extends AppCompatActivity {
	
	private static int splashInterval = 3000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
		/*this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); */

        setContentView(R.layout.splashscreen);
        startAnim();
		/*new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
                this.finish();

            }

            private void finish() {

            }
        }, splashInterval); */
    }

    public void startAnim() {
        final Button img = (Button)findViewById(R.id.logo_app);
        final Animation myAdmin = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.anim_2);

        BounceInterpolator interpolator = new BounceInterpolator(0.01, 4);
        myAdmin.setInterpolator(interpolator);

        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                img.startAnimation(myAdmin);
            }
        }.start();

        waitProses();
    }

    public void waitProses() {
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);

                SplashScreen.this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        }.start();
    }
}
