package com.example.proyectofinaldaute;

public class dto_categorias {
    int id;
    String nombre;
    int estado;

    public dto_categorias(int id, String nombre, int estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public dto_categorias() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
