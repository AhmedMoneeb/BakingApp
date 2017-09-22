package com.solutionco.android.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.Utilities.NetworkUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Ahmed on 6/5/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityScreenTest {

    // parameter number 3 (false) means that the activity isn't started automatically
    @Rule
    public ActivityTestRule<RecipeDetailActivity> RecipeDetailActivityTestRule
            = new ActivityTestRule<>(RecipeDetailActivity.class, false , false);


    @Test
    public void clickRecipeStepRecyclerViewItem_ShowsDescibtion() {
        new GetAllRecipes().execute();
    }

    class GetAllRecipes extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                return NetworkUtil.getRecipes();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Bundle b = new Bundle();
            b.putParcelableArrayList(MainActivity.ARRAY_LIST_EXTRA ,(ArrayList<Recipe>) o);
            b.putInt(MainActivity.INDEX_EXTRA , 0);
            Intent intent = new Intent();
            intent.putExtra(MainActivity.BUNDLE_EXTRA,b);
            RecipeDetailActivityTestRule.launchActivity(intent);
            Recipe currentRecipe = RecipeDetailActivity.choosen;
            int pos = currentRecipe.getIngredients().size();
            onView(withId(R.id.recipe_detail_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(pos, click()));

            onView(withId(R.id.full_description_textView)).
                    check(matches(withText(currentRecipe.getSteps().get(pos).getDescription())));

        }
    }
}
