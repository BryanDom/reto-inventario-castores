package com.castores.inventario.model;

import java.time.LocalDateTime;

public class Producto {
    private int idProducto;
    private String nombreProducto;
    private String descripcion;
    private int cantidadActual;
    private int estatus;
    private LocalDateTime fechaCreacion;

    public Producto() {
    }

    public Producto(int idProducto, String nombreProducto, String descripcion, int cantidadActual, int estatus,
                    LocalDateTime fechaCreacion) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.estatus = estatus;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
