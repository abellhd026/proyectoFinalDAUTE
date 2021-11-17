package com.example.proyectofinaldaute.ui.Categorias;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.proyectofinaldaute.MainActivity;
import com.example.proyectofinaldaute.MySingleton;
import com.example.proyectofinaldaute.R;
import com.example.proyectofinaldaute.listadoCategorias;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("ALL")
public class  categorias extends Fragment implements View.OnClickListener {


    private EditText id, nombre;
    private Spinner estado;
    private Button save, delete;
    String datoSelected = "";
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_categorias, container, false);

        id = root.findViewById(R.id.id_cat);
        nombre = root.findViewById(R.id.nombre_cat);
        estado = root.findViewById(R.id.estado_cat);
        save = root.findViewById(R.id.btnSave);
        delete = root.findViewById(R.id.btndelete);
        progressDialog = new ProgressDialog(getContext());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estadoCategorias, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        estado.setAdapter(adapter);

        //Evento del Spinner
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

        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        return root;
    }

    //HACER ACA EL METODO PARA VALIDACION DE DATOS
    public boolean validarDatos(String code, String name) {
        if (code.length() == 0) {
            id.setError("Ingrese un ID");

        } else if( nombre.length() == 0) {
            nombre.setError("Ingrese un Nombre");
        } else if (estado.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Debe seleccionar un estado para la categoria", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //HACER ACA EL EVENTO ONCLICK DEL BOTON GUARDAR
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSave:

                String code = id.getText().toString();
                String name = nombre.getText().toString();

                if (validarDatos(code, name)) { // Funcion que retorna true si hay datos ingresados
                    if (estado.getSelectedItemPosition() > 0) {

                        // Metodo que guarda  la informacion en la base de datos
                        saveServer(getContext(), Integer.parseInt(code), name, Integer.parseInt(datoSelected));
                        progressDialog.setMessage("Guardando categoria..."); //esto es del progress dialog
                        progressDialog.show();  //esto es del progress dialog

                    }
                }

                break;

            case R.id.btndelete:
                Intent intent = new Intent(getContext(), listadoCategorias.class);
                startActivity(intent);


                break;
            default:
        }
    }


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

                    progressDialog.dismiss();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se puedo guardar. \n" + "Intentelo más tarde.", Toast.LENGTH_SHORT).show();
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

    //metodo para el boton nueva categoria
    private void new_categories() {
        id.setText(null);
        nombre.setText(null);
        estado.setSelection(0);
    }

}