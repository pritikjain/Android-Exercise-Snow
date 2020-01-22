package com.servicenow.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.servicenow.exercise.BuildConfig;
import com.servicenow.resources.Game;
import com.servicenow.resources.Games;
import com.servicenow.resources.NESGames;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameController implements Callback<Games> {


    public interface Callback {
        void onResponse(List<Game> list);
        void onFailure(Throwable t);
    }


    public static final Game[] nesGames = NESGames.INSTANCE.getList();

    WeakReference<Callback> mCallbackHandler;
    Call<Games> mApiCall;


    static final String BASE_URL = "https://jsonblob.com/";


    public boolean start(Callback callback) {

        mCallbackHandler = new WeakReference<>(callback);
        if (!BuildConfig.UseCloud) {
            callback.onResponse(Arrays.asList(nesGames));
            return true;
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GameApiEndpoint gameApi = retrofit.create(GameApiEndpoint.class);

        Call<Games> call = gameApi.get();
        // execute asynchronously
        call.enqueue(this);

        return true;
    }


    public void stop() {
        if (mApiCall != null) {
            mApiCall.cancel();
            mCallbackHandler = null;
        }
    }

    @Override
    public void onResponse(Call<Games> call, Response<Games> response) {
        if (mCallbackHandler.get() == null) {
            return;
        }
        if (response.isSuccessful()) {
            List<Game> gameList = response.body().games;
            mCallbackHandler.get().onResponse(gameList);
        } else {
            mCallbackHandler.get().onFailure(new Throwable("Failed to retrived data"));
        }

        mApiCall = null;
    }

    @Override
    public void onFailure(Call<Games> call, Throwable t) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
        mCallbackHandler.get().onFailure(t);
        mApiCall = null;
    }


}
