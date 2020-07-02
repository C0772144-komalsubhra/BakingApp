package com.example.myapplication.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.model.ListItem;
import com.example.myapplication.repo.RecipeRepo;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private RecipeRepo recipeRepo;
    private LiveData<List<ListItem>> recipeData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        recipeRepo = new RecipeRepo(application);

        if (recipeData == null) {
            Log.d("Recipe", "getContentFromAPI: Called It's null");
            recipeData = recipeRepo.getContentFromAPI();
        }

    }

    public LiveData<List<ListItem>> getContentFromAPI(){ return recipeData; }
}
