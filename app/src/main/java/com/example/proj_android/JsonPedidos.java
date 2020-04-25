package com.example.proj_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPedidos {

    @GET("notas")
    Call<List<Problema>> getProblema();
}
