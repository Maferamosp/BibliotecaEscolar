package controlador;

import dao.LibroDAO;
import model.Categoria;
import model.Libro;

import java.sql.SQLException;
import java.util.List;

public class LibroController {
    private final LibroDAO libroDAO = new LibroDAO();

    public List<Libro> listar() throws SQLException {
        return libroDAO.listar();
    }

    public List<Categoria> listarCategorias() throws SQLException {
        return libroDAO.listarCategorias();
    }

    public void guardar(Libro libro) throws SQLException {
        if (libro.getTitulo().isBlank() || libro.getAutor().isBlank() || libro.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Título, autor e ISBN son obligatorios.");
        }
        if (libro.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (libro.getId() == 0) {
            libroDAO.insertar(libro);
        } else {
            libroDAO.actualizar(libro);
        }
    }

    public void eliminar(int idLibro) throws SQLException {
        libroDAO.eliminar(idLibro);
    }
}
