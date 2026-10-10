package app;

import controlador.AutenticacionController;
import controlador.EstudianteController;
import model.Estudiante;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ModuloAutenticacion extends JFrame {
    private final AutenticacionController controlador = new AutenticacionController();
    private final EstudianteController estudianteController = new EstudianteController();
    private final JTextField txtRut = new JTextField(15);
    private final JPasswordField txtContrasena = new JPasswordField(15);

    public ModuloAutenticacion() {
        setTitle("Biblioteca Escolar - Sistema de gestion de libros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        form.add(new JLabel("RUT:"));
        form.add(txtRut);
        form.add(new JLabel("Contraseña:"));
        form.add(txtContrasena);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> ingresar());
        getRootPane().setDefaultButton(btnIngresar);
        form.add(new JLabel());
        form.add(btnIngresar);

        add(form);
        pack();
        setLocationRelativeTo(null);
    }

    private void ingresar() {
        try {
            Usuario usuario = controlador.iniciarSesion(txtRut.getText(), new String(txtContrasena.getPassword()));
            if (usuario == null) {
                mostrarError("RUT o contraseña incorrectos.");
                return;
            }
            Estudiante estudiante = null;
            if (usuario.getRol().equals("estudiante")) {
                estudiante = estudianteController.buscarPorRut(usuario.getRut());
                if (estudiante == null) {
                    mostrarError("Este usuario no esta registrado como estudiante.");
                    return;
                }
            }
            new ModuloBiblioteca(usuario, estudiante).setVisible(true);
            dispose();
        } catch (IllegalArgumentException | SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
