package com.solutionco.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solutionco.android.bakingapp.Data.Recipe;
import com.solutionco.android.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by Ahmed on 5/31/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


    public ListItemClickListener listener;

    public interface ListItemClickListener{
        void onListItemClicked(int index);
    }

    ArrayList<Recipe>recipes;

    public RecipeAdapter(ArrayList<Recipe> recipes , ListItemClickListener l) {
        this.recipes = recipes;
        listener = l;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.recipe_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(id , parent ,false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }



    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex) {
            name.setText(recipes.get(listIndex).getName());
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            listener.onListItemClicked(index);
        }
    }
}
