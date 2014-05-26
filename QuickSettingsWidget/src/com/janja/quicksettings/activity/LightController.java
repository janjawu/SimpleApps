package com.janja.quicksettings.activity;

import com.janja.quicksettings.widget.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;

public class LightController extends Activity implements Runnable {

    private static final int BRIGHTNESS_ON = 255;
    private static final int DEFAULT_BACKLIGHT = (int) (BRIGHTNESS_ON * 0.4f);

    private float light = -1.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int value = getBrightness(this);
        setBacklights((float) value / 255f);

        new Thread(this).start();
    }

    private void setBacklights(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        light = lp.screenBrightness;
        lp.screenBrightness = brightness;
        getWindow().setAttributes(lp);
    }

    private static int getBrightness(Context context) {
        try {
            int brightness = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, DEFAULT_BACKLIGHT);
            return brightness;
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public void run() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        while (light == lp.screenBrightness) {
        }
        finish();
    }
}