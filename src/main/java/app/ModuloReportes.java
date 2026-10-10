package app;

import controlador.EstudianteController;
import controlador.ReporteController;
import model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ModuloReportes extends JPanel {
    private static final String MAS_PRESTADOS = "Libros mas prestados";
    private static final String HISTORIAL = "Historial por estudiante";
    private static final String EN_PRESTAMO = "Libros en prestamo";
    private static final String[] COLUMNAS_PRESTAMO = {"ID", "Estudiante", "Libro", "Prestamo", "Vencimiento", "Estado"};

    private final ReporteController reporteController = new ReporteController();
    private final EstudianteController estudianteController = new EstudianteController();

    private final JComboBox<String> cmbReporte = new JComboBox<>(new String[]{MAS_PRESTADOS, HISTORIAL, EN_PRESTAMO});
    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();

    private final DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public ModuloReportes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        superior.add(new JLabel("Reporte:"));
        superior.add(cmbReporte);
        superior.add(new JLabel("Estudiante:"));
        superior.add(cmbEstudiante);
        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generar());
        superior.add(btnGenerar);

        cmbReporte.addActionListener(e -> cmbEstudiante.setEnabled(HISTORIAL.equals(cmbReporte.getSelectedItem())));

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        cargarEstudiantes();
        generar();
    }

    private void cargarEstudiantes() {
        try {
            for (Estudiante e : estudianteController.listar()) {
                cmbEstudiante.addItem(e);
            }
            cmbEstudiante.setEnabled(false);
        } catch (SQLException ex) {
            mostrarError("Error al listar estudiantes: " + ex.getMessage());
        }
    }

    private void generar() {
        String tipo = (String) cmbReporte.getSelectedItem();
        try {
            if (MAS_PRESTADOS.equals(tipo)) {
                mostrar(reporteController.librosMasPrestados(), "Libro", "Veces prestado");
            } else if (HISTORIAL.equals(tipo)) {
                Estudiante est = (Estudiante) cmbEstudiante.getSelectedItem();
                if (est != null) {
                    mostrar(reporteController.historialEstudiante(est.getId()), COLUMNAS_PRESTAMO);
                }
            } else {
                mostrar(reporteController.librosEnPrestamo(), COLUMNAS_PRESTAMO);
            }
        } catch (SQLException ex) {
            mostrarError("Error al generar reporte: " + ex.getMessage());
        }
    }

    private void mostrar(List<Object[]> filas, String... columnas) {
        modelo.setDataVector(filas.toArray(new Object[0][]), columnas);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
