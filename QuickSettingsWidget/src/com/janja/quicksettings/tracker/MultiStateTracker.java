package com.janja.quicksettings.tracker;

import android.content.Context;
import android.widget.RemoteViews;

public abstract class MultiStateTracker implements StateTracker {
    public static enum State {
        LOW, MID, HIGH
    }

    private State intendedState;

    public abstract int getButtonImageId(State state);

    public abstract int getIndicatorImageId(State state);

    protected abstract void requestStateChange(Context context,
            State desiredState);

    public abstract State getActualState(Context context);

    @Override
    public void setImageViewResources(Context context, RemoteViews views) {
        int buttonId = getButtonId();
        int indicatorId = getIndicatorId();

        switch (getActualState(context)) {
            case LOW:
                views.setImageViewResource(buttonId,
                        getButtonImageId(State.LOW));
                views.setImageViewResource(indicatorId,
                        getIndicatorImageId(State.LOW));
                break;
            case MID:
                views.setImageViewResource(buttonId,
                        getButtonImageId(State.MID));
                views.setImageViewResource(indicatorId,
                        getIndicatorImageId(State.MID));

                break;
            case HIGH:
                views.setImageViewResource(buttonId,
                        getButtonImageId(State.HIGH));
                views.setImageViewResource(indicatorId,
                        getIndicatorImageId(State.HIGH));
                break;
        }
    }

    @Override
    public void toggleState(Context context) {
        State currentState = getActualState(context);
        switch (currentState) {
            case LOW:
                intendedState = State.MID;
                break;
            case MID:
                intendedState = State.HIGH;
                break;
            case HIGH:
                intendedState = State.LOW;
                break;
        }

        requestStateChange(context, intendedState);
    }
}
