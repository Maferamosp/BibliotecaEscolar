package controlador;

import dao.LibroDAO;
import dao.PrestamoDAO;
import model.Prestamo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PrestamoController {
    private static final int DIAS_PRESTAMO = 7;
    private static final Object LOCK_STOCK = new Object();

    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final LibroDAO libroDAO = new LibroDAO();

    // registra el prestamo, descuenta el stock y deja el registro del historial
    public void registrarPrestamo(int idEstudiante, int idLibro) throws SQLException {
        synchronized (LOCK_STOCK) {
            try (Connection con = DatabaseConnection.conectar()) {
                con.setAutoCommit(false);
                try {
                    if (libroDAO.descontarStock(con, idLibro) == 0) {
                        throw new IllegalStateException("No hay stock disponible de este libro.");
                    }
                    Date hoy = new Date();
                    Date vencimiento = new Date(hoy.getTime() + DIAS_PRESTAMO * 24L * 60 * 60 * 1000);
                    prestamoDAO.insertar(con, new Prestamo(0, idEstudiante, idLibro, hoy, vencimiento, false));
                    con.commit();
                } catch (SQLException | RuntimeException e) {
                    con.rollback();
                    throw e;
                }
            }
        }
    }

    public void devolverPrestamo(int idPrestamo) throws SQLException {
        synchronized (LOCK_STOCK) {
            try (Connection con = DatabaseConnection.conectar()) {
                con.setAutoCommit(false);
                try {
                    Prestamo p = prestamoDAO.buscarPorId(con, idPrestamo);
                    if (p == null || p.isDevuelto()) {
                        throw new IllegalStateException("El prestamo no existe o fue devuelto.");
                    }
                    prestamoDAO.marcarDevuelto(con, idPrestamo);
                    libroDAO.aumentarStock(con, p.getIdLibro());
                    con.commit();

                } catch (SQLException | RuntimeException e) {
                    con.rollback();
                    throw e;
                }
            }
        }
    }

    public List<Object[]> listarPendientes(Integer idEstudiante) throws SQLException {
        return prestamoDAO.listarPendientes(idEstudiante);
    }
}
