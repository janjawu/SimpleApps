package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.AsyncTask;

public class BluetoothStateTracker extends BiStateTracker {

    private BluetoothAdapter bluetoothAdapter = null;

    @Override
    public int getButtonId() {
        return R.id.img_bluetooth;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_bluetooth;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.bt_on : R.drawable.bt_off;
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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            int bluetoothState = bluetoothAdapter.getState();
            switch (bluetoothState) {
                case BluetoothAdapter.STATE_OFF:
                    return State.DISABLED;
                case BluetoothAdapter.STATE_ON:
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

        if (bluetoothAdapter == null) {
            return;
        }

        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... args) {
                if (desiredState == State.ENABLED) {
                    bluetoothAdapter.enable();
                } else {
                    bluetoothAdapter.disable();
                }
                return desiredState;
            }
        }.execute();
    }
}