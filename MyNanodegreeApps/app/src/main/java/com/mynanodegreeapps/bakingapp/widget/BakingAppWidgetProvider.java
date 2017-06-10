package com.mynanodegreeapps.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mynanodegreeapps.R;
import com.mynanodegreeapps.bakingapp.BakingActivity;
import com.mynanodegreeapps.bakingapp.BakingIngredientListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = getBakingListView(context);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);

        AppWidgetManager appManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidgetProvider.class);
        RemoteViews updateViews = getBakingListView(context);

        appManager.updateAppWidget(thisWidget, updateViews);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BakingAppWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widgetList);
        }

        super.onReceive(context, intent);
    }

    private static RemoteViews getBakingListView(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.baking_widget_provider);

        Intent intent = new Intent(context,BakingAppRemoteViewService.class);
        views.setRemoteAdapter(R.id.widgetList ,intent);

        Intent ingredientIntent = new Intent(context, BakingIngredientListActivity.class);
        PendingIntent ingredientPendingIntent = PendingIntent.getActivity(
                context,
                0,
                ingredientIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widgetList,ingredientPendingIntent);

        return views;
    }
}