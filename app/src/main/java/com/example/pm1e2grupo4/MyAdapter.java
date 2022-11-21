package com.example.pm1e2grupo4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pm1e2grupo4.Conexion.conexion;
import com.example.pm1e2grupo4.Modelos.contactos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private List<contactos> listItems;

    private Context context;
    private ProgressDialog dialog;

    public MyAdapter(List<contactos> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView id_usuario;
        public TextView nombre;
        public TextView telefono;
        public TextView latitud;
        public TextView longitud;
        public CardView card_view;
        public ViewHolder(View itemView ) {
            super(itemView);
            id_usuario = (TextView) itemView.findViewById(R.id.id_usuario);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            telefono = (TextView) itemView.findViewById(R.id.telefono);
            latitud = (TextView) itemView.findViewById(R.id.longitud);
            longitud = (TextView) itemView.findViewById(R.id.latitud);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final contactos listItem = listItems.get(position);
        holder.id_usuario.setText(listItem.getId_usuario());
        holder.nombre.setText(listItem.getNombre());
        holder.telefono.setText(listItem.getTelefono());
        holder.latitud.setText(listItem.getLatitud());
        holder.longitud.setText(listItem.getLongitud());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final ProgressDialog dialog = new ProgressDialog(view.getContext());
                dialog.setMessage("Cargando los datos borrados");

                final CharSequence[] dialogitem = {"Ver Datos","Editar Datos","Ubicacion en Google Maps","Borrar Datos"};
                builder.setTitle(listItem.getNombre());
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                Intent intent = new Intent(view.getContext(), DetailDataActivity.class);
                                intent.putExtra("id_usuario", listItem.getId_usuario());
                                intent.putExtra("nombre",listItem.getNombre());
                                intent.putExtra("telefono",listItem.getTelefono());
                                intent.putExtra("latitud",listItem.getLatitud());
                                intent.putExtra("longitud", listItem.getLongitud());
                                view.getContext().startActivity(intent);
                                break;

                            case 1 :
                                Intent intent2 = new Intent(view.getContext(), EditActivity.class);
                                intent2.putExtra("id_usuario", listItem.getId_usuario());
                                intent2.putExtra("nombre",listItem.getNombre());
                                intent2.putExtra("telefono",listItem.getTelefono());
                                intent2.putExtra("latitud",listItem.getLatitud());
                                intent2.putExtra("longitud", listItem.getLongitud());
                                view.getContext().startActivity(intent2);
                                break;

                            case 2:
                                Intent intent3 = new Intent(view.getContext(), MapsActivity.class);
                                intent3.putExtra("latitud",listItem.getLatitud());
                                intent3.putExtra("longitud", listItem.getLongitud());
                                view.getContext().startActivity(intent3);
                                break;

                            case 3 :
                                AlertDialog.Builder builderDel = new AlertDialog.Builder(view.getContext());
                                builderDel.setTitle(listItem.getNombre());
                                builderDel.setMessage("¿Está seguro de que desea eliminar los datos?");
                                builderDel.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialog.show();

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, conexion.URL_DELETE, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                dialog.hide();
                                                dialog.dismiss();
                                                Toast.makeText(view.getContext(),"Dato eliminado con exito: " + listItem.getNombre(), Toast.LENGTH_LONG).show();
                                                ListarActivity.ma.refresh_list();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                dialog.hide();
                                                dialog.dismiss();
                                                Toast.makeText(view.getContext(),"Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }){
                                            protected HashMap<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_usuario", listItem.getId_usuario());
                                                return (HashMap<String, String>) params;
                                            }
                                        };
                                        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
                                        dialogInterface.dismiss();
                                    }
                                });

                                builderDel.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override

                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builderDel.create().show();
                                break;
                        }
                    }
                });

                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
