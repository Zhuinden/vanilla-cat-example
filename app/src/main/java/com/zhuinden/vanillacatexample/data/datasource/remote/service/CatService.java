package com.zhuinden.vanillacatexample.data.datasource.remote.service;

import com.zhuinden.vanillacatexample.data.datasource.remote.api.CatsBO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Owner on 2017. 04. 29..
 */

public interface CatService {
    @GET("api/images/get?format=xml&results_per_page=20")
    Call<CatsBO> getCats();
}
