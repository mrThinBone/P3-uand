package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;

/**
 * Created by DELL-INSPIRON on 3/29/2017.
 */

public class StockHawkWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // appWidgetIds: unique identifiers for each widget (of your app) user has created
        for(int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_stock_hawk);

            // control what happen when user click on app widget
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
