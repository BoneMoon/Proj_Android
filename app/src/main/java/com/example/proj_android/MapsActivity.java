package com.example.proj_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.proj_android.MainActivity.SHARED_PREFS;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final float INITIAL_ZOOM = 18f;
    public static final String EXTRA_LAT = "latitude";
    public static final String EXTRA_LONG = "longitude";
    private List<Problema> probList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.add:
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(MapsActivity.this);
                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                                    double latitude = 41.813299;
                                    double longitude = -8.857961;
                                    Intent intent = new Intent(MapsActivity.this, ProblemaActivity.class);
                                    intent.putExtra(EXTRA_LAT, latitude);
                                    intent.putExtra(EXTRA_LONG, longitude);
                                    startActivity(intent);
                                }
                            }
                        }, Looper.getMainLooper());
                return true;
            case R.id.exit:
                SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                String token = preferences.getString("apitoken", "");

                if(token != null){
                    JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
                    Call<Users> logoutCall = service.PostLogout(token);
                    logoutCall.enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {
                            Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng home = new LatLng(41.813299, -8.857961);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, INITIAL_ZOOM));

        setMapLongClick(mMap);

        setInfoWindowClickToPanorama(googleMap);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String token = preferences.getString("apitoken", "api");
        JsonPedidos service = RetrofitClientInstance.getRetrofitInstance().create(JsonPedidos.class);
        Call<List<Problema>> getCall = service.getProblema(token);

        getCall.enqueue(new Callback<List<Problema>>() {
            @Override
            public void onResponse(Call<List<Problema>> call, Response<List<Problema>> response) {
                if(response.body() != null){
                    probList = response.body();
                    getMarker(probList);
                }else{
                    Toast.makeText(MapsActivity.this, "Não há notas!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Problema>> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Pin")
                        .snippet(snippet));

                Intent intent = new Intent(MapsActivity.this, ProblemaActivity.class);
                intent.putExtra(EXTRA_LAT, latLng.latitude);
                intent.putExtra(EXTRA_LONG, latLng.longitude);
                startActivity(intent);
            }
        });
    }

    private void getMarker(List<Problema> list){
        for (Problema problema: list) {
            mMap.addMarker(
                    new MarkerOptions()
                    .position(new LatLng(problema.getLatitude(), problema.getLongitude()))
                    .title(problema.getTitulo()));
        }
    }

    private void setInfoWindowClickToPanorama(GoogleMap map) {
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for(Problema problema: probList){
                    if(probList != null){
                        LatLng l = new LatLng(problema.getLatitude(), problema.getLongitude());
                        if(l.equals(marker.getPosition())){
                            Intent intent = new Intent(MapsActivity.this, getProblemaActivity.class);
                            intent.putExtra("tit", problema.getTitulo());
                            intent.putExtra("desc", problema.getDescricao());
                            intent.putExtra("tipo", problema.getTipodescricao());
                            intent.putExtra("img", problema.getFoto());
                            intent.putExtra("lat", problema.getLatitude());
                            intent.putExtra("lon", problema.getLongitude());
                            intent.putExtra("id", problema.getId());
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}
