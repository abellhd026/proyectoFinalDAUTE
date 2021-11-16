package com.example.proyectofinaldaute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class modificarProductos extends AppCompatActivity{
    private EditText idP, nombre, desc, stock, precio, med;
    String idProducto = "";
    private Button actualizar, eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_productos);

        Bundle bundle = new Bundle();

        String id = getIntent().getStringExtra("valorID");

        String s[] = id.split("-");

        idProducto = s[0].trim();

        idP = findViewById(R.id.id_producto);
        nombre = findViewById(R.id.nom_producto);
        desc = findViewById(R.id.desc_producto);
        stock = findViewById(R.id.stock_producto);
        precio = findViewById(R.id.precio_producto);
        med = findViewById(R.id.med_producto);
        actualizar = findViewById(R.id.edit);
        eliminar = findViewById(R.id.delete);

        showProductsInfo(getApplicationContext(),idProducto);


       eliminar.setOnClickListener(new View.OnClickListener() {

           String code = idP.getText().toString();

           @Override
           public void onClick(View view) {
               if(code.length() != 0){
                   borrarRegistro(getApplicationContext(), Integer.parseInt(code));
                   Toast.makeText(modificarProductos.this, "Registro Eliminado!!", Toast.LENGTH_SHORT).show();
               }
           }
       });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actualizar.getText().toString().equals("Editar")) {
                    actualizar.setText(R.string.update);
                    enableEditText();
                    Toast.makeText(getBaseContext(), "Actualizar", Toast.LENGTH_SHORT).show();
                } else {
                    actualizar.setText(R.string.edit);
                    hideEditText();
                    Toast.makeText(getBaseContext(), "Editar", Toast.LENGTH_SHORT).show();

                    String code = idP.getText().toString();
                    String Nombre = nombre.getText().toString();
                    String Descripcion = desc.getText().toString();
                    String Stock = stock.getText().toString();
                    String Precio = precio.getText().toString();
                    String Medida = med.getText().toString();


                    // LLamar aca al metodo que actualiza el registro en la base de datos (updateProductos)
                }

            }
        });


    }


    //Crear aca el metodo para actualizar y eliminar el producto
    private void updateProductos (final Context context, String id, String nombre, String descripcion, String Stock, String Precio, String medida) {
        String url = "https://defunctive-loran.000webhostapp.com/actualizarProducto.php";

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
                map.put("id_prod", String.valueOf(id));
                map.put("nom_prod", nombre);
                map.put("desc_prod", descripcion);
                map.put("stock_prod", Stock);
                map.put("precio_prod", Precio);
                map.put("med_prod", medida);
                return map;
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


    private void showProductsInfo(Context context, String id) {

        String url = "https://defunctive-loran.000webhostapp.com/getProductoCodigo.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject productosObject = new JSONObject(response);

                    String id = productosObject.getString("id");
                    String nombreP = productosObject.getString("nombre");
                    String descripcion = productosObject.getString("descripcion");
                    String stockP = productosObject.getString("stock");
                    String precioP = productosObject.getString("precio");
                    String medidaP = productosObject.getString("medida");
                    //String estadoP = productosObject.getString("estado");
                    //String categoriaP = productosObject.getString("categoria");

                    hideEditText();
                    idP.setText(id);
                    nombre.setText(nombreP);
                    desc.setText(descripcion);
                    stock.setText(stockP);
                    precio.setText(precioP);
                    med.setText(medidaP);

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
                map.put("id_prod", id.trim());

                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void hideEditText() {
        idP.setEnabled(false);
        nombre.setEnabled(false);
        desc.setEnabled(false);
        stock.setEnabled(false);
        precio.setEnabled(false);
        med.setEnabled(false);
    }


    public void enableEditText() {
        nombre.setEnabled(true);
        desc.setEnabled(true);
        stock.setEnabled(true);
        precio.setEnabled(true);
        med.setEnabled(true);
    }

    private void borrarRegistro (Context context, final int id_cat){
        String url = "https://defunctive-loran.000webhostapp.com/eliminarProducto.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                JSONObject requestJSON;
                try {
                    requestJSON = new JSONObject(response.toString());
                    String id = requestJSON.getString("id");
                    String mensaje = requestJSON.getString("mensaje");

                    if(id.length() > 0){
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                    }else{
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
                map.put("id_prod", String.valueOf(id_cat));
                return map;

            }

        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }//Fin del metodo saveServer
}