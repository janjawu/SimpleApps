package com.janja.quicksettings.widget;

import com.janja.quicksettings.tracker.BluetoothStateTracker;
import com.janja.quicksettings.tracker.BrightnessStateTracker;
import com.janja.quicksettings.tracker.GpsStateTracker;
import com.janja.quicksettings.tracker.MobileStateTracker;
import com.janja.quicksettings.tracker.RotateStateTracker;
import com.janja.quicksettings.tracker.StateTracker;
import com.janja.quicksettings.tracker.SyncStateTracker;
import com.janja.quicksettings.tracker.VolumeStateTracker;
import com.janja.quicksettings.tracker.WifiStateTracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.RemoteViews;

public class QuickSettingsWidget extends AppWidgetProvider {

    static final ComponentName THIS_APPWIDGET = new ComponentName(
            "com.janja.quicksettings.widget",
            "com.janja.quicksettings.widget.QuickSettingsWidget");

    private static final int BUTTON_WIFI = 0;
    private static final int BUTTON_BRIGHTNESS = 1;
    private static final int BUTTON_SYNC = 2;
    private static final int BUTTON_GPS = 3;
    private static final int BUTTON_BLUETOOTH = 4;
    private static final int BUTTON_MOBILE = 5;
    private static final int BUTTON_ROTATE = 6;
    private static final int BUTTON_VOLUME = 7;

    private static final String SYNC_CONN_STATUS_CHANGED = "com.android.sync.SYNC_CONN_STATUS_CHANGED";

    private static final StateTracker sWifiState = new WifiStateTracker();
    private static final StateTracker sBluetoothState = new BluetoothStateTracker();
    private static final StateTracker sGpsState = new GpsStateTracker();
    private static final StateTracker sSyncState = new SyncStateTracker();
    private static final StateTracker sMobileState = new MobileStateTracker();
    private static final StateTracker sRotateState = new RotateStateTracker();
    private static final StateTracker sBrightness = new BrightnessStateTracker();
    private static final StateTracker sVolume = new VolumeStateTracker();

    private static SettingsObserver sSettingsObserver;

    @Override
    public void onEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(
                "com.janja.quicksettings.widget", ".QuickSettingsWidget"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        checkObserver(context);
    }

    @Override
    public void onDisabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(
                "com.janja.quicksettings.widget", ".QuickSettingsWidget"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        if (sSettingsObserver != null) {
            sSettingsObserver.stopObserving();
            sSettingsObserver = null;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

        RemoteViews view = buildUpdate(context);

        for (int i = 0; i < appWidgetIds.length; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i], view);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            sWifiState.onActualStateChange(context);
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            sBluetoothState.onActualStateChange(context);
        } else if (SYNC_CONN_STATUS_CHANGED.equals(action)) {
            sSyncState.onActualStateChange(context);
        } else if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(action)) {
            sVolume.onActualStateChange(context);
        } else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            Uri data = intent.getData();
            int buttonId = Integer.parseInt(data.getSchemeSpecificPart());
            if (buttonId == BUTTON_WIFI) {
                sWifiState.toggleState(context);
            } else if (buttonId == BUTTON_BRIGHTNESS) {
                sBrightness.toggleState(context);
            } else if (buttonId == BUTTON_SYNC) {
                sSyncState.toggleState(context);
            } else if (buttonId == BUTTON_GPS) {
                sGpsState.toggleState(context);
            } else if (buttonId == BUTTON_BLUETOOTH) {
                sBluetoothState.toggleState(context);
            } else if (buttonId == BUTTON_MOBILE) {
                sMobileState.toggleState(context);
            } else if (buttonId == BUTTON_ROTATE) {
                sRotateState.toggleState(context);
            } else if (buttonId == BUTTON_VOLUME) {
                sVolume.toggleState(context);
            }
        } else {
            return;
        }

        updateWidget(context);
    }

    private static void checkObserver(Context context) {
        if (sSettingsObserver == null) {
            sSettingsObserver = new SettingsObserver(new Handler(),
                    context.getApplicationContext());
            sSettingsObserver.startObserving();
        }
    }

    public static void updateWidget(Context context) {
        checkObserver(context);
        RemoteViews views = buildUpdate(context);
        final AppWidgetManager gm = AppWidgetManager.getInstance(context);
        gm.updateAppWidget(THIS_APPWIDGET, views);
    }

    static RemoteViews buildUpdate(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget);

        views.setOnClickPendingIntent(R.id.btn_wifi,
                getLaunchPendingIntent(context, BUTTON_WIFI));

        views.setOnClickPendingIntent(R.id.btn_brightness,
                getLaunchPendingIntent(context, BUTTON_BRIGHTNESS));

        views.setOnClickPendingIntent(R.id.btn_sync,
                getLaunchPendingIntent(context, BUTTON_SYNC));

        views.setOnClickPendingIntent(R.id.btn_gps,
                getLaunchPendingIntent(context, BUTTON_GPS));

        views.setOnClickPendingIntent(R.id.btn_bluetooth,
                getLaunchPendingIntent(context, BUTTON_BLUETOOTH));

        views.setOnClickPendingIntent(R.id.btn_mobile,
                getLaunchPendingIntent(context, BUTTON_MOBILE));

        views.setOnClickPendingIntent(R.id.btn_rotate,
                getLaunchPendingIntent(context, BUTTON_ROTATE));

        views.setOnClickPendingIntent(R.id.btn_volume,
                getLaunchPendingIntent(context, BUTTON_VOLUME));

        updateButtons(views, context);
        return views;
    }

    private static PendingIntent getLaunchPendingIntent(Context context,
            int buttonId) {
        Intent launchIntent = new Intent();
        launchIntent.setClass(context, QuickSettingsWidget.class);
        launchIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        launchIntent.setData(Uri.parse("custom:" + buttonId));
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, launchIntent,
                0);
        return pi;
    }

    private static void updateButtons(RemoteViews views, Context context) {
        sWifiState.setImageViewResources(context, views);
        sBluetoothState.setImageViewResources(context, views);
        sGpsState.setImageViewResources(context, views);
        sSyncState.setImageViewResources(context, views);
        sBrightness.setImageViewResources(context, views);
        sMobileState.setImageViewResources(context, views);
        sRotateState.setImageViewResources(context, views);
        sVolume.setImageViewResources(context, views);
    }
}