package com.solutionco.android.bakingapp.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.DataBase.DBHelper;
import com.solutionco.android.bakingapp.DataBase.DataBaseContract;
import com.solutionco.android.bakingapp.R;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends Activity implements RecipeAdapterAtConfiguration.ConfigClickListener {


    RecyclerView recyclerView;
    RecipeAdapterAtConfiguration adapter;
    RecyclerView.LayoutManager layoutManager;

    public static final String CHOOSEN_RECIPE_ID_EXTRA ="com.solutionco.android.bakingapp.Widget.RECIPEID";

    private static final String PREFS_NAME = "com.solutionco.android.bakingapp.Widget.RecipeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public RecipeWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.recipe_widget_configure);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        recyclerView = (RecyclerView)findViewById(R.id.configurationList);
        layoutManager = new LinearLayoutManager(this);
        new QueryInDBTask(this).execute();
        // If this activity was started with an intent without an app widget ID, finish with an error.

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }


    @Override
    public void onConfigurationClicked(String id) {

        new DoUpdateDB(id , this).execute();

    }


    private class QueryInDBTask extends AsyncTask{

        Context context;

        public QueryInDBTask(Context context) {
            this.context = context;
            Log.v("hi", String.valueOf(this.context instanceof RecipeWidgetConfigureActivity) );
        }

        @Override
        protected Object doInBackground(Object[] params) {
            DBHelper helper = new DBHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();

            String[] projection = {
                    DataBaseContract.Recipes.RECIPE_ID,
                    DataBaseContract.Recipes.RECIPE_NAME,
                    DataBaseContract.Recipes.CHOOSEN_IN_WIDGET
            };
            Cursor cursor = db.query(
                    DataBaseContract.Recipes.TABLE_NAME,
                    projection,null , null,null,null,null);
            ArrayList<Recipe> recipeArrayList = new ArrayList<>();
            while(cursor.moveToNext()) {
                Recipe t = new Recipe();
                t.setId(cursor.getString(cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_ID)));
                t.setName(cursor.getString(cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_NAME)));
                t.setSteps(null);
                t.setIngredients(null);
                recipeArrayList.add(t);
            }
            cursor.close();
            return recipeArrayList;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o!=null){
                adapter = new RecipeAdapterAtConfiguration( (ArrayList<Recipe>)o ,
                        (RecipeWidgetConfigureActivity)context);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
            }
        }
    }


    class DoUpdateDB extends AsyncTask{

        String choosen;
        Context context;

        public DoUpdateDB(String choosen , Context con) {
            this.choosen = choosen;
            context = con;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            DBHelper helper = new DBHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.Recipes.CHOOSEN_IN_WIDGET, 0);

            int count1 = db.update(
                    DataBaseContract.Recipes.TABLE_NAME,
                    values,
                    null,
                    null);

            values = new ContentValues();
            values.put(DataBaseContract.Recipes.CHOOSEN_IN_WIDGET, 1);


            String selection = DataBaseContract.Recipes.RECIPE_ID + " LIKE ?";
            String[] selectionArgs = { choosen };

            int count2 = db.update(
                    DataBaseContract.Recipes.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent(getApplicationContext(), IngredientWidgetService.class);
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            views.setRemoteAdapter(mAppWidgetId , R.id.widget_grid_view, resultValue);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            setResult(RESULT_OK, resultValue);
            ((RecipeWidgetConfigureActivity)context).finish();
        }
    }
}

