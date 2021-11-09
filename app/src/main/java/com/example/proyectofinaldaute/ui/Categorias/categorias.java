package com.example.proyectofinaldaute.ui.Categorias;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.proyectofinaldaute.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class  categorias extends Fragment implements View.OnClickListener {


    private EditText id, nombre;
    private Spinner estado;
    private Button save;
    String datoSelected = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);

        id = root.findViewById(R.id.id_cat);
        nombre = root.findViewById(R.id.nombre_cat);
        estado = root.findViewById(R.id.estado_cat);
        save = root.findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estadoCategorias, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        save.setOnClickListener(this);

        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }
    //HACER ACA EL METODO PARA VALIDACION DE DATOS
    public boolean validarDatos(String code, String name) {
        if (code.length() == 0 || nombre.length() == 0) {
            id.setError("Ingrese un ID");
            nombre.setError("Ingrese un Nombre");
            return false;
        } else {
            return true;
        }
    }

    //HACER ACA EL EVENTO ONCLICK DEL BOTON GUARDAR
   


    private void saveServer (final Context context, final int id_cat, final String name_cat, final int est_cat){
        String url = "https://defunctive-loran.000webhostapp.com/guardarCategoria.php";

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
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String>  map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(id_cat));
                map.put("nombre", name_cat);
                map.put("estado", String.valueOf(est_cat));
                return map;

            }

        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }//Fin del metodo saveServer



}