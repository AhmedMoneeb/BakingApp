package com.solutionco.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.solutionco.android.bakingapp.Data.Step;

public class RecipeVideoActivity extends AppCompatActivity {

    public static Step choosenStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        choosenStep = intent.getParcelableExtra(RecipeDetailActivity.Step_EXTRA);
        setContentView(R.layout.activity_recipe_video);
    }
}
