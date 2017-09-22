package com.solutionco.android.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solutionco.android.bakingapp.Adapters.RecipeDetailAdapter;
import com.solutionco.android.bakingapp.Data.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    Recipe currentChoosen;
    RecyclerView recyclerV;
    RecyclerView.LayoutManager layoutManager;
    RecipeDetailAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        recyclerV = (RecyclerView)view.findViewById(R.id.recipe_detail_recycler_view);

        Bundle bundle = getArguments();
        currentChoosen = RecipeDetailActivity.choosen;
        adapter = new RecipeDetailAdapter(getContext() , currentChoosen , (RecipeDetailActivity)this.getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerV.setAdapter(adapter);
        recyclerV.setLayoutManager(layoutManager);
        recyclerV.setHasFixedSize(true);
        return view;
    }

}
