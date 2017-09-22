package com.solutionco.android.bakingapp.DataBase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmed on 6/3/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    public DBHelper(Context con){
        super(con , DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECIPES_TABLE = "CREATE TABLE "+DataBaseContract.Recipes.TABLE_NAME
                + " ( "+ DataBaseContract.Recipes.RECIPE_ID + " TEXT PRIMARY KEY , "
                + DataBaseContract.Recipes.RECIPE_NAME + " TEXT NOT NULL , "
                + DataBaseContract.Recipes.CHOOSEN_IN_WIDGET + " INTEGER NOT NULL "
                + " );" ;

        final String CREATE_INGREDIENTS_TABLE = "CREATE TABLE "+DataBaseContract.Ingredients.TABLE_NAME
                + " ( "+ DataBaseContract.Ingredients.RECIPE_ID + " TEXT , "
                + DataBaseContract.Ingredients.INGRDDIENT_QUANTITY + " REAL NOT NULL ,"
                + DataBaseContract.Ingredients.INGRDDIENT_MEASURE + " TEXT NOT NULL ,"
                + DataBaseContract.Ingredients.INGRDDIENT_NAME + " TEXT NOT NULL "

                + " , FOREIGN KEY (" + DataBaseContract.Ingredients.RECIPE_ID + " ) "
                +"REFERENCES " +DataBaseContract.Recipes.TABLE_NAME + " ( " +DataBaseContract.Recipes.RECIPE_ID
                + " )"

                + " );";
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Recipes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Ingredients.TABLE_NAME);
        onCreate(db);
    }
}
