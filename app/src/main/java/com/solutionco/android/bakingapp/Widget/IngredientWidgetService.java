package com.solutionco.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.solutionco.android.bakingapp.DataBase.DBHelper;
import com.solutionco.android.bakingapp.DataBase.DataBaseContract;
import com.solutionco.android.bakingapp.R;

/**
 * Created by Ahmed on 6/3/2017.
 */

public class IngredientWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewsFactory(this.getApplicationContext() );
    }
}

class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    Cursor cursor;
    Context context;
    String recipeID ;
    public static String recipeName;

    public IngredientRemoteViewsFactory( Context context ) {
        this.context = context;
//        SharedPreferences settings = context.getSharedPreferences("SelectedRecipeID4theWidget", 0);
//        recipeID = settings.getString("RecipeID4theWidget", "1");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection0 = {
                DataBaseContract.Recipes.RECIPE_ID, DataBaseContract.Recipes.RECIPE_NAME};

        String selection0 = DataBaseContract.Recipes.CHOOSEN_IN_WIDGET + " = ?";
        String[] selectionArgs0 = {"1"};
        Cursor temp_cursor = db.query(
                DataBaseContract.Recipes.TABLE_NAME,
                projection0,
                selection0,
                selectionArgs0,
                null,
                null,
                null
        );

        int aaaaa = temp_cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_ID);
        int recipeNameIndex = temp_cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_NAME);
        temp_cursor.moveToFirst();
        recipeID = temp_cursor.getString(aaaaa);
        recipeName = temp_cursor.getString(recipeNameIndex);


        String[] projection = {
                DataBaseContract.Ingredients.RECIPE_ID,
                DataBaseContract.Ingredients.INGRDDIENT_NAME,
                DataBaseContract.Ingredients.INGRDDIENT_QUANTITY,
                DataBaseContract.Ingredients.INGRDDIENT_MEASURE
        };
        String selection = DataBaseContract.Ingredients.RECIPE_ID + " = ?";
        String[] selectionArgs = {recipeID};
        cursor = db.query(
                DataBaseContract.Ingredients.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(cursor==null)
            return 0;
        else
            return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || cursor.getCount() == 0) return null;
        cursor.moveToPosition(position);
        int ingredientNameIndex = cursor.getColumnIndex(DataBaseContract.Ingredients.INGRDDIENT_NAME);
        int ingredientQuantityIndex = cursor.getColumnIndex(DataBaseContract.Ingredients.INGRDDIENT_QUANTITY);
        int ingredientMeasureIndex = cursor.getColumnIndex(DataBaseContract.Ingredients.INGRDDIENT_MEASURE);


        String ingredientName = cursor.getString(ingredientNameIndex);
        String ingredientQuantity = cursor.getString(ingredientQuantityIndex);
        String ingredientMeasure = cursor.getString(ingredientMeasureIndex);


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item_at_widget);
        views.setTextViewText(R.id.ingredient_name_at_widget, ingredientName);
        views.setTextViewText(R.id.ingredient_quantity_at_widget, "Quantity: "+ingredientQuantity);
        views.setTextViewText(R.id.ingredient_measure_at_widget, "Measure: " + ingredientMeasure);
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    class DoQuery extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {

        return null ;
        }

    }

}
