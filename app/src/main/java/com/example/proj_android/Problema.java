package com.example.proj_android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Problema {

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

    @SerializedName("localizacao")
    @Expose
    private String localizacao;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    public Problema(String titulo, String descricao, String tipodescricao, String foto, String localizacao, Integer user_id){
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipodescricao = tipodescricao;
        this.foto = foto;
        this.localizacao = localizacao;
        this.user_id = user_id;
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

    public String getLocalizacao() {
        return localizacao;
    }

    public Integer getUser_id() {
        return user_id;
    }
}
