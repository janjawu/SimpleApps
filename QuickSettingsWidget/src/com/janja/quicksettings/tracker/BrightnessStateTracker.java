package com.janja.quicksettings.tracker;

import com.janja.quicksettings.activity.LightController;
import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class BrightnessStateTracker extends MultiStateTracker {

    private static final int BRIGHTNESS_DIM = 20;
    private static final int BRIGHTNESS_ON = 255;
    private static final int MINIMUM_BACKLIGHT = BRIGHTNESS_DIM + 10;
    private static final int MAXIMUM_BACKLIGHT = BRIGHTNESS_ON;
    private static final int DEFAULT_BACKLIGHT = (int) (BRIGHTNESS_ON * 0.4f);
    private static final int HALF_BRIGHTNESS_THRESHOLD = (int) (0.3 * MAXIMUM_BACKLIGHT);
    private static final int FULL_BRIGHTNESS_THRESHOLD = (int) (0.8 * MAXIMUM_BACKLIGHT);

    @Override
    public int getButtonId() {
        return R.id.img_brightness;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_brightness;
    }

    @Override
    public int getButtonImageId(State state) {
        switch (state) {
            case LOW:
                return R.drawable.bright_low;
            case MID:
                return R.drawable.bright_mid;
            case HIGH:
                return R.drawable.bright_high;
        }
        return R.drawable.bright_low;
    }

    @Override
    public int getIndicatorImageId(State state) {
        if (state == State.LOW) {
            return R.drawable.signal_off;
        } else {
            return R.drawable.signal_on;
        }
    }

    @Override
    public int getButtonPosition() {
        return POS_RIGHT;
    }

    @Override
    public State getActualState(Context context) {
        int brightness = getBrightness(context);

        if (brightness > FULL_BRIGHTNESS_THRESHOLD) {
            return State.HIGH;
        } else if (brightness > HALF_BRIGHTNESS_THRESHOLD) {
            return State.MID;
        } else {
            return State.LOW;
        }
    }

    @Override
    public void onActualStateChange(Context context) {
        QuickSettingsWidget.updateWidget(context);
    }

    @Override
    protected void requestStateChange(Context context, State desiredState) {
        int brightness;
        if (desiredState == State.MID) {
            brightness = DEFAULT_BACKLIGHT;
        } else if (desiredState == State.HIGH) {
            brightness = MAXIMUM_BACKLIGHT;
        } else {
            brightness = MINIMUM_BACKLIGHT;
        }

        ContentResolver cr = context.getContentResolver();
        Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        Intent brightnessIntent = new Intent();
        brightnessIntent.setClass(context, LightController.class);
        brightnessIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(brightnessIntent);

    }

    private int getBrightness(Context context) {

        int brightness;
        try {
            brightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            brightness = 0;
            e.printStackTrace();
        }
        return brightness;

    }
}