package model;

public abstract class Usuario extends Persona {
    private String contrasena;

    public Usuario(int id, String nombre, String rut, String correo, String contrasena) {
        super(id, nombre, rut, correo);
        this.contrasena = contrasena;
    }

    public abstract String getRol();

    public abstract boolean tieneGestionTotal();

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
