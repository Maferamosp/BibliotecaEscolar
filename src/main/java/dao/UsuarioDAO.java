package dao;

import controlador.DatabaseConnection;
import model.Bibliotecario;
import model.Usuario;
import model.UsuarioEstudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String rut, String contrasena) throws SQLException {
        String sql = "SELECT id, nombre, rut, correo, contraseña, rol FROM usuarios WHERE rut = ? AND contraseña = ?";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rut);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearUsuario(rs);
                }
                return null;
            }
        }
    }

    private Usuario crearUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String rut = rs.getString("rut");
        String correo = rs.getString("correo");
        String contrasena = rs.getString("contraseña");
        if (rs.getString("rol").equals("bibliotecario")) {
            return new Bibliotecario(id, nombre, rut, correo, contrasena);
        }
        return new UsuarioEstudiante(id, nombre, rut, correo, contrasena);
    }
}
