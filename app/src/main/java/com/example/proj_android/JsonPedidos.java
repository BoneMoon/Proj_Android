package com.example.proj_android;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JsonPedidos {

    @GET("notas")
    Call<List<Problema>> getProblema(
            @Header("Authorization")
            String token
    );

    @GET("notas/{nota}")
    Call<Problema> getUmProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    String id
    );

    @POST("notas")
    Call<Problema> postProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    Problema problema
    );

    @POST("notas/{nota}")
    Call<Problema> updateProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    Problema problema
    );

    @DELETE("notas/{nota}")
    Call<Problema> deleteProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    String id
    );

    @POST("login")
    Call<Users> PostUsers(
            @Body
            Users users
    );

    @POST("register")
    Call<Users> RegisterUsers(
            @Body
                    Users users
    );

    @POST("logout")
    Call<Users> PostLogout(
            @Header("Authorization")
                    String token
    );


}
