package com.example.proyectofinaldaute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
    int conta = 0;
    String datoSelectedC = "", datoSelected = "";
    Spinner estadoProducto, estadoCategoria;
    private Button actualizar, eliminar;
    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;

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
        estadoProducto = findViewById(R.id.est_producto);
        estadoCategoria = findViewById(R.id.est_categorias);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.estadoProductos, R.layout.support_simple_spinner_dropdown_item);

        estadoProducto.setAdapter(adapter);


        fk_categorias(getApplicationContext());



        showProductsInfo(getApplicationContext(),idProducto);




       eliminar.setOnClickListener(new View.OnClickListener() {

           String code = idP.getText().toString().trim();

           @Override
           public void onClick(View view) {

                   borrarRegistro(getApplicationContext(), idProducto);

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


                    updateProductos(getApplicationContext(), code, Nombre, Descripcion, Stock, Precio, Medida, datoSelected, datoSelectedC);
                }

            }
        });

        estadoProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (estadoProducto.getSelectedItemPosition() > 0) {
                    datoSelected = estadoProducto.getSelectedItem().toString();
                } else {
                    datoSelected = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        estadoCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (conta >= 1 && estadoProducto.getSelectedItemPosition() > 0) {
                    String item_spinner = estadoProducto.getSelectedItem().toString();

                    String s[] = item_spinner.split("-");

                    datoSelectedC = s[0].trim();

                } else {
                    datoSelectedC = "";
                }
                conta++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void updateProductos (final Context context, String id, String nombre, String descripcion, String Stock, String Precio, String medida, String estado, String categoria) {
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
                map.put("est_prod" , estado);
                map.put("categoria", categoria);

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

    private void borrarRegistro (Context context, String id_prod){
        String url = "https://defunctive-loran.000webhostapp.com/eliminarProducto.php";

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
                        Intent intent = new Intent(modificarProductos.this, MainActivity.class);
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
                map.put("id_prod", String.valueOf(id_prod));
                return map;

            }

        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }




    public void fk_categorias(final Context context) {

        listaCategorias = new ArrayList<dto_categorias>();
        lista = new ArrayList<String>();
        lista.add("Seleccione Categoria");
        String url = "https://defunctive-loran.000webhostapp.com/buscarCategorias.php";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray array = new JSONArray(response);

                    int totalEncontrados = array.length();

                    dto_categorias objCategorias = null;

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria = Integer.parseInt(categoriasObject.getString("id"));
                        String nombre_categoria = categoriasObject.getString("nombre");
                        int estado_categoria = Integer.parseInt(categoriasObject.getString("estado"));



                        objCategorias = new dto_categorias(id_categoria, nombre_categoria, estado_categoria);



                        listaCategorias.add(objCategorias);

                        lista.add(listaCategorias.get(i).getId()+"-"+listaCategorias.get(i).getNombre());

                        //lista.add(id_categoria,nombre_categoria);


                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, lista);
                        // adaptador.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        estadoCategoria.setAdapter(adaptador);


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