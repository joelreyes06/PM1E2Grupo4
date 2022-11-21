package com.example.pm1e2grupo4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pm1e2grupo4.Conexion.conexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    ImageView aeViewImage;
    TextView aeid;
    EditText aenombre, aetelefono, aelatitud, aelongitud;
    String Id_usuario, Nombre, Telefono, Latitud, Longitud;
    Button btnEditar;
    Boolean valid = true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        aeid = (TextView) findViewById(R.id.aeid);
        aenombre = (EditText) findViewById(R.id.aeNombre);
        aetelefono = (EditText) findViewById(R.id.aeTelefono);
        aelatitud = (EditText) findViewById(R.id.aeLatitud);
        aelongitud = (EditText) findViewById(R.id.aeLongitud);
        btnEditar = (Button) findViewById(R.id.btnEditar);

        progressDialog = new ProgressDialog(this);

        Id_usuario = getIntent().getStringExtra("id_usuario");
        aeid.setText(getIntent().getStringExtra("id_usuario"));
        aenombre.setText(getIntent().getStringExtra("nombre"));
        aetelefono.setText(getIntent().getStringExtra("telefono"));
        aelatitud.setText(getIntent().getStringExtra("latitud"));
        aelongitud.setText(getIntent().getStringExtra("longitud"));


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Nombre = aenombre.getText().toString();
                Telefono = aetelefono.getText().toString();
                Latitud = aelatitud.getText().toString();
                Longitud = aelongitud.getText().toString();

                if(TextUtils.isEmpty(Nombre)){
                    aenombre.setError("Nombre no puede estar vacío");
                    valid = false;
                }else {
                    valid = true;

                    if(TextUtils.isEmpty(Telefono)){
                        aetelefono.setError("Telefono no puede estar vacío");
                        valid = false;
                    }else {
                        valid = true;

                        if(TextUtils.isEmpty(Latitud)){
                            aelatitud.setError("latitud no puede estar vacío");
                            valid = false;
                        }else {
                            valid = true;

                            if(TextUtils.isEmpty(Longitud)){
                                aelongitud.setError("Longitud no puede estar vacío");
                                valid = false;
                            }else {
                                valid = true;
                            }
                        }

                    }
                }

                if(valid){
                    progressDialog.setMessage("Cargando");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, conexion.URL_UPDATE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(EditActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                if(jsonObject.getString("message").equals("Dato actualizado con exito")){
                                    ListarActivity.ma.refresh_list();
                                    finish();
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(EditActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(EditActivity.this, "Error en los datos: " + error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        protected Map<String , String> getParams() throws AuthFailureError {
                            Map<String , String> params = new HashMap<>();
                            params.put("id_usuario", Id_usuario);
                            params.put("nombre", Nombre);
                            params.put("telefono", Telefono);
                            params.put("latitud", Latitud);
                            params.put("longitud",Longitud);
                            return params;
                        }
                    };
                    RequestHandler.getInstance(EditActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });

    }
}