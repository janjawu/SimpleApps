package com.janja.quicksettings.tracker;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.Context;
import android.media.AudioManager;

public class VolumeStateTracker extends MultiStateTracker {

    @Override
    public int getButtonId() {
        return R.id.img_volume;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_volume;
    }

    @Override
    public int getButtonImageId(State state) {
        switch (state) {
            case HIGH:
                return R.drawable.voice_normal;
            case MID:
                return R.drawable.voice_vibrate;
            case LOW:
                return R.drawable.voice_mute;
        }
        return R.drawable.voice_normal;
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
        int VolumeType = getVolumeType(context);
        if (VolumeType == AudioManager.RINGER_MODE_NORMAL) {
            return State.HIGH;
        } else if (VolumeType == AudioManager.RINGER_MODE_VIBRATE) {
            return State.MID;
        } else if (VolumeType == AudioManager.RINGER_MODE_SILENT) {
            return State.LOW;
        }
        return State.HIGH;
    }

    @Override
    public void onActualStateChange(Context context) {
        QuickSettingsWidget.updateWidget(context);
    }

    @Override
    protected void requestStateChange(Context context, State desiredState) {
        int VolumeType = AudioManager.RINGER_MODE_NORMAL;
        if (desiredState == State.HIGH) {
            VolumeType = AudioManager.RINGER_MODE_NORMAL;
        } else if (desiredState == State.MID) {
            VolumeType = AudioManager.RINGER_MODE_VIBRATE;
        } else if (desiredState == State.LOW) {
            VolumeType = AudioManager.RINGER_MODE_SILENT;
        }

        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(VolumeType);
    }

    private int getVolumeType(Context context) {
        AudioManager audio = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return audio.getRingerMode();
    }
}
