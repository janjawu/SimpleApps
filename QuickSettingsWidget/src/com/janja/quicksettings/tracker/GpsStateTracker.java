package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;

public class GpsStateTracker extends BiStateTracker {

    @Override
    public int getButtonId() {
        return R.id.img_gps;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_gps;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.gps_on : R.drawable.gps_off;
    }

    @Override
    public int getIndicatorImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.signal_on : R.drawable.signal_off;
    }

    @Override
    public int getButtonPosition() {
        return POS_CENTER;
    }

    @Override
    public State getActualState(Context context) {
        String mode = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (mode != null) {
            return mode.contains("gps") ? State.ENABLED : State.DISABLED;
        } else {
            return State.DISABLED;
        }
    }

    @Override
    public void onActualStateChange(Context context) {
        QuickSettingsWidget.updateWidget(context);
    }

    @Override
    public void requestStateChange(final Context context,
            final State desiredState) {
        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... args) {
                Intent gpsIntent = new Intent();
                gpsIntent
                        .setClassName("com.android.settings",
                                "com.android.settings.widget.SettingsAppWidgetProvider");
                gpsIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
                gpsIntent.setData(Uri.parse("custom:3"));

                try {
                    PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
                } catch (CanceledException e) {
                    e.printStackTrace();
                }
                return desiredState;
            }
        }.execute();
    }
}