package app;

import controlador.LibroController;
import model.Categoria;
import model.Estudiante;
import model.Libro;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuloBiblioteca extends JFrame {
    private final LibroController libroController = new LibroController();
    private final boolean bibliotecario;

    private List<Libro> libros = new ArrayList<>();
    private List<Categoria> categorias = new ArrayList<>();

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Titulo", "Autor", "ISBN", "Editorial", "Stock", "Categoria"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);

    private final JTextField txtTitulo = new JTextField(20);
    private final JTextField txtAutor = new JTextField(20);
    private final JTextField txtIsbn = new JTextField(20);
    private final JTextField txtEditorial = new JTextField(20);
    private final JTextField txtStock = new JTextField(20);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();

    public ModuloBiblioteca(Usuario usuario, Estudiante estudiante) {
        setTitle("Biblioteca Escolar - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        this.bibliotecario = usuario.getRol().equals("bibliotecario");

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Libros", crearPanelLibros());
        pestanas.addTab("Prestamos y devoluciones", new ModuloPrestamos(usuario, estudiante));

        if (bibliotecario) {
            pestanas.addTab("Estudiantes", new ModuloEstudiantes());
            pestanas.addTab("Reportes", new ModuloReportes());
        }

        add(pestanas);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        cargarDatos();
    }

    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        //solo si bibliotecario es true puede modificar la data
        if (bibliotecario) {
            JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
            form.add(new JLabel("Titulo:"));
            form.add(txtTitulo);
            form.add(new JLabel("Autor:"));
            form.add(txtAutor);
            form.add(new JLabel("ISBN:"));
            form.add(txtIsbn);
            form.add(new JLabel("Editorial:"));
            form.add(txtEditorial);
            form.add(new JLabel("Stock:"));
            form.add(txtStock);
            form.add(new JLabel("Categoria:"));
            form.add(cmbCategoria);
            panel.add(form, BorderLayout.NORTH);

            JButton btnNuevo = new JButton("Nuevo");
            JButton btnGuardar = new JButton("Guardar");
            JButton btnEliminar = new JButton("Eliminar");
            btnNuevo.addActionListener(e -> limpiarFormulario());
            btnGuardar.addActionListener(e -> guardar());
            btnEliminar.addActionListener(e -> eliminar());
            JPanel botones = new JPanel();
            botones.add(btnNuevo);
            botones.add(btnGuardar);
            botones.add(btnEliminar);
            panel.add(botones, BorderLayout.SOUTH);

            tabla.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                    cargarFormulario(libros.get(tabla.getSelectedRow()));
                }
            });
        }
        return panel;
    }

    private void cargarDatos() {
        try {
            libros = libroController.listar();
            categorias = libroController.listarCategorias();

            cmbCategoria.removeAllItems();
            for (Categoria c : categorias) {
                cmbCategoria.addItem(c);
            }

            modelo.setRowCount(0);
            for (Libro l : libros) {
                modelo.addRow(new Object[]{l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn(),
                        l.getEditorial(), l.getStock(), nombreCategoria(l.getIdCategoria())});
            }
        } catch (SQLException ex) {
            mostrarError("Error al obtener los libros: " + ex.getMessage());
        }
    }

    private String nombreCategoria(int idCategoria) {
        for (Categoria c : categorias) {
            if (c.getId() == idCategoria) {
                return c.getNombre();
            }
        }
        return "";
    }

    private void cargarFormulario(Libro l) {
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtIsbn.setText(l.getIsbn());
        txtEditorial.setText(l.getEditorial());
        txtStock.setText(String.valueOf(l.getStock()));
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).getId() == l.getIdCategoria()) {
                cmbCategoria.setSelectedIndex(i);
            }
        }
    }

    private void limpiarFormulario() {
        tabla.clearSelection();
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
    }

    private void guardar() {
        try {
            int stock;
            try {
                stock = Integer.parseInt(txtStock.getText().trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("El stock debe ser un numero entero.");
            }
            int fila = tabla.getSelectedRow();
            int id = fila >= 0 ? libros.get(fila).getId() : 0;
            Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();
            if (categoria == null) {
                throw new IllegalArgumentException("Seleccione una categoria.");
            }
            Libro libro = new Libro(id, txtTitulo.getText().trim(), txtAutor.getText().trim(),
                    txtIsbn.getText().trim(), txtEditorial.getText().trim(), stock, categoria.getId());
            libroController.guardar(libro);
            limpiarFormulario();
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Libro guardado correctamente.");
        } catch (IllegalArgumentException | SQLException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            mostrarError("Seleccione un libro de la tabla");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "Eliminar el libro seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            libroController.eliminar(libros.get(fila).getId());
            limpiarFormulario();
            cargarDatos();
        } catch (SQLException ex) {
            mostrarError("No se pudo eliminar el libro (puede tener prestamos asociados). " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
