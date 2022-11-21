package com.example.pm1e2grupo4.Modelos;

public class contactos {

    String id_usuario;
    String nombre;
    String telefono;
    String longitud;
    String latitud;
    String imagen;

    public contactos(){
        //
    }

    public contactos(String id_usuario, String nombre, String telefono, String longitud, String latitud) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.longitud = longitud;
        this.latitud = latitud;
        this.imagen = imagen;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getImagen() {
        return latitud;
    }

    public void setImagen(String latitud) {
        this.latitud = latitud;
    }
}
