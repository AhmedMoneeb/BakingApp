package com.solutionco.android.bakingapp.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Ahmed on 6/3/2017.
 */

public final class DataBaseContract {
    private DataBaseContract() {

    }


    public static class Recipes implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final String RECIPE_ID = "id";
        public static final String RECIPE_NAME= "name";
        public static final String CHOOSEN_IN_WIDGET= "choosen";

    }

    public static class Ingredients implements BaseColumns {
        public static final String TABLE_NAME = "ingredients";
        public static final String RECIPE_ID= "recipe_id";
        public static final String INGRDDIENT_QUANTITY = "quantity";
        public static final String INGRDDIENT_MEASURE= "measure";
        public static final String INGRDDIENT_NAME= "name";
    }
}
