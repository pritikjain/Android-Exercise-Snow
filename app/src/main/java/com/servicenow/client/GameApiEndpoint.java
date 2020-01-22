package com.servicenow.client;

import com.servicenow.resources.Games;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GameApiEndpoint {

    @GET("api/jsonBlob/f5bf443c-160d-11ea-ab7b-c5ee597d34d8")
    Call<Games> get();

}
