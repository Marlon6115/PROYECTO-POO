package org.example.proyectopoo.dao;

import org.example.proyectopoo.conexion.Conexion;
import org.example.proyectopoo.modelo.Crud;
import org.example.proyectopoo.modelo.Estudiante;
import org.example.proyectopoo.util.Alertas;

import java.sql.*;
import java.util.*;

public class EstudianteDAO implements Crud{

    private Connection conn;

    public EstudianteDAO() {
        try {
            conn = Conexion.getConnection();
        } catch (SQLException e) {
            Alertas.error("Conexión", "Error de conexión: " + e.getMessage());
        }
    }

    @Override
    public List<Estudiante> seleccionarTodo() {

        List<Estudiante> lista = new ArrayList<>();
        String sql = "select * from estudiantes";

        try(Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getInt("id"),
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("carrera")
                ));
            }
        }catch (SQLException e) {
            Alertas.error("ERROR", "Error al mostrar" + e.getMessage());
        }
        return lista;
    }

    @Override
    public void insertar(Estudiante estudiante) throws SQLException {
        String sql = "INSERT INTO estudiantes(cedula, nombre, apellido, correo, carrera) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, estudiante.getCedula());
        ps.setString(2, estudiante.getNombre());
        ps.setString(3, estudiante.getApellido());
        ps.setString(4, estudiante.getCorreo());
        ps.setString(5, estudiante.getCarrera());
        ps.executeUpdate();
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        String sql = "update estudiantes set nombre=?, apellido=?, correo=?, carrera=? where cedula=?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getCorreo());
            ps.setString(4, estudiante.getCarrera());
            ps.setString(5, estudiante.getCedula());
            ps.executeUpdate();
        } catch (SQLException e) {
            Alertas.error("ERROR", "Error al actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String cedula) {
        String sql = "delete from estudiantes where cedula = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, cedula);
            ps.executeUpdate();
        } catch (Exception e) {
            Alertas.error("ERROR", "Error al eliminar: " + e.getMessage());
        }
    }

    public boolean existeCedula(String cedula) {

        String sql = "SELECT COUNT(*) FROM estudiantes WHERE cedula = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            Alertas.error("ERROR", e.getMessage());
        }

        return false;
    }



}