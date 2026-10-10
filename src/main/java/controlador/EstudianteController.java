package controlador;

import dao.EstudianteDAO;
import model.Estudiante;

import java.sql.SQLException;
import java.util.List;

public class EstudianteController {
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();

    public List<Estudiante> listar() throws SQLException {
        return estudianteDAO.listar();
    }

    public Estudiante buscarPorRut(String rut) throws SQLException {
        return estudianteDAO.buscarPorRut(rut);
    }

    public void registrar(Estudiante estudiante) throws SQLException {
        if (estudiante.getNombre().isEmpty() || estudiante.getRut().isEmpty()) {
            throw new IllegalArgumentException("Nombre y RUT son obligatorios.");
        }
        estudianteDAO.insertar(estudiante);
    }
}
