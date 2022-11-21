package com.example.pm1e2grupo4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pm1e2grupo4.Conexion.conexion;
import com.example.pm1e2grupo4.Modelos.contactos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListarActivity extends AppCompatActivity {

    Button btnAtras, btnUbi,btnBuscar;
    EditText txtBuscar;
    EditText aeid, aenombre, aetelefono, aelatitud, aelongitud;
    String Id_usuario, Nombre, Telefono, Latitud, Longitud;

    public static ListarActivity ma;
    protected Cursor cursor;

    ArrayList<contactos> thelist;
    ListView listview;
    List<contactos> listItems;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        btnAtras = (Button) findViewById(R.id.btnAtras);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);


        getSupportActionBar().setTitle("Listar Datos");

        recyclerView = (RecyclerView) findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListarActivity.this));

        progressDialog = new ProgressDialog(this);
        listItems = new ArrayList<>();
        ma = this;

        refresh_list();

       /* btnUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });*/

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().trim().length() == 0) {
                    //BusquedadPersonalizada("");
                } else {
                  //  BusquedadPersonalizada(s.toString());
                }


            }
        });
    }





    public void refresh_list(){
        listItems.clear();
        adapter = new MyAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog.setMessage("Cargando");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, conexion.URL_SELECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    progressDialog.hide();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    Toast.makeText(ListarActivity.this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    for (int i = 0; i<jsonArray.length(); i++){
                        JSONObject o = jsonArray.getJSONObject(i);
                        contactos item = new contactos(
                                o.getString("id_usuario"),
                                o.getString("nombre"),
                                o.getString("telefono"),
                                o.getString("latitud"),
                                o.getString("longitud")

                        );
                        listItems.add(item);

                        adapter = new MyAdapter(listItems,getApplicationContext());
                        recyclerView.setAdapter(adapter);

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(ListarActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("name", "kl");
                return params;
            }
        };
        RequestHandler.getInstance(ListarActivity.this).addToRequestQueue(stringRequest);
    }
}