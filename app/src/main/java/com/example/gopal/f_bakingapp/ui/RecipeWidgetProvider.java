package com.example.gopal.f_bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.gopal.f_bakingapp.AddWidgetService;
import com.example.gopal.f_bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String list) {
       // String list1 = "Brownie";

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.recipe_ingredients, list);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

      //AddWidgetService.addRecipeIngredientWidget(context);
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId, recipeIngredient);
//        }

    }
    public static void addRecipeIngredientWidgets(Context context, AppWidgetManager appWidgetManager,
                                                  int[] appWidgetIds, String recipeIngredient) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeIngredient);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            String loadedRecipe = intent.getStringExtra("recipe");
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.recipe_ingredients);
           // this.onUpdate(context,mgr,mgr.getAppWidgetIds(cn));
            addRecipeIngredientWidgets(context,mgr,mgr.getAppWidgetIds(cn),loadedRecipe);
        }
    }
}

