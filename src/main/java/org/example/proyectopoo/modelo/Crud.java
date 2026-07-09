package org.example.proyectopoo.modelo;

import java.util.Map;

public interface Crud {

    public Map<Integer, Estudiante> seleccionarTodo();

    public Estudiante buscar(int id);

    public void insertar(Estudiante estudiante);

    public void actualizar(Estudiante estudiante);

    public void eliminar(int id);

}
