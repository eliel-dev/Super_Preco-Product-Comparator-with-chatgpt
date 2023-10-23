package com.cedup.super_preco.model.dao;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.GrupoDTO;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GrupoDAO {
    public List<GrupoDTO> getGrupos() throws SQLException {
        List<GrupoDTO> grupos = new ArrayList<>();

        String sql = "SELECT * FROM grupo";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id = resultado.getInt("id_grupo");
                String nome = resultado.getString("nome");
                grupos.add(new GrupoDTO(id, nome));
            }
            return grupos;
        }
    }

    public GrupoDTO getGrupo(int id) throws SQLException {
        GrupoDTO grupo = null;
        String sql = "SELECT * FROM grupo where id_grupo = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    grupo = new GrupoDTO(id, nome);
                }
            }
            return grupo;
        }
    }

    public GrupoDTO postGrupo(GrupoDTO grupo) throws SQLException {
        String sql = "INSERT INTO grupo (nome) VALUES (?)";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, grupo.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                grupo.id= rs.getInt(1);
            }
        }
        return grupo;
    }

    public void deleteGrupo(int id) throws SQLException{
        String sql = "DELETE FROM grupo WHERE id_grupo=?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public GrupoDTO putGrupo(GrupoDTO grupo) throws SQLException{
        String sql = "UPDATE grupo SET nome=? WHERE id_grupo=?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, grupo.getNome());
            stmt.setInt(2, grupo.getId());

            stmt.executeUpdate();
        }
        return grupo;
    }

}
