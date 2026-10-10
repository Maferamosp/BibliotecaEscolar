package model;

public class UsuarioEstudiante extends Usuario {

    public UsuarioEstudiante(int id, String nombre, String rut, String correo, String contrasena) {
        super(id, nombre, rut, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "estudiante";
    }

    @Override
    public String getTipo() {
        return "Estudiante";
    }

    @Override
    public boolean tieneGestionTotal() {
        return false;
    }
}
