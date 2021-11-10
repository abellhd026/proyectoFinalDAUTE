package com.example.proyectofinaldaute;

public class dto_categorias {
    String id, nombre;
    int estado;

    public dto_categorias(int id_categoria, String nombre_categoria, int estado_categoria) {
    }

    public dto_categorias() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
