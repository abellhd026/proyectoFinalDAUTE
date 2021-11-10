package com.example.proyectofinaldaute.ui.Productos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class productos extends Fragment {
    private EditText id, nombre, descripcion, stock, precio, medida;
    private Spinner estado, categoria;
    private Button saved, viewP;

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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estadoProductos, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.estadoCategorias, R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categoria.setAdapter(adapter2);


        // Inflate the layout for this fragmen


        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = id.getText().toString();
                String Nombre = nombre.getText().toString();
                String Descripcion = descripcion.getText().toString();
                String Stock = stock.getText().toString();
                String Precio = precio.getText().toString();
                String Medida = medida.getText().toString();
                String Estado = datoSelected;
                String Categoria = datoSelectedC;


                Toast.makeText(getContext(), "Guardando...", Toast.LENGTH_SHORT).show();
                saveProductos(getContext(),code,Nombre,Descripcion,Stock,Precio,Medida,Estado,Categoria);


            }
        });


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


        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoria.getSelectedItemPosition() > 0) {
                    datoSelectedC = categoria.getSelectedItem().toString();
                } else {
                    datoSelectedC = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        return view;


    }


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
        MySingleton.getInstance(context).addToRequestQueue(request);;


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
                    dto_categorias objCategorias = new dto_categorias();

                    for (int i = 1; i < array.length(); i++) {
                        JSONObject categoriasObject = array.getJSONObject(i);
                        int id_categoria = categoriasObject.getInt("id");
                        String nombre_categoria = categoriasObject.getString("nombre");
                        int estado_categoria = Integer.parseInt(categoriasObject.getString("estado"));

                        objCategorias = new dto_categorias(id_categoria, nombre_categoria, estado_categoria);

                        objCategorias.setNombre(nombre_categoria);
                        objCategorias.setEstado(estado_categoria);

                        listaCategorias.add(objCategorias);

                        lista.add(listaCategorias.get(i).getId()+"-"+listaCategorias.get(i).getNombre());


                        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lista);
                        adaptador.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
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
    }


}