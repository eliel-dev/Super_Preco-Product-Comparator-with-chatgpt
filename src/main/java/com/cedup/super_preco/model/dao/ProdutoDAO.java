package com.cedup.super_preco.model.dao;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.ProdutoDTO;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO {
    public List<ProdutoDTO> getAll() throws SQLException {
        List<ProdutoDTO> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id_produto = resultado.getInt("id_produto");
                int id_mercado = resultado.getInt("id_mercado");
                int id_grupo = resultado.getInt("id_grupo");
                String nome = resultado.getString("nome");
                double preco = resultado.getDouble("preco");
                String link = resultado.getString("link");
                String link_img = resultado.getString("link_img");
                produtos.add(new ProdutoDTO(id_produto, id_mercado, id_grupo, nome, preco, link, link_img));
            }
            return produtos;
        }
    }

    public List<ProdutoDTO> getByMercado() throws SQLException {
        List<ProdutoDTO> produtos = new ArrayList<>();

        String sql = "SELECT id_produto, id_mercado, nome FROM produto";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id_produto = resultado.getInt("id_produto");
                int id_mercado = resultado.getInt("id_mercado");
                String nome = resultado.getString("nome");
                produtos.add(new ProdutoDTO(id_produto, id_mercado, nome));
            }
            return produtos;
        }
    }


    public ProdutoDTO getProduto(int id) throws SQLException {
        ProdutoDTO produto = null;
        String sql = "SELECT * FROM produto where id_produto = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id_produto = rs.getInt("id_produto");
                    int id_mercado = rs.getInt("id_mercado");
                    int id_grupo = rs.getInt("id_grupo");
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    String link = rs.getString("link");
                    String link_img = rs.getString("link_img");
                    produto = new ProdutoDTO(id_produto, id_mercado, id_grupo, nome, preco, link, link_img);
                }
            }
            return produto;
        }
    }

    public List<ProdutoDTO> getProdutosPorGrupo(int id_grupo) throws SQLException {
        List<ProdutoDTO> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto WHERE id_grupo = ?";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id_grupo);
            try(ResultSet resultado = stmt.executeQuery()) {
                while (resultado.next()) {
                    int id_produto = resultado.getInt("id_produto");
                    int id_mercado = resultado.getInt("id_mercado");
                    String nome = resultado.getString("nome");
                    double preco = resultado.getDouble("preco");
                    String link = resultado.getString("link");
                    String link_img = resultado.getString("link_img");
                    produtos.add(new ProdutoDTO(id_produto, id_mercado, id_grupo, nome, preco, link, link_img));
                }
            }
        }
        return produtos;
    }


    public ProdutoDTO postProduto(ProdutoDTO produto) throws SQLException {
        String sql = "INSERT INTO produto (id_mercado, id_grupo, nome, preco, link, link_img) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, produto.getId_mercado());
            stmt.setInt(2, produto.getId_grupo());
            stmt.setString(3, produto.getNome());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getLink());
            stmt.setString(6, produto.getLink_img());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                produto.id_produto= rs.getInt(1);
            }
        }
        return produto;
    }

    public void postProdutos(List<ProdutoDTO> produtos) throws SQLException {
        for (ProdutoDTO produto : produtos) {
            postProduto(produto);
        }
    }

    public void deleteProduto(int id) throws SQLException{
        String sql = "DELETE FROM produto WHERE id_produto = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public ProdutoDTO putProduto(ProdutoDTO produto) throws SQLException{

        String sql = "UPDATE produto SET id_mercado = ?, id_grupo = ?, nome = ?, preco = ?, link = ?, link_img = ? WHERE id_produto = ?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, produto.getId_mercado());
            stmt.setInt(2, produto.getId_grupo());
            stmt.setString(3, produto.getNome());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getLink());
            stmt.setString(6, produto.getLink_img());
            stmt.setInt(7, produto.getId_produto());

            stmt.executeUpdate();
        }
        return produto;
    }

}
