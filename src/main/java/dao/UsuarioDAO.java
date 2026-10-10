package dao;

import controlador.DatabaseConnection;
import model.Usuario;

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
                    return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                            rs.getString("correo"), rs.getString("contraseña"), rs.getString("rol"));
                }
                return null;
            }
        }
    }
}
