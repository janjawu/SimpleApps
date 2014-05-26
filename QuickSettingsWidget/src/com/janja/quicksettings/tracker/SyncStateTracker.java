package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

public class SyncStateTracker extends BiStateTracker {

    @Override
    public int getButtonId() {
        return R.id.img_sync;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_sync;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.sync_on : R.drawable.sync_off;
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
        boolean on = ContentResolver.getMasterSyncAutomatically();
        return on ? State.ENABLED : State.DISABLED;
    }

    @Override
    public void onActualStateChange(Context context) {
        QuickSettingsWidget.updateWidget(context);
    }

    @Override
    public void requestStateChange(final Context context,
            final State desiredState) {
        final boolean sync = ContentResolver.getMasterSyncAutomatically();

        new AsyncTask<Void, Void, State>() {
            @Override
            protected State doInBackground(Void... args) {

                if (desiredState == State.ENABLED) {
                    if (!sync) {
                        ContentResolver.setMasterSyncAutomatically(true);
                    }
                    return State.ENABLED;
                }

                if (sync) {
                    ContentResolver.setMasterSyncAutomatically(false);
                }
                return State.DISABLED;
            }
        }.execute();
    }
}