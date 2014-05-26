package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

public class WifiStateTracker extends BiStateTracker {

    @Override
    public int getButtonId() {
        return R.id.img_wifi;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_wifi;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.wifi_on : R.drawable.wifi_off;
    }

    @Override
    public int getIndicatorImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.signal_on : R.drawable.signal_off;
    }

    public int getButtonPosition() {
        return POS_LEFT;
    }

    @Override
    public State getActualState(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            int wifiState = wifiManager.getWifiState();
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    return State.DISABLED;
                case WifiManager.WIFI_STATE_ENABLED:
                    return State.ENABLED;
                default:
                    return State.INTERMEDIATE;
            }
        }
        return State.DISABLED;
    }

    @Override
    public void onActualStateChange(Context context) {
        QuickSettingsWidget.updateWidget(context);
    }

    @Override
    protected void requestStateChange(Context context, final State desiredState) {

        final WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            return;
        }

        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... args) {
                wifiManager.setWifiEnabled(desiredState == State.ENABLED);
                return desiredState;
            }
        }.execute();
    }
}