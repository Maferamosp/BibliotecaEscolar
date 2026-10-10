package dao;

import controlador.DatabaseConnection;
import model.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static final String SELECT_PRESTAMO = "SELECT p.id, "
            + "(SELECT e.nombre FROM estudiantes e WHERE e.id = p.id_estudiante) AS nombre, "
            + "(SELECT l.titulo FROM libros l WHERE l.id = p.id_libro) AS titulo, "
            + "p.fecha_prestamo, p.fecha_devolucion, p.devuelto "
            + "FROM prestamos p ";

    public void insertar(Connection con, Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, FALSE)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, new Date(p.getFechaPrestamo().getTime()));
            ps.setDate(4, new Date(p.getFechaDevolucion().getTime()));
            ps.executeUpdate();
        }
    }

    public int marcarDevuelto(Connection con, int idPrestamo) throws SQLException {
        String sql = "UPDATE prestamos SET devuelto = TRUE WHERE id = ? AND devuelto = FALSE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            return ps.executeUpdate();
        }
    }

    public Prestamo buscarPorId(Connection con, int idPrestamo) throws SQLException {
        String sql = "SELECT id, id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto FROM prestamos WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Prestamo(rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"),
                        rs.getDate("fecha_prestamo"), rs.getDate("fecha_devolucion"), rs.getBoolean("devuelto"));
            }
        }
    }

    public List<Object[]> listarPendientes(Integer idEstudiante) throws SQLException {
        String sql = SELECT_PRESTAMO
                + "WHERE p.devuelto = FALSE " + (idEstudiante != null ? "AND p.id_estudiante = ? " : "")
                + "ORDER BY p.fecha_devolucion";

        List<Object[]> filas = new ArrayList<>();

        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idEstudiante != null) {
                ps.setInt(1, idEstudiante);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(filaPrestamo(rs));
                }
            }
        }
        return filas;
    }

    public List<Object[]> historialEstudiante(int idEstudiante) throws SQLException {
        String sql = SELECT_PRESTAMO + "WHERE p.id_estudiante = ? ORDER BY p.fecha_prestamo DESC";
        List<Object[]> filas = new ArrayList<>();
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(filaPrestamo(rs));
                }
            }
        }
        return filas;
    }

    public List<Object[]> librosMasPrestados() throws SQLException {
        String sql = "SELECT (SELECT l.titulo FROM libros l WHERE l.id = p.id_libro) AS titulo, "
                + "COUNT(p.id) AS veces FROM prestamos p "
                + "GROUP BY p.id_libro ORDER BY veces DESC, titulo";
        List<Object[]> filas = new ArrayList<>();
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                filas.add(new Object[]{rs.getString("titulo"), rs.getInt("veces")});
            }
        }
        return filas;
    }

    private Object[] filaPrestamo(ResultSet rs) throws SQLException {
        Date vencimiento = rs.getDate("fecha_devolucion");
        String estado;
        if (rs.getBoolean("devuelto")) {
            estado = "Devuelto";
        } else if (vencimiento.toLocalDate().isBefore(java.time.LocalDate.now())) {
            estado = "ATRASADO";
        } else {
            estado = "En prestamo";
        }
        return new Object[]{rs.getInt("id"), rs.getString("nombre"), rs.getString("titulo"),
                rs.getDate("fecha_prestamo"), vencimiento, estado};
    }
}
