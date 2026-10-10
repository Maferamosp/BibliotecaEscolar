package controlador;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;

public class AutenticacionController {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario iniciarSesion(String rut, String contrasena) throws SQLException {
        if (rut.isBlank() || contrasena.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar RUT y contraseña.");
        }
        return usuarioDAO.autenticar(rut.trim(), contrasena);
    }
}
