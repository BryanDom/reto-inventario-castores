package com.castores.inventario.model;

import java.time.LocalDateTime;

public class Movimiento {
    private long idMovimiento;
    private int idProducto;
    private int idUsuario;
    private int idTipoMovimiento;
    private int cantidad;
    private LocalDateTime fechaHora;
    private String comentario;

    public Movimiento() {
    }

    public Movimiento(long idMovimiento, int idProducto, int idUsuario, int idTipoMovimiento, int cantidad,
                      LocalDateTime fechaHora, String comentario) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.idUsuario = idUsuario;
        this.idTipoMovimiento = idTipoMovimiento;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.comentario = comentario;
    }

    public long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdTipoMovimiento() {
        return idTipoMovimiento;
    }

    public void setIdTipoMovimiento(int idTipoMovimiento) {
        this.idTipoMovimiento = idTipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
