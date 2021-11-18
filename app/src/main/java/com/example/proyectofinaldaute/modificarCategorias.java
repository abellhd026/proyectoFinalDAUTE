package com.example.proyectofinaldaute;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.proyectofinaldaute.ui.Categorias.categorias;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class modificarCategorias extends Activity implements View.OnClickListener {
    private EditText idC, nombre;
    Spinner estado;
    String idCategoria = "";
    String datoSelected = "";
    private Button actualizar, eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_categorias);

        Bundle bundle = new Bundle();

        String id = getIntent().getStringExtra("valorC");

        String s[] = id.split(" - ");

        idCategoria = s[0].trim();

        idC = findViewById(R.id.id_cat);
        nombre = findViewById(R.id.nombre_cat);
        actualizar = findViewById(R.id.btnUpdate);
        eliminar = findViewById(R.id.btnDelete);
        estado = findViewById(R.id.estado_cat);
        showCategoriasInfo(getApplicationContext(), idCategoria);

        actualizar.setOnClickListener(this);
        eliminar.setOnClickListener(this);

        idC.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoCategorias, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(estado.getSelectedItemPosition() > 0){
                    datoSelected = estado.getSelectedItem().toString();
                }else {
                    datoSelected = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }





    private void showCategoriasInfo(Context context, String id) {

        String url = "https://defunctive-loran.000webhostapp.com/buscarCategoriaPorCodigo.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject productosObject = new JSONObject(response);

                    String id = productosObject.getString("id");
                    String nombreC = productosObject.getString("nombre");
                    String estadoC = productosObject.getString("estado");


                    idC.setText(id);
                    nombre.setText(nombreC);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error en el try catch" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error en la Conexion", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", id.trim());

                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void updateCategory (final Context context, String id, String nombre, String estado) {
        String url = "https://defunctive-loran.000webhostapp.com/actualizarCategoria.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject requestJSON = null;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(modificarCategorias.this, listadoCategorias.class);
                        startActivity(intent);
                        finish();

                    }else if(estado.equals("2")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(context, "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error en la conexion" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_cat", String.valueOf(id));
                map.put("nom_cat", nombre);
                map.put("est_cat", String.valueOf(estado));
                return map;
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


    private void borrarCategoria (Context context, String id_cat){
        String url = "https://defunctive-loran.000webhostapp.com/eliminarCategoria.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                JSONObject requestJSON;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String estado = requestJSON.getString("estado");
                    String mensaje = requestJSON.getString("mensaje");

                    if(estado.equals("1")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(modificarCategorias.this, listadoCategorias.class);
                        startActivity(intent);
                        finish();
                    }else if(estado.equals("2")){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se Puede Modificar. \n" + "Intentelo Más Tarde.", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id_cat", String.valueOf(id_cat));
                return map;

            }

        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnUpdate:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Actualizar");
                builder1.setMessage("¿Desea Actualizar esta categoria?");
                builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = idC.getText().toString().trim();
                        String Nombre = nombre.getText().toString();

                        updateCategory(getApplicationContext(), id, Nombre, datoSelected);
                    }
                });

                builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder1.show();

                break;

            case R.id.btnDelete:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Eliminar");
                builder2.setMessage("¿Desea Eliminar esta categoria?");
                builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrarCategoria(getApplicationContext(), idCategoria);
                    }
                });

                builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder2.show();
                break;
            }

    }

}