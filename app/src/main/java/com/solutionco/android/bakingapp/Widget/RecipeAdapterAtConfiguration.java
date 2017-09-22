package com.solutionco.android.bakingapp.Widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutionco.android.bakingapp.Adapters.RecipeAdapter;
import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by Ahmed on 6/3/2017.
 */

public class RecipeAdapterAtConfiguration extends RecyclerView.Adapter<RecipeAdapterAtConfiguration.RecipeAtConfigViewHolder> {

    final private ConfigClickListener listener;


    public interface ConfigClickListener{
        void onConfigurationClicked(String id);
    }

    ArrayList<Recipe> recipes;

    public RecipeAdapterAtConfiguration(ArrayList<Recipe> recipes , ConfigClickListener l) {
        this.recipes = recipes;
        listener = l;
    }


    @Override
    public RecipeAtConfigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.recipe_item_at_configuration;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id , parent ,false);
        RecipeAdapterAtConfiguration.RecipeAtConfigViewHolder viewHolder
                = new RecipeAdapterAtConfiguration.RecipeAtConfigViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAtConfigViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }





    public class RecipeAtConfigViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        public RecipeAtConfigViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.recipe_name_at_config);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex) {
            name.setText(recipes.get(listIndex).getName());
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            listener.onConfigurationClicked(recipes.get(index).getId());
        }
    }

}
