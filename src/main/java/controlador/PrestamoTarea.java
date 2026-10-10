package controlador;

import javax.swing.SwingUtilities;
import java.sql.SQLException;
import java.util.function.Consumer;


public class PrestamoTarea implements Runnable {
    private final PrestamoController controlador;
    private final int idEstudiante;
    private final int idLibro;
    private final Consumer<String> alTerminar;

    public PrestamoTarea(PrestamoController controlador, int idEstudiante, int idLibro, Consumer<String> alTerminar) {
        this.controlador = controlador;
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.alTerminar = alTerminar;
    }

    // ejecuta el prestamo en un hilo para no bloquear la interfaz
    @Override
    public void run() {
        String mensaje;
        try {
            controlador.registrarPrestamo(idEstudiante, idLibro);
            mensaje = "Prestamo registrado correctamente.";
        } catch (IllegalStateException e) {
            mensaje = e.getMessage();
        } catch (SQLException e) {
            mensaje = "Error de base de datos: " + e.getMessage();
        }
        String resultado = mensaje;
        SwingUtilities.invokeLater(() -> alTerminar.accept(resultado));
    }
}
