package app;

import controlador.EstudianteController;
import model.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class ModuloEstudiantes extends JPanel {
    private final EstudianteController controlador = new EstudianteController();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "RUT", "Curso", "Correo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtRut = new JTextField(15);
    private final JTextField txtCurso = new JTextField(15);
    private final JTextField txtCorreo = new JTextField(15);

    public ModuloEstudiantes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("RUT:"));
        form.add(txtRut);
        form.add(new JLabel("Curso:"));
        form.add(txtCurso);
        form.add(new JLabel("Correo:"));
        form.add(txtCorreo);
        JButton btnRegistrar = new JButton("Registrar estudiante");
        btnRegistrar.addActionListener(e -> registrar());
        form.add(new JLabel());
        form.add(btnRegistrar);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);
        cargarTabla();
    }

    private void registrar() {
        try {
            controlador.registrar(new Estudiante(
                    0,
                    txtNombre.getText().trim(),
                    txtRut.getText().trim(),
                    txtCurso.getText().trim(),
                    txtCorreo.getText().trim())
            );
            txtNombre.setText("");
            txtRut.setText("");
            txtCurso.setText("");
            txtCorreo.setText("");
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Estudiante registrado correctamente.");
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            for (Estudiante e : controlador.listar()) {
                modelo.addRow(new Object[]{e.getId(), e.getNombre(), e.getRut(), e.getCurso(), e.getCorreo()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar estudiantes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
