package org.example.proyectopoo.modelo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Crud {

    public List<Estudiante> seleccionarTodo();

    public void insertar(Estudiante estudiante) throws SQLException;

    public void actualizar(Estudiante estudiante);

    public void eliminar(String cedula);

}
