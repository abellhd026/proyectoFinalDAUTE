package com.example.proyectofinaldaute.ui.Productos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.proyectofinaldaute.MainActivity;
import com.example.proyectofinaldaute.MySingleton;
import com.example.proyectofinaldaute.Navigation_DAUTE;
import com.example.proyectofinaldaute.R;
import com.example.proyectofinaldaute.dto_categorias;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class productos extends Fragment implements View.OnClickListener {
    private EditText id, nombre, descripcion, stock, precio, medida;
    private Spinner estado, categoria;
    private Button saved, viewP;
    int conta = 0;
    private ProgressDialog progressDialog;


    ArrayList<String> lista = null;
    ArrayList<dto_categorias> listaCategorias;
    String datoSelected = "";
    String datoSelectedC = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);


        id = view.findViewById(R.id.id_prod);
        nombre = view.findViewById(R.id.nom_prod);
        descripcion = view.findViewById(R.id.desc_prod);
        stock = view.findViewById(R.id.stock_prod);
        precio = view.findViewById(R.id.precio_prod);
        medida =  view.findViewById(R.id.med_prod);
        estado =  view.findViewById(R.id.est_prod);
        categoria =  view.findViewById(R.id.fk_categorias);
        saved = view.findViewById(R.id.btnSaveP);
        viewP = view.findViewById(R.id.btnView);
        progressDialog = new ProgressDialog(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estadoProductos, R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapter);


        // Metodo que carga las categorias en el Spinner al iniciar la vista
        fk_categorias(getContext());



        saved.setOnClickListener(this);

        // Guarda el estado seleccionado del Spinner en la variable datoSelected
        estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (estado.getSelectedItemPosition() > 0) {
                    datoSelected = estado.getSelectedItem().toString();
                } else {
                    datoSelected = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Guarda el ID de la categoria seleccionada del Spinner para asociar el producto a una categoria en especifico
        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (conta >= 1 && categoria.getSelectedItemPosition() > 0) {
                    String item_spinner = categoria.getSelectedItem().toString();

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


        // Evento OnClick que dirige al usuario a la vista del listado de los productos
        viewP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;


    }



    // Metodo que guarda los productos en la base de datos
    private void saveProductos (final Context context, String id, String nombre, String descripcion, String Stock, String Precio, String medida, String estado, String categoria) {
        String url = "https://defunctive-loran.000webhostapp.com/guardarProducto.php";

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

                    progressDialog.dismiss();

                }catch (JSONException e){
                    e.printStackTrace();
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
                map.put("nombre_prod", nombre);
                map.put("desc_prod", descripcion);
                map.put("stock", Stock);
                map.put("precio_prod", Precio);
                map.put("med_prod", medida);
                map.put("est_prod", String.valueOf(estado));
                map.put("categoria", String.valueOf(categoria));
                return map;
            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }


    @Override
    public void onClick(View view) {
        String code = id.getText().toString();
        String Nombre = nombre.getText().toString();
        String Descripcion = descripcion.getText().toString();
        String Stock = stock.getText().toString();
        String Precio = precio.getText().toString();
        String Medida = medida.getText().toString();
        String Estado = datoSelected;
        String Categoria = datoSelectedC;


        switch (view.getId()){
            case R.id.btnSaveP:

                if (validarDatos(code, Nombre, Descripcion, Stock, Precio, Medida)) { // Funcion que retorna true si hay datos ingresados
                    if (estado.getSelectedItemPosition() > 0  && categoria.getSelectedItemPosition() > 0) {

                        // Metodo que guarda  la informacion en la base de datos
                        saveProductos(getContext(),code,Nombre,Descripcion,Stock,Precio,Medida,Estado,Categoria);
                        progressDialog.setMessage("Guardando Producto..."); //esto es del progress dialog
                        progressDialog.show();  //esto es del progress dialog
                    }
                }

                break;

        }
    }



    // Metodo que consulta las categorias en la bd y las muestra en el Spinner
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


                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, lista);
                        categoria.setAdapter(adaptador);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ERROR EN LA CONEXION DE INTERNET", Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }



    // Metodo que valida que el usuario haya introducido datos
    public boolean validarDatos(String code, String name, String descrip, String strock, String prec, String medid) {

        if (code.length() == 0) {
            id.setError("Ingrese ID");

        }else if( name.length() == 0) {
            nombre.setError("Ingrese Nombre");

        }else if(descrip.length() == 0){
            descripcion.setError("Ingrese Descripcion");

        } else if (strock.length() == 0) {
            stock.setError("Ingrese Stock");

        } else if (prec.length() == 0) {
            precio.setError("Ingrese Precio");

        } else if (medid.length() == 0) {
            medida.setError("Ingrese UM");

        } else if (categoria.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Debe de seleccionar una categoria", Toast.LENGTH_SHORT).show();

        } else if (estado.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Debe seleccionar un estado para el producto", Toast.LENGTH_SHORT).show();
        }
        return true;
    }



}