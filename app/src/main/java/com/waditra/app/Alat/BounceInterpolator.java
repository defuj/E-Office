package com.waditra.app.Alat;

/**
 * Created by defuj on 10/08/2016.
 */
public class BounceInterpolator implements android.view.animation.Interpolator {
    double mAmplitude = 3;
    double mFrequency = 5;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}