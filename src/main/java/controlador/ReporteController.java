package controlador;

import dao.PrestamoDAO;

import java.sql.SQLException;
import java.util.List;

public class ReporteController {
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public List<Object[]> librosMasPrestados() throws SQLException {
        return prestamoDAO.librosMasPrestados();
    }

    public List<Object[]> historialEstudiante(int idEstudiante) throws SQLException {
        return prestamoDAO.historialEstudiante(idEstudiante);
    }

    public List<Object[]> librosEnPrestamo() throws SQLException {
        return prestamoDAO.listarPendientes(null);
    }
}
