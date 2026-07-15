package org.example.proyectopoo.modelo;

import java.util.List;
import java.util.Map;

public interface Crud {

    public List<Estudiante> seleccionarTodo();

    public Estudiante buscar(String opcion);

    public void insertar(Estudiante estudiante);

    public void actualizar(Estudiante estudiante);

    public void eliminar(Estudiante estudiante);

}
