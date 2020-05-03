package com.example.proj_android;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPedidos {

    @GET("notas")
    Call<List<Problema>> getProblema(
            @Header("Authorization")
            String token
    );

    @GET("notas/{id}")
    Call<Problema> getUmProblema(
            @Header("Authorization")
                    String token,
            @Path("id")
                    Integer id
    );

    @POST("notas")
    Call<Problema> postProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    Problema problema
    );

    @POST("notas/{id}")
    Call<Problema> updateProblema(
            @Header("Authorization")
                    String token,
            @Body()
                    Problema problema,
            @Path("id")
                    Integer id
    );

    @DELETE("notas/{id}")
    Call<ResponseBody> deleteProblema(
            @Header("Authorization")
                    String token,
            @Path("id")
                    Integer id
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
