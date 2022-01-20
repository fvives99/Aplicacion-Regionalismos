package com.example.myapplication;

public class Palabras {

    String palabra;
    String significado;
    String pais;
    String id;

    public Palabras(){}


    public Palabras(String palabra, String significado, String pais) {
        this.palabra = palabra;
        this.significado = significado;
        this.pais = pais;

    }



    public String getPalabra() {
        return palabra;
    }

    public String getSignificado() {
        return significado;
    }

    public String getPais(){
        return pais;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
