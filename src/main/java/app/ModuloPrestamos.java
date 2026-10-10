package app;

import controlador.*;
import model.Estudiante;
import model.Libro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ModuloPrestamos extends JPanel {
    private final PrestamoController prestamoController = new PrestamoController();
    private final LibroController libroController = new LibroController();
    private final EstudianteController estudianteController = new EstudianteController();

    private final Estudiante estudiante;
    private final boolean bibliotecario;

    private final JComboBox<Estudiante> cmbEstudiante = new JComboBox<>();
    private final JComboBox<Libro> cmbLibro = new JComboBox<>();
    private final JButton btnPrestar = new JButton("Registrar préstamo");

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Estudiante", "Libro", "Prestamo", "Vencimiento", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    public ModuloPrestamos(Usuario usuario, Estudiante estudiante) {
        this.estudiante = estudiante;
        this.bibliotecario = usuario.tieneGestionTotal();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (bibliotecario) {
            superior.add(new JLabel("Estudiante:"));
            superior.add(cmbEstudiante);
        } else {
            superior.add(new JLabel("Estudiante: " + estudiante.getNombre()));
        }
        superior.add(new JLabel("Libro:"));
        superior.add(cmbLibro);
        btnPrestar.addActionListener(e -> prestar());
        superior.add(btnPrestar);

        JButton btnDevolver = new JButton("Registrar devolucion");
        btnDevolver.addActionListener(e -> devolver());
        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inferior.add(btnDevolver);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(inferior, BorderLayout.SOUTH);
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            cmbLibro.removeAllItems();
            for (Libro l : libroController.listar()) {
                cmbLibro.addItem(l);
            }
            if (bibliotecario) {
                cmbEstudiante.removeAllItems();
                for (Estudiante e : estudianteController.listar()) {
                    cmbEstudiante.addItem(e);
                }
            }
            // bibliotecario ve todos, estudiante solo los suyos
            Integer idEstudiante = bibliotecario ? null : estudiante.getId();
            List<Object[]> filas = prestamoController.listarPendientes(idEstudiante);
            modelo.setDataVector(filas.toArray(new Object[0][]),
                    new Object[]{"ID", "Estudiante", "Libro", "Prestamo", "Vencimiento", "Estado"});
        } catch (SQLException ex) {
            mostrarError("Error de base de datos: " + ex.getMessage());
        }
    }

    private void prestar() {
        Libro libro = (Libro) cmbLibro.getSelectedItem();
        Estudiante estudiante1 = bibliotecario ? (Estudiante) cmbEstudiante.getSelectedItem() : estudiante;
        if (libro == null || estudiante1 == null) {
            mostrarError("Seleccione estudiante y libro.");
            return;
        }
        // empieza la operacion en un hilo aparte
        btnPrestar.setEnabled(false);
        Thread hilo = new Thread(new PrestamoTarea(prestamoController, estudiante1.getId(), libro.getId(), mensaje -> {
            btnPrestar.setEnabled(true);
            JOptionPane.showMessageDialog(this, mensaje);
            cargarDatos();
        }));
        hilo.start();
    }

    private void devolver() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un prestamo de la tabla.");
            return;
        }
        int idPrestamo = (int) modelo.getValueAt(fila, 0);
        try {
            prestamoController.devolverPrestamo(idPrestamo);
            JOptionPane.showMessageDialog(this, "Devolucion registrada correctamente.");
            cargarDatos();
        } catch (IllegalStateException | SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
