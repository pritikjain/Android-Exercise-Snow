package com.servicenow.resources;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// As cloud
public class Games {
    @SerializedName("games")
    public List<Game> games;
}
