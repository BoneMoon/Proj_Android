package com.example.proj_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "token";
    EditText name;
    EditText password;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editUtilizador);
        password = findViewById(R.id.editPasse);
        email = findViewById(R.id.editEmail);
    }

    public void btnNotas(View view) {
        Intent i = new Intent(MainActivity.this, NotaActivity.class);
        startActivity(i);
    }

    public void btnMapa(View view) {
        JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
        Users user = new Users(name.getText().toString(), email.getText().toString(), password.getText().toString());
        Call<Users> userCall = service.PostUsers(user);

        userCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if(response.body() != null){
                    Users user = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("userid", user.getId());
                    editor.putString("apitoken", user.getApi_token());
                    editor.apply();
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void btRegisto(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
