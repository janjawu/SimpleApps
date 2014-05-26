package com.janja.quicksettings.widget;

import com.janja.quicksettings.tracker.MobileStateTracker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;

public class SettingsObserver extends ContentObserver {

    private Context context;

    SettingsObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    void startObserving() {
        ContentResolver resolver = context.getContentResolver();

        resolver.registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS),
                false, this);

        resolver.registerContentObserver(
                Settings.Secure.getUriFor(MobileStateTracker.MOBILE_DATA),
                true, this);

        resolver.registerContentObserver(Settings.Secure
                .getUriFor(Settings.Secure.LOCATION_PROVIDERS_ALLOWED), true,
                this);

        resolver.registerContentObserver(Settings.System
                .getUriFor(Settings.System.ACCELEROMETER_ROTATION), true, this);
    }

    void stopObserving() {
        context.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange) {
        QuickSettingsWidget.updateWidget(context);
    }
}