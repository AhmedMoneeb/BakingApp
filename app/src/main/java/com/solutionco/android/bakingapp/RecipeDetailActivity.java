package com.solutionco.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.solutionco.android.bakingapp.Adapters.RecipeDetailAdapter;
import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.Data.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.RecipeDetailClickListener {

    TextView testTextView;
    public static Recipe choosen;
    public static Step choosenStep = null;
    ArrayList<Recipe>allRecipes;
    public static final String Step_EXTRA= "com.solutionco.android.bakingapp.ChoosenStep";

    Toolbar recipeDetailActivityToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE_EXTRA);

        allRecipes = bundle.getParcelableArrayList(MainActivity.ARRAY_LIST_EXTRA);
        int index = bundle.getInt(MainActivity.INDEX_EXTRA , 0);
        choosen = allRecipes.get(index);

        setContentView(R.layout.activity_recipe_detail);
        recipeDetailActivityToolBar = (Toolbar)findViewById(R.id.recipe_detail_activity_toolBar);
        setSupportActionBar(recipeDetailActivityToolBar);
        getSupportActionBar().setTitle(R.string.app_name);
        testTextView = (TextView)findViewById(R.id.test_test_test);
        testTextView.setText(choosen.getName());

    }

    @Override
    public void onStepClicked(int id) {

      if(findViewById(R.id.video_and_description)!=null){

            for(int i = 0; i<choosen.getSteps().size(); i++){
                if(Integer.parseInt(choosen.getSteps().get(i).getId())==id){
                    choosenStep = choosen.getSteps().get(i);
                    break;
                }
            }
            Fragment newFragment = new RecipeVideoFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.video_and_description, newFragment);

            transaction.commit();

        }
        else{
            for(int i = 0; i<choosen.getSteps().size(); i++){
                if(Integer.parseInt(choosen.getSteps().get(i).getId())==id){
                    choosenStep = choosen.getSteps().get(i);
                    Intent intent = new Intent(this , RecipeVideoActivity.class);
                    intent.putExtra(Step_EXTRA , choosenStep);
                    startActivity(intent);
                    break;
                }
            }
        }


    }
}
