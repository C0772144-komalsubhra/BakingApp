package com.example.myapplication.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Ingredients;
import com.example.myapplication.model.ListItem;
import com.example.myapplication.model.Steps;
import com.example.myapplication.ui.adapter.RecipeListAdapter;
import com.example.myapplication.ui.detail.StepListActivity;
import com.example.myapplication.utils.Constants;
import com.example.myapplication.utils.ItemClickSupport;
import com.example.myapplication.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment{
        @BindView(R.id.recipe_list)
        RecyclerView mRecipeList;

        private Context context;
        private RecipeListAdapter adapter;

        private List<List<Steps>> stepsList = new ArrayList<>();
        private List<List<Ingredients>> ingredientsList = new ArrayList<>();
        private List<ListItem> listItems = new ArrayList<>();
        private MainViewModel viewModel;

        public RecipeFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_recipe, container, false);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            initialSetup();
            setupViewModel();
            setRclListener();
        }

        private void setupViewModel() {
            viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

            viewModel.getContentFromAPI().observe(this, new Observer<List<ListItem>>() {
                @Override
                public void onChanged(@Nullable List<ListItem> listItems) {
                    if (listItems != null && listItems.size() != 0) {
                        adapter = new RecipeListAdapter(context, new ArrayList<>(listItems));
                        mRecipeList.setAdapter(adapter);

                        RecipeFragment.this.listItems.clear();
                        RecipeFragment.this.listItems.addAll(listItems);

                        stepsList.clear();
                        ingredientsList.clear();

                        for (int i=0; i<listItems.size();i++){
                            stepsList.add(listItems.get(i).getSteps());
                            ingredientsList.add(listItems.get(i).getIngredients());
                        }
                    }
                }
            });
        }

        private void initialSetup() {
            context = getActivity();
            mRecipeList.setLayoutManager(new GridLayoutManager(context, 2));
        }

        private void setRclListener(){
            ItemClickSupport.addTo(mRecipeList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent intent = new Intent(getActivity(), StepListActivity.class);
                    intent.putParcelableArrayListExtra(Constants.STEPS,
                            new ArrayList<Parcelable>(stepsList.get(position)));
                    intent.putParcelableArrayListExtra(Constants.INGREDIENTS,
                            new ArrayList<Parcelable>(ingredientsList.get(position)));

                    savePreferenceForWidget(
                            ingredientsList.get(position),
                            listItems.get(position).getName()
                    );

                    intent.putExtra(Constants.RECIPE_NAME, listItems.get(position).getName());
                    startActivity(intent);
                }
            });
        }

        private void savePreferenceForWidget(List<Ingredients> list, String name) {
            SharedPreferences preferences = getActivity().getSharedPreferences(Constants.WIDGET, Context.MODE_PRIVATE);

            StringBuilder ingredientsString = new StringBuilder();
            makeIngredientsString(list, ingredientsString);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constants.INGREDIENTS, ingredientsString.toString());
            editor.putString(Constants.RECIPE_NAME, name);
            editor.apply();
        }

        private void makeIngredientsString(List<Ingredients> list, StringBuilder ingredientsString ) {
            for (int i=0; i<list.size();i++){
                String measure = list.get(i).getMeasure();
                String ingredient = list.get(i).getIngredient();
                float quantity = list.get(i).getQuantity();

                ingredientsString
                        .append(quantity)
                        .append(" ")
                        .append(measure)
                        .append(" ")
                        .append(ingredient)
                        .append("\n");
            }
        }
}
