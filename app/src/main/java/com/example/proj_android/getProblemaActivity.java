package com.example.proj_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.proj_android.MainActivity.SHARED_PREFS;


public class getProblemaActivity extends AppCompatActivity {

    private TextView textLatLong;
    private TextView probTit;
    private TextView probDesc;
    private TextView probTipo;
    private double latitude;
    private double longitude;
    private ImageView image;
    private  Integer id;
    private Integer idU;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_problema);

        textLatLong = findViewById(R.id.textLatLong);
        probTit = findViewById(R.id.prob_titulo);
        probDesc = findViewById(R.id.desc_prob_text);
        probTipo = findViewById(R.id.tipo_prob_text);
        image = findViewById(R.id.image_guarda);


        Intent intent = getIntent();
        final String tit = intent.getStringExtra("tit");
        final String desc = intent.getStringExtra("desc");
        final String tipo = intent.getStringExtra("tipo");
        final String img = intent.getStringExtra("img");
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);
        Intent i = getIntent();
        id = i.getIntExtra("id", 0);
        idU = i.getIntExtra("idU", 0);

        latitude = lat;
        longitude = lon;

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        token = preferences.getString("apitoken", "api");

        JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
        Call<Problema> getproblemaCall = service.getUmProblema(token, id);
        getproblemaCall.enqueue(new Callback<Problema>() {
            @Override
            public void onResponse(Call<Problema> call, Response<Problema> response) {
                if(response.body() != null){
                    Intent i = getIntent();
                    probTit.setText(i.getStringExtra("tit"));
                    probDesc.setText(i.getStringExtra("desc"));
                    probTipo.setText(i.getStringExtra("tipo"));
                }
            }

            @Override
            public void onFailure(Call<Problema> call, Throwable t) {
                Toast.makeText(getProblemaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void btnApagar(View view) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Integer userId = preferences.getInt("userid", 0);

        if(userId != idU){
            Toast.makeText(getApplicationContext(),"Não pode apagar nota!",Toast.LENGTH_SHORT).show();
        }else{
            JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
            Call<ResponseBody> deleteProb = service.deleteProblema(token, id);

            deleteProb.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Intent i = new Intent(getProblemaActivity.this, MapsActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Erro",Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public void btnAtualizar(View view) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String token = preferences.getString("apitoken", "api");
        Integer userId = preferences.getInt("userid", 0);

        if(userId != idU){
            Toast.makeText(getApplicationContext(),"Não pode atualizar a nota!",Toast.LENGTH_SHORT).show();
        }else{
            JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
            Problema problema = new Problema(probTit.getText().toString(), probDesc.getText().toString(), probTipo.getText().toString(), latitude, longitude, "asdd", userId);

            Call<Problema> updateCall = service.updateProblema(token, problema, id);
            updateCall.enqueue(new Callback<Problema>() {
                @Override
                public void onResponse(Call<Problema> call, Response<Problema> response) {
                    if(response.body() != null){
                        Intent i = new Intent(getProblemaActivity.this, MapsActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"Note Updated",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getProblemaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Problema> call, Throwable t) {

                }
            });
        }
    }

    public void pickImages(View view) {
    }
}
