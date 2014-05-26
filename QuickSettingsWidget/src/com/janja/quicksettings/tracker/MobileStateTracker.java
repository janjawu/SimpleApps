package com.janja.quicksettings.tracker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.janja.quicksettings.widget.QuickSettingsWidget;
import com.janja.quicksettings.widget.R;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.provider.Settings;

public class MobileStateTracker extends BiStateTracker {
    public static final String MOBILE_DATA = "mobile_data";

    @Override
    public int getButtonId() {
        return R.id.img_mobile;
    }

    @Override
    public int getIndicatorId() {
        return R.id.ind_mobile;
    }

    @Override
    public int getButtonImageId(State state) {
        boolean on = state == State.ENABLED;
        return on ? R.drawable.data_on : R.drawable.data_off;
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
        boolean on = Settings.Secure.getInt(resolver, MOBILE_DATA, 0) == 1;
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

                ConnectivityManager conMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                Class<?> conMgrClass = null;
                Field iConMgrField = null;
                Object iConMgr = null;
                Class<?> iConMgrClass = null;
                Method setMobileDataEnabledMethod = null;

                try {
                    conMgrClass = Class.forName(conMgr.getClass().getName());
                    iConMgrField = conMgrClass.getDeclaredField("mService");
                    iConMgrField.setAccessible(true);
                    iConMgr = iConMgrField.get(conMgr);
                    iConMgrClass = Class.forName(iConMgr.getClass().getName());
                    setMobileDataEnabledMethod = iConMgrClass
                            .getDeclaredMethod("setMobileDataEnabled",
                                    Boolean.TYPE);
                    setMobileDataEnabledMethod.setAccessible(true);
                    setMobileDataEnabledMethod.invoke(iConMgr,
                            desiredState == State.ENABLED);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return desiredState;
            }
        }.execute();
    }
}