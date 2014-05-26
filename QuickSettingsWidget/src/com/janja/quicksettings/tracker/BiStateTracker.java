package com.janja.quicksettings.tracker;

import android.content.Context;
import android.widget.RemoteViews;

public abstract class BiStateTracker implements StateTracker {

    public static enum State {
        DISABLED, ENABLED, INTERMEDIATE
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
            case DISABLED:
                views.setImageViewResource(buttonId,
                        getButtonImageId(State.DISABLED));
                views.setImageViewResource(indicatorId,
                        getIndicatorImageId(State.DISABLED));
                break;
            case ENABLED:
                views.setImageViewResource(buttonId,
                        getButtonImageId(State.ENABLED));
                views.setImageViewResource(indicatorId,
                        getIndicatorImageId(State.ENABLED));

                break;
            case INTERMEDIATE:
                boolean inTransition = getActualState(context) == State.INTERMEDIATE;
                boolean isTurningOn = inTransition
                        && intendedState == State.ENABLED;
                boolean isTurningOff = inTransition
                        && intendedState == State.DISABLED;

                if (isTurningOn) {
                    views.setImageViewResource(buttonId,
                            getButtonImageId(State.ENABLED));
                    views.setImageViewResource(indicatorId,
                            getIndicatorImageId(State.ENABLED));
                } else if (isTurningOff) {
                    views.setImageViewResource(buttonId,
                            getButtonImageId(State.DISABLED));
                    views.setImageViewResource(indicatorId,
                            getIndicatorImageId(State.DISABLED));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void toggleState(Context context) {
        State currentState = getActualState(context);
        switch (currentState) {
            case ENABLED:
                intendedState = State.DISABLED;
                break;
            case DISABLED:
                intendedState = State.ENABLED;
                break;
            default:
                break;
        }

        boolean inTransition = currentState == State.INTERMEDIATE;
        if (!inTransition) {
            requestStateChange(context, intendedState);
        }
    }
}
