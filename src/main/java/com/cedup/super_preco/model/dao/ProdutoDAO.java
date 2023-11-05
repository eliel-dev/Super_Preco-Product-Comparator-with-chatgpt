package com.cedup.super_preco.model.dao;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.ProdutoDTO;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {
    public List<ProdutoDTO> getGrupos() throws SQLException {
        List<ProdutoDTO> grupos = new ArrayList<>();

        String sql = "SELECT * FROM produto";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                String id = resultado.getString("id_produto");
                String nome = resultado.getString("nome");
                grupos.add(new ProdutoDTO(id, nome));
            }
            return grupos;
        }
    }

    public ProdutoDTO getGrupo(String id) throws SQLException {
        ProdutoDTO grupo = null;
        String sql = "SELECT * FROM produto where id_produto = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    grupo = new ProdutoDTO(id, nome);
                }
            }
            return grupo;
        }
    }

    public ProdutoDTO insertGrupo(ProdutoDTO grupo) throws SQLException {
        String sql = "INSERT INTO produto (id_produto, nome) VALUES (?, ?)";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, grupo.getId());
            stmt.setString(2, grupo.getNome());
            stmt.executeUpdate();
        }
        return grupo;
    }

    public void deleteGrupo(String id) throws SQLException{
        String sql = "DELETE FROM produto WHERE id_produto=?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public ProdutoDTO putGrupo(ProdutoDTO grupo) throws SQLException{
        String sql = "UPDATE produto SET nome=? WHERE id_produto=?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, grupo.getNome());
            stmt.setString(2, grupo.getId());

            stmt.executeUpdate();
        }
        return grupo;
    }

}
