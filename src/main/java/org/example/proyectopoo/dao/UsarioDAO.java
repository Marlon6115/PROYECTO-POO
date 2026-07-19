package org.example.proyectopoo.dao;

import org.example.proyectopoo.conexion.Conexion;
import org.example.proyectopoo.modelo.Usuario;
import org.example.proyectopoo.util.Alertas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UsarioDAO {
    private Connection conn;

    public UsarioDAO(){
        try {
            conn = Conexion.getConnection();
        } catch (Exception e) {
            Alertas.error("Conexión", "Error de conexión: " + e.getMessage());
        }
    }
    public void nuevo(Usuario usuario){
        String sql = "insert into usuarios(username, password, rol) values(?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRol());
            ps.executeUpdate();
        }catch (SQLException e){
            Alertas.error("ERROR", "Error al crear usuario: " + e.getMessage());
        }
    }
    public Usuario buscarUsuario(Usuario usuario) {
        String sqlUser = "SELECT * FROM usuarios WHERE username = ? AND rol = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sqlUser);
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getRol());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean validacion = BCrypt.checkpw(usuario.getPassword(), rs.getString("password"));
                if (validacion) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            null,
                            rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            Alertas.error("ERROR", "Error al buscar usuario: " + e.getMessage());
            return null;
        }
        return null;
    }
}