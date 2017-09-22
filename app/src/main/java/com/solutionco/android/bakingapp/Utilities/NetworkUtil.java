package com.solutionco.android.bakingapp.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.solutionco.android.bakingapp.Data.Ingredient;
import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.Data.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ahmed on 5/31/2017.
 */

public final class NetworkUtil {
    private static final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    public static ArrayList<Recipe> getRecipes () throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .build();
        Response response = client.newCall(request).execute();
        return parseJSON(response.body().string());
    }


    private static ArrayList<Recipe> parseJSON (String response){
        ArrayList<Recipe> recipes = new ArrayList<>();
        try{
            JSONArray recipieArray= new JSONArray(response);

            for (int i=0; i<recipieArray.length();i++){
                Recipe recipe = new Recipe();
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                ArrayList<Step> steps = new ArrayList<>();
                JSONObject object = recipieArray.getJSONObject(i);

                recipe.setId(object.getString("id"));
                recipe.setName(object.getString("name"));

                JSONArray ingredientsArray = object.getJSONArray("ingredients");
                for(int j=0; j<ingredientsArray.length();j++){
                    Ingredient ingredient = new Ingredient();
                    JSONObject ob = ingredientsArray.getJSONObject(j);
                    ingredient.setQuantity(Double.parseDouble(ob.getString("quantity")));
                    ingredient.setMeasure(ob.getString("measure"));
                    ingredient.setIngredient(ob.getString("ingredient"));
                    ingredients.add(ingredient);
                }

                JSONArray stepsArray = object.getJSONArray("steps");
                for(int k=0 ; k<stepsArray.length();k++){
                    Step step = new Step();
                    JSONObject ob = stepsArray.getJSONObject(k);
                    step.setId(ob.getString("id"));
                    step.setShortDescription(ob.getString("shortDescription"));
                    step.setDescription(ob.getString("description"));
                    step.setVideoURL(ob.getString("videoURL"));
                    step.setThumbnailURL(ob.getString("thumbnailURL"));
                    steps.add(step);
                }

                recipe.setIngredients(ingredients);
                recipe.setSteps(steps);
                recipes.add(recipe);
            }

        }catch (Exception ex){
            System.out.println(ex);
        }


        return recipes;
    }


}
