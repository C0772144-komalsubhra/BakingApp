package com.example.myapplication.repo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.ListItem;
import com.example.myapplication.utils.EspressoIdlingResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.List;

public class RecipeRepo {

    private final Context context;
    private MutableLiveData<List<ListItem>> listLiveData = new MutableLiveData<>();

    public RecipeRepo(Application application){
        this.context = application;
    }

    public LiveData<List<ListItem>> getContentFromAPI() {
        EspressoIdlingResource.increment();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                context.getResources().getString(R.string.url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    List<ListItem> listItems = gson.fromJson(jsonArray.toString(),
                            new TypeToken<List<ListItem>>(){}.getType());
                    listLiveData.postValue(listItems);
                    EspressoIdlingResource.decrement();
                }catch (Exception ignored){ }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Recipe", "onErrorResponse: " + error.getMessage());
                EspressoIdlingResource.decrement();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return listLiveData;
    }

}
