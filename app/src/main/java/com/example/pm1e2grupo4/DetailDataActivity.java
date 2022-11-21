package com.example.pm1e2grupo4;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailDataActivity extends AppCompatActivity {

    ImageView adViewImage;
    TextView adid, adnombre, adtelefono, adlatitud, adlongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        adid = (TextView) findViewById(R.id.aeid);
        adnombre = (TextView) findViewById(R.id.aenombre);
        adtelefono = (TextView) findViewById(R.id.aetelefono);
        adlatitud = (TextView) findViewById(R.id.aelatitud);
        adlongitud = (TextView) findViewById(R.id.aelongitud);

        adid.setText(getIntent().getStringExtra("id_usuario"));
        adnombre.setText(getIntent().getStringExtra("nombre"));
        adtelefono.setText(getIntent().getStringExtra("telefono"));
        adlatitud.setText(getIntent().getStringExtra("latitud"));
        adlongitud.setText(getIntent().getStringExtra("longitud"));
    }
}