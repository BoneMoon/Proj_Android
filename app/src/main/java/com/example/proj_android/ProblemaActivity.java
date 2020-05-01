package com.example.proj_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.proj_android.MainActivity.SHARED_PREFS;
import static com.example.proj_android.MapsActivity.EXTRA_LAT;
import static com.example.proj_android.MapsActivity.EXTRA_LONG;

public class ProblemaActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int REQUEST_IMAGE_ACESS = 1001;
    private TextView textLatLong;
    private TextView probTit;
    private TextView probDesc;
    private TextView probTipo;
    private double latitude;
    private double longitude;
    private ImageView image;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(EXTRA_LAT, 0.0);
        longitude = intent.getDoubleExtra(EXTRA_LONG, 0.0);

        textLatLong = findViewById(R.id.textLatLong);
        probTit = findViewById(R.id.prob_titulo);
        probDesc = findViewById(R.id.prob_desc);
        probTipo = findViewById(R.id.prob_tipo);
        image = findViewById(R.id.image_guarda);

        button = findViewById(R.id.button_guarda);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ProblemaActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getCurrentLocation();
            return;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION: {
                if(grantResults.length>0){
                    getCurrentLocation();
                }else{
                    Toast.makeText(this, "Permissão negada!", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case REQUEST_IMAGE_ACESS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImagesGallery();
                }else{
                    Toast.makeText(this, "Permissão negada!", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    public void pickImages(View view) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                requestPermissions(permissions, REQUEST_IMAGE_ACESS);
            } else {
                pickImagesGallery();
            }
        }else {
            pickImagesGallery();
        }
    }

    private void pickImagesGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //define a imagem para a imageView
             image.setImageURI(data.getData());

            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            //Log.i("tag", imageString);
        }
    }



    private void getCurrentLocation() {
        textLatLong.setText(
                String.format(
                        "Latitude: %s\nLongitude: %s",
                        latitude,
                        longitude
                )
        );
    }

    public void btnProblema(View view) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        Integer userId = preferences.getInt("userid", 0);
        String token = preferences.getString("apitoken", "api");

        JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
        Problema problema = new Problema(probTit.getText().toString(), probDesc.getText().toString(), probTipo.getText().toString(), latitude, longitude, image.toString(), userId);
        Call<Problema> problemaCall = service.postProblema(token, problema);

        problemaCall.enqueue(new Callback<Problema>() {
            @Override
            public void onResponse(Call<Problema> call, Response<Problema> response) {
                if(response.body() != null){
                    Intent i = new Intent(ProblemaActivity.this, MapsActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(ProblemaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Problema> call, Throwable t) {
                Toast.makeText(ProblemaActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
