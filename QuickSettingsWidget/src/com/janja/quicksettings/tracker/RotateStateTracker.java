package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

public class RotateStateTracker extends BiStateTracker {

    @Override
    public int getButtonId() {
        return R.id.img_rotate;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_rotate;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.rotation_on : R.drawable.rotation_off;
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

        ContentResolver resolver = context.getContentResolver();
        boolean on = Settings.System.getInt(resolver,
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
        return on ? State.ENABLED : State.DISABLED;

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
                Display display = ((WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();

                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.USER_ROTATION, display.getRotation());

                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.ACCELEROMETER_ROTATION,
                        desiredState == State.ENABLED ? 1 : 0);
                return desiredState;
            }
        }.execute();
    }
}