package com.example.gopal.f_bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gopal.f_bakingapp.ui.RecipeWidgetProvider;

/**
 * Created by Gopal on 2/14/2019.
 */

public class AddWidgetService extends IntentService{
   private static final String ACTION_ADD_WIDGET = "com.example.gopal.f_bakingapp.action_add_widget";

    public AddWidgetService() {
        super("AddWidgetService");
    }
    public static void addRecipeIngredientWidget(Context context){
        Intent intent = new Intent(context, AddWidgetService.class);
        intent.setAction(ACTION_ADD_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            String action = intent.getAction();
            if (ACTION_ADD_WIDGET.equals(action)){
                addWidget();
            }

        }
    }

    public  void addWidget(){

        Log.e("AddWidgetService","addWidget is working");
        // Get the ingredient List of desired recipe that user want to add
        String recipeIngredient = "Brownies Elite";
        //
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(this,AddWidgetService.class));

        RecipeWidgetProvider.addRecipeIngredientWidgets(this,appWidgetManager,appWidgetsId,recipeIngredient);
    }
}
