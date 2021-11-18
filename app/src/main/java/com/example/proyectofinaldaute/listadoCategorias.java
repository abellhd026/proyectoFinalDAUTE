package com.example.proyectofinaldaute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listadoCategorias extends AppCompatActivity {
    ListView listadoCategorias;

    ArrayList<String> lista = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_categorias);


        listadoCategorias = findViewById(R.id.listadoCategorias);


        mostrarCategorias(getApplicationContext());



        listadoCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), modificarCategorias.class);
                String c = (String) listadoCategorias.getItemAtPosition(position);
                intent.putExtra("valorC", c);
                startActivity(intent);
            }
        });
    }

    public void mostrarCategorias(final Context context) {

        lista = new ArrayList<String>();
        String url = "https://defunctive-loran.000webhostapp.com/getAllCategorias.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> values = new ArrayList<String>();


                try {

                    JSONArray array = new JSONArray(response);


                    for (int i = 0; i < array.length(); i++) {

                        JSONObject productosObject = array.getJSONObject(i);
                        int id_categoria = Integer.parseInt(productosObject.getString("id"));
                        String nombre_categoria = productosObject.getString("nombre");


                        values.add(String.valueOf(id_categoria) + " - " + nombre_categoria );

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, values);
                        listadoCategorias.setAdapter(adapter);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR EN LA CONEXION DE INTERNET", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Regresar) {
            Toast.makeText(listadoCategorias.this, "Regresar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(listadoCategorias.this, Navigation_DAUTE.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

