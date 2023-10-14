package com.cedup.super_preco.model.dao;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.MercadoDTO;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MercadoDAO {
    public List<MercadoDTO> getMercados() throws SQLException {
        List<MercadoDTO> mercados = new ArrayList<>();

        String sql = "SELECT * FROM mercado";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id = resultado.getInt("id_mercado");
                String nome = resultado.getString("nome");
                mercados.add(new MercadoDTO(id, nome));
            }
            return mercados;
        }
    }

    public MercadoDTO getMercado(int id) throws SQLException {
        MercadoDTO mercado = null;
        String sql = "SELECT * FROM mercado where id_mercado = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    mercado = new MercadoDTO(id, nome);
                }
            }
            return mercado;
        }
    }

    public MercadoDTO postMercado(MercadoDTO mercado) throws SQLException {
        String sql = "INSERT INTO mercado (nome) VALUES (?)";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mercado.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                mercado.id= rs.getInt(1);
            }
        }
        return mercado;
    }

    public void deleteMercado(int id) throws SQLException{
        String sql = "DELETE FROM mercado WHERE id_mercado=?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public MercadoDTO putMercado(MercadoDTO mercado) throws SQLException{
        String sql = "UPDATE mercado SET nome=? WHERE id_mercado=?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, mercado.getNome());
            stmt.setInt(2, mercado.getId());

            stmt.executeUpdate();
        }
        return mercado;
    }
}
