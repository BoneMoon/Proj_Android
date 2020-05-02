package com.example.proj_android;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Problema {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("titulo")
    @Expose
    private String titulo;

    @SerializedName("descricao")
    @Expose
    private String descricao;

    @SerializedName("tipodescricao")
    @Expose
    private String tipodescricao;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    public Problema(String titulo, String descricao, String tipodescricao, double latitude, double longitude, String foto, Integer user_id){
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipodescricao = tipodescricao;
        this.foto = foto;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipodescricao() {
        return tipodescricao;
    }

    public String getFoto() {
        return foto;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setTipodescricao(String tipodescricao) {
        this.tipodescricao = tipodescricao;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
