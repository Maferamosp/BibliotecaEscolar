package model;

import java.util.Date;

public class Prestamo {
    private int id;
    private int idEstudiante;
    private int idLibro;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private boolean devuelto;

    public Prestamo(int id, int idEstudiante, int idLibro, Date fechaPrestamo, Date fechaDevolucion, boolean devuelto) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
    }

    public int getId() {
        return id;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }
}
