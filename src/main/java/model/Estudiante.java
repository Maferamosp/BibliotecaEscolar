package model;

public class Estudiante {
    private int id;
    private String nombre;
    private String rut;
    private String curso;
    private String correo;

    public Estudiante(int id, String nombre, String rut, String curso, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.curso = curso;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRut() {
        return rut;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    @Override
    public String toString() {
        return nombre + " (" + rut + ")";
    }
}
