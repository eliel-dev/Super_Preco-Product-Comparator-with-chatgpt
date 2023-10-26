package com.cedup.super_preco.model.dao;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.Produto_MercadoDTO;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Produto_MercadoDAO {
    public List<Produto_MercadoDTO> getAll() throws SQLException {
        List<Produto_MercadoDTO> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto_mercado";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id_produto_mercado = resultado.getInt("id_produto_mercado");
                int id_mercado = resultado.getInt("id_mercado");
                int id_produto = resultado.getInt("id_produto");
                String nome = resultado.getString("nome");
                double preco = resultado.getDouble("preco");
                String link = resultado.getString("link");
                String link_img = resultado.getString("link_img");
                produtos.add(new Produto_MercadoDTO(id_produto_mercado, id_mercado, id_produto, nome, preco, link, link_img));
            }
            return produtos;
        }
    }

    public List<Produto_MercadoDTO> getByMercado() throws SQLException {
        List<Produto_MercadoDTO> produtos = new ArrayList<>();

        String sql = "SELECT id_produto_mercado, id_mercado, nome FROM produto_mercado";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                int id_produto_mercado = resultado.getInt("id_produto_mercado");
                int id_mercado = resultado.getInt("id_mercado");
                String nome = resultado.getString("nome");
                produtos.add(new Produto_MercadoDTO(id_produto_mercado, id_mercado, nome));
            }
            return produtos;
        }
    }

    public Produto_MercadoDTO getProduto(int id) throws SQLException {
        Produto_MercadoDTO produto = null;
        String sql = "SELECT * FROM produto_mercado where id_produto_mercado = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id_produto = rs.getInt("id_produto_mercado");
                    int id_mercado = rs.getInt("id_mercado");
                    int id_grupo = rs.getInt("id_produto");
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    String link = rs.getString("link");
                    String link_img = rs.getString("link_img");
                    produto = new Produto_MercadoDTO(id_produto, id_mercado, id_grupo, nome, preco, link, link_img);
                }
            }
            return produto;
        }
    }

    public List<Produto_MercadoDTO> getProdutosPorGrupo(int id_produto) throws SQLException {
        List<Produto_MercadoDTO> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto_mercado WHERE id_produto = ?";
        try(PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id_produto);
            try(ResultSet resultado = stmt.executeQuery()) {
                while (resultado.next()) {
                    int id_produto_mercado = resultado.getInt("id_produto_mercado");
                    int id_mercado = resultado.getInt("id_mercado");
                    String nome = resultado.getString("nome");
                    double preco = resultado.getDouble("preco");
                    String link = resultado.getString("link");
                    String link_img = resultado.getString("link_img");
                    produtos.add(new Produto_MercadoDTO(id_produto_mercado, id_mercado, id_produto, nome, preco, link, link_img));
                }
            }
        }
        return produtos;
    }

    public Produto_MercadoDTO postProduto(Produto_MercadoDTO produto) throws SQLException {
        String sql = "INSERT INTO produto_mercado (id_mercado, id_produto, nome, preco, link, link_img) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, produto.getId_mercado());
            stmt.setInt(2, produto.getId_produto());
            stmt.setString(3, produto.getNome());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getLink());
            stmt.setString(6, produto.getLink_img());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                produto.id_produto_mercado = rs.getInt(1);
            }
        }
        return produto;
    }

    public void postProdutos(List<Produto_MercadoDTO> produtos) throws SQLException {
        for (Produto_MercadoDTO produto : produtos) {
            postProduto(produto);
        }
    }

    public void deleteProduto(int id) throws SQLException{
        String sql = "DELETE FROM produto WHERE id_produto_mercado = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Produto_MercadoDTO putProduto(Produto_MercadoDTO produto) throws SQLException{

        String sql = "UPDATE produto_mercado SET id_mercado = ?, id_produto = ?, nome = ?, preco = ?, link = ?, link_img = ? WHERE id_produto_mercado = ?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, produto.getId_mercado());
            stmt.setInt(2, produto.getId_produto());
            stmt.setString(3, produto.getNome());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getLink());
            stmt.setString(6, produto.getLink_img());
            stmt.setInt(7, produto.getId_produto_mercado());

            stmt.executeUpdate();
        }
        return produto;
    }
}
