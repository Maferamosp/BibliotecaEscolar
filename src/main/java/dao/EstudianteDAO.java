package dao;

import controlador.DatabaseConnection;
import model.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public List<Estudiante> listar() throws SQLException {
        String sql = "SELECT id, nombre, rut, curso, correo FROM estudiantes ORDER BY id";
        List<Estudiante> estudiantes = new ArrayList<>();
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                estudiantes.add(transformar(rs));
            }
        }
        return estudiantes;
    }

    public Estudiante buscarPorRut(String rut) throws SQLException {
        String sql = "SELECT id, nombre, rut, curso, correo FROM estudiantes WHERE rut = ?";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rut);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? transformar(rs) : null;
            }
        }
    }

    public void insertar(Estudiante e) throws SQLException {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getRut());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getCorreo());
            ps.executeUpdate();
        }
    }

    private Estudiante transformar(ResultSet rs) throws SQLException {
        return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                rs.getString("curso"), rs.getString("correo"));
    }
}
