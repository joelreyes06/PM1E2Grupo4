package com.example.pm1e2grupo4;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pm1e2grupo4.Conexion.conexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final int PETICION_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    public static String latitud="";
    public static String longitud="";

    Bitmap image, imgUser;
    byte[] fotos;
    boolean foto;

    EditText nombre, telefono;
    TextView tvlatitud, tvlongitud;
    Button btnSalvar, guardados, tomarfoto;

    ImageView imgFoto;

    String Nombre, Telefono, Latitud, Longitud;
    Boolean valid = true;
    ProgressDialog progressDialog;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.txtNom);
        telefono = (EditText) findViewById(R.id.txtTel);
        tvlongitud = (TextView) findViewById(R.id.txtLong);
        tvlatitud = (TextView) findViewById(R.id.txtLati);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        tomarfoto = (Button) findViewById(R.id.btnTomarFoto);
        guardados = (Button) findViewById(R.id.btnSalvados);
        imgFoto = (ImageView) findViewById(R.id.imgFoto);

        progressDialog = new ProgressDialog(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        guardados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListarActivity.class);
                startActivity(intent);
            }
        });

        tomarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        imgFoto.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                permisos();
                return false;
            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombre = nombre.getText().toString();
                Telefono = telefono.getText().toString();
                Latitud = tvlatitud.getText().toString();
                Longitud = tvlongitud.getText().toString();

                if(TextUtils.isEmpty(Nombre)){
                    nombre.setError("Nombre no puede estar vacío");
                    valid = false;

                } else {
                    valid = true;

                    if(TextUtils.isEmpty(Telefono)){
                        telefono.setError("Telefono no puede estar vacío");
                        valid = false;

                    } else {
                        valid = true;

                        if(TextUtils.isEmpty(Latitud)){
                            tvlatitud.setError("Latitud no puede estar vacío");
                            valid = false;

                        } else {
                            valid = true;

                            if(TextUtils.isEmpty(Longitud)){
                                tvlongitud.setError("Longitud no puede estar vacío");
                                valid = false;

                            } else {
                                valid = true;
                            }
                        }

                    }
                }

                if(valid) {
                    progressDialog.setMessage("Cargando");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, conexion.URL_ADD, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                if(jsonObject.getString("message").equals("Datos agregado con exito")) {
//                                    ListarActivity.ma.refresh_list();
//                                    finish();
                                    limpiar();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(MainActivity.this, "Error en los datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("nombre", Nombre);
                            params.put("telefono", Telefono);
                            params.put("latitud", Latitud);
                            params.put("longitud", Longitud);

                            return params;
                        }
                    };
                    RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

    }

    private void limpiar() {
        nombre.setText("");
        telefono.setText("");
        tvlatitud.setText("");
        tvlongitud.setText("");
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();

            MainActivity.setLatitud(loc.getLatitude() + "");
            MainActivity.setLongitud(loc.getLongitude() + "");
            tvlatitud.setText(loc.getLatitude() + "");
            tvlongitud.setText(loc.getLongitude() + "");
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public static String getLatitud() {
        return latitud;
    }

    public static void setLatitud(String latitud) {
        MainActivity.latitud = latitud;
    }

    public static String getLongitud() {
        return longitud;
    }

    public static void setLongitud(String longitud) {
        MainActivity.longitud =longitud;
}


    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_CAM);
        }else{
            tomarfoto_intent();
        }
    }

    private void tomarfoto_intent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent, TAKE_PIC_REQUEST);
        }
    }


    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    private void obtenerLocalizacion() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        encontrarUbicacion(getApplicationContext(), lm);
    }

    public void encontrarUbicacion(Context contexto, LocationManager locationManager) {
        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) contexto.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {
                            String longitud = String.valueOf(location.getLongitude());
                            String latitud = String.valueOf(location.getLatitude());

                            tvlongitud.setText(longitud);
                            tvlatitud.setText(latitud);
                        }

                        public void onProviderDisabled(String provider) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {
                        }
                    });
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                String longitud = String.valueOf(location.getLongitude());
                String latitud = String.valueOf(location.getLatitude());
                tvlongitud.setText(longitud);
                tvlatitud.setText(latitud);
            }
        }
    }

    private void mostrarDialogoLocalizacionNoEncontrada() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Localización")
                .setMessage("No se ha encontrado su localización")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void checkGPS(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            mostrarDialogoGPSInactivo();
        }
    }

    private void mostrarDialogoGPSInactivo() {
        new AlertDialog.Builder(this)
                .setTitle("Activación de GPS")
                .setMessage("Debe activar la ubicación de su dispositivo para acceder a todas las funciones")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}