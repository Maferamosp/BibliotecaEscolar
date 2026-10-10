package dao;

import controlador.DatabaseConnection;
import model.Categoria;
import model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public List<Libro> listar() throws SQLException {
        String sql = "SELECT id, titulo, autor, isbn, editorial, stock, id_categoria FROM libros ORDER BY titulo";
        List<Libro> libros = new ArrayList<>();
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                libros.add(transformar(rs));
            }
        }
        return libros;
    }

    public List<Categoria> listarCategorias() throws SQLException {
        String sql = "SELECT id, nombre FROM categorias ORDER BY nombre";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return categorias;
    }

    public void insertar(Libro l) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.executeUpdate();
        }
    }

    public void actualizar(Libro l) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, editorial = ?, stock = ?, id_categoria = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.setInt(7, l.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection con = DatabaseConnection.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int descontarStock(Connection con, int idLibro) throws SQLException {
        String sql = "UPDATE libros SET stock = stock - 1 WHERE id = ? AND stock > 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            return ps.executeUpdate();
        }
    }

    public void aumentarStock(Connection con, int idLibro) throws SQLException {
        String sql = "UPDATE libros SET stock = stock + 1 WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            ps.executeUpdate();
        }
    }

    private Libro transformar(ResultSet rs) throws SQLException {
        return new Libro(rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("isbn"), rs.getString("editorial"), rs.getInt("stock"), rs.getInt("id_categoria"));
    }
}
