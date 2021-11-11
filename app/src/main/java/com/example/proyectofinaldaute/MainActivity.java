package com.example.proyectofinaldaute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listadoProductos;

    ArrayList<String> lista = null;
    ArrayList<dto_productos> listaProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listadoProductos = findViewById(R.id.listviewProductos);

        mostrarProductos(getApplicationContext());

    }


    public void mostrarProductos(final Context context) {

        listaProductos = new ArrayList<dto_productos>();
        lista = new ArrayList<String>();
        String url = "https://defunctive-loran.000webhostapp.com/getAllProductos.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ArrayList<String> values = new ArrayList<String>();

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                try {

                    JSONArray array = new JSONArray(response);

                    int totalEncontrados = array.length();

                    dto_productos objProductos = null;

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject productosObject = array.getJSONObject(i);
                        int id_categoria = Integer.parseInt(productosObject.getString("id"));
                        String nombre_categoria = productosObject.getString("nombre");
                        double precio = Double.parseDouble(productosObject.getString("precio"));


                        objProductos = new dto_productos(id_categoria, nombre_categoria, precio);

                        listaProductos.add(objProductos);

                        lista.add(listaProductos.get(i).getId() + "-" + listaProductos.get(i).getNombre());




                        values.add(String.valueOf(id_categoria) + " - " + nombre_categoria );

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, values);
                        listadoProductos.setAdapter(adapter);
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



}

