package model;

public class Bibliotecario extends Usuario {

    public Bibliotecario(int id, String nombre, String rut, String correo, String contrasena) {
        super(id, nombre, rut, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "bibliotecario";
    }

    @Override
    public String getTipo() {
        return "Bibliotecario";
    }

    @Override
    public boolean tieneGestionTotal() {
        return true;
    }
}
