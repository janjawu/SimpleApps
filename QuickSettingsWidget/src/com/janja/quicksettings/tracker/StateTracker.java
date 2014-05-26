package com.janja.quicksettings.tracker;

import android.content.Context;
import android.widget.RemoteViews;

public interface StateTracker {

    public static final int POS_LEFT = 0;
    public static final int POS_CENTER = 1;
    public static final int POS_RIGHT = 2;

    public abstract int getButtonId();

    public abstract int getIndicatorId();

    public abstract int getButtonPosition();

    public abstract void setImageViewResources(Context context,
            RemoteViews views);

    public abstract void toggleState(Context context);

    public abstract void onActualStateChange(Context context);
}