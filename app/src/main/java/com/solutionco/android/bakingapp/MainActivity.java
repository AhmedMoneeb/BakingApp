package com.solutionco.android.bakingapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.solutionco.android.bakingapp.Adapters.RecipeAdapter;
import com.solutionco.android.bakingapp.Data.Ingredient;
import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.DataBase.DBHelper;
import com.solutionco.android.bakingapp.DataBase.DataBaseContract;
import com.solutionco.android.bakingapp.Utilities.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> , RecipeAdapter.ListItemClickListener {


    TextView problemDescription;

    RecyclerView recyclerView;
    LayoutManager layoutManager;
    RecipeAdapter adapter;
    Toolbar mainActivityToolbar ;

    public static final String ARRAY_LIST_EXTRA = "com.solutionco.android.bakingapp.allRecipes";
    public static final String INDEX_EXTRA = "com.solutionco.android.bakingapp.index";
    public static final String BUNDLE_EXTRA = "com.solutionco.android.bakingapp.BUNDLE";

    private static final int LOADER_ID = 2103;
    private ArrayList<Recipe> allRecipes = new ArrayList<>();

    ProgressDialog progress;
    boolean tablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityToolbar = (Toolbar) findViewById(R.id.main_activity_toolBar);
        setSupportActionBar(mainActivityToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        progress = new ProgressDialog(this);
        progress.setTitle("");
        progress.setMessage("Getting Recipes...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        recyclerView = (RecyclerView)findViewById(R.id.all_recipe_recycler_view);

        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.destroyLoader(LOADER_ID);

        Loader<ArrayList<Recipe>> recipeLoader = loaderManager.getLoader(LOADER_ID);

     if(connectedToNetwork()){
         loaderManager.initLoader(LOADER_ID, null , this);

         tablet = findViewById(R.id.tablet_indicator) != null ;

         if(tablet){
             layoutManager = new GridLayoutManager(this , 3);
         }
         else{
             layoutManager = new LinearLayoutManager(this);
         }
     }
     else{
         progress.dismiss();
         setContentView(R.layout.activity_main_connection_problem);
         problemDescription = (TextView)findViewById(R.id.connectivity_problem_text_view);
         problemDescription.setText(R.string.no_network);
         mainActivityToolbar = (Toolbar) findViewById(R.id.main_activity_toolBar);
         setSupportActionBar(mainActivityToolbar);
         getSupportActionBar().setTitle(R.string.app_name);
     }



    }



    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<  ArrayList<Recipe>  >(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
               // progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                try {
                    return NetworkUtil.getRecipes();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {


        //progressBar.setVisibility(View.INVISIBLE);
        if(data!=null){
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            String[] projection = {
                    DataBaseContract.Recipes.RECIPE_ID,
                    DataBaseContract.Recipes.RECIPE_NAME
            };
            Cursor cursor = db.query(
                    DataBaseContract.Recipes.TABLE_NAME,
                    projection,null , null,null,null,null);
            allRecipes = new ArrayList<>();
            while(cursor.moveToNext()) {
                Recipe t = new Recipe();
                t.setId(cursor.getString(cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_ID)));
                t.setName(cursor.getString(cursor.getColumnIndex(DataBaseContract.Recipes.RECIPE_NAME)));
                t.setSteps(null);
                t.setIngredients(null);
                allRecipes.add(t);
            }

            if(allRecipes == null || allRecipes.size()!= data.size()){
                new InsertInDBTask((Context)this , data).execute();
            }
            allRecipes = data;
            adapter = new RecipeAdapter(allRecipes , this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            progress.dismiss();
        }
        else{
            progress.dismiss();
            setContentView(R.layout.activity_main_connection_problem);
            problemDescription = (TextView)findViewById(R.id.connectivity_problem_text_view);
            problemDescription.setText(R.string.no_data_loaded);
            mainActivityToolbar = (Toolbar) findViewById(R.id.main_activity_toolBar);
            setSupportActionBar(mainActivityToolbar);
            getSupportActionBar().setTitle(R.string.app_name);

        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    @Override
    public void onListItemClicked(int index) {
        Intent intent = new Intent(this , RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARRAY_LIST_EXTRA , allRecipes);
        bundle.putInt( INDEX_EXTRA , index);
        intent.putExtra(BUNDLE_EXTRA,bundle);
        startActivity(intent);
    }

    public boolean connectedToNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void reload(View v){
        recreate();
    }

    private class InsertInDBTask extends AsyncTask {

        ArrayList<Recipe> r ;
        Context context;

        public InsertInDBTask(Context c , ArrayList<Recipe> r) {
            this.r = r;
            context = c;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(r!=null){
                DBHelper helper = new DBHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                for (int i=0 ; i<r.size(); i++){
                    ContentValues values = new ContentValues();
                    values.put(DataBaseContract.Recipes.RECIPE_ID ,r.get(i).getId());
                    values.put(DataBaseContract.Recipes.RECIPE_NAME , r.get(i).getName());
                    values.put(DataBaseContract.Recipes.CHOOSEN_IN_WIDGET , 0 );
                    db.insert(DataBaseContract.Recipes.TABLE_NAME, null, values);
                }

                for(int i=0; i<r.size() ; i++){
                    for(int j =0 ; j<r.get(i).getIngredients().size() ; j++){
                        Ingredient temp = r.get(i).getIngredients().get(j);
                        ContentValues values = new ContentValues();
                        values.put(DataBaseContract.Ingredients.RECIPE_ID ,r.get(i).getId());
                        values.put(DataBaseContract.Ingredients.INGRDDIENT_QUANTITY, temp.getQuantity() );
                        values.put(DataBaseContract.Ingredients.INGRDDIENT_MEASURE , temp.getMeasure());
                        values.put(DataBaseContract.Ingredients.INGRDDIENT_NAME, temp.getIngredient());
                        db.insert(DataBaseContract.Ingredients.TABLE_NAME, null, values);
                    }
                }



            }
            return null;
        }

    }

}
