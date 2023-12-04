package com.cedup.super_preco.model.produto;

import com.cedup.super_preco.ConnectionSingleton;
import com.cedup.super_preco.model.mercado.MercadoEntity;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class Produto_MercadoDAO {
    public List<Produto_MercadoEntity> getAll() throws SQLException {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto_mercado";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id_produto_mercado = rs.getInt("id_produto_mercado");
                MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                ProdutoEntity id_produto = new ProdutoEntity(rs.getString("id_produto"));
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                String link = rs.getString("link");
                String link_img = rs.getString("link_img");
                produtos.add(new Produto_MercadoEntity(id_produto_mercado, id_mercado, id_produto, nome, preco, link,
                        link_img));
            }
            return produtos;
        }
    }

    public List<Produto_MercadoEntity> autocomplete(String searchTerm) throws SQLException {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();

        String sql = "SELECT pm.* " +
                "FROM produto_mercado pm " +
                "INNER JOIN " +
                "(SELECT id_produto, MIN(id_produto_mercado) as id_produto_mercado " +
                "FROM produto_mercado " +
                "GROUP BY id_produto) subquery " +
                "ON pm.id_produto = subquery.id_produto " +
                "AND pm.id_produto_mercado = subquery.id_produto_mercado " +
                "WHERE pm.nome LIKE ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id_produto_mercado = rs.getInt("id_produto_mercado");
                MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                ProdutoEntity id_produto = new ProdutoEntity(rs.getString("id_produto"));
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                String link = rs.getString("link");
                String link_img = rs.getString("link_img");
                produtos.add(new Produto_MercadoEntity(id_produto_mercado, id_mercado,
                        id_produto, nome, preco, link, link_img));
            }
        }
        return produtos;
    }

    public List<Produto_MercadoEntity> getUniqueProdutos(int page, int size) throws SQLException {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();

        String sql = "SELECT pm.* " +
                "FROM produto_mercado pm " +
                "INNER JOIN " +
                "(SELECT id_produto, MIN(id_produto_mercado) as id_produto_mercado " +
                "FROM produto_mercado " +
                "GROUP BY id_produto) subquery " +
                "ON pm.id_produto = subquery.id_produto " +
                "AND pm.id_produto_mercado = subquery.id_produto_mercado " +
                "ORDER BY pm.id_produto LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, size);
            stmt.setInt(2, page * size);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id_produto_mercado = rs.getInt("id_produto_mercado");
                    MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                    ProdutoEntity id_produto = new ProdutoEntity(rs.getString("id_produto"));
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    String link = rs.getString("link");
                    String link_img = rs.getString("link_img");
                    produtos.add(new Produto_MercadoEntity(id_produto_mercado, id_mercado, id_produto, nome, preco, link,
                            link_img));
                }
            }
        }
        return produtos;
    }

    public List<Produto_MercadoEntity> getByMercado(int limit, int offset) throws SQLException {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();

        String sql = "SELECT id_produto_mercado, id_mercado, nome FROM produto_mercado ORDER BY nome LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id_produto_mercado = rs.getInt("id_produto_mercado");
                MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                String nome = rs.getString("nome");
                produtos.add(new Produto_MercadoEntity(id_produto_mercado, id_mercado, nome));
                // System.out.println(nome);
            }
            System.out.println("limit: " + limit);
            System.out.println("offset: " + offset);
            System.out.println("_____________________________________________________________________");
        }
        return produtos;
    }

    public int getTotalProdutos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto_mercado";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
    }

    public int getTotalGrupos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
    }

    public Produto_MercadoEntity getProduto(int id) throws SQLException {
        Produto_MercadoEntity produto = null;
        String sql = "SELECT * FROM produto_mercado where id_produto_mercado = ?";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id_produto_mercado = rs.getInt("id_produto_mercado");
                    MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                    ProdutoEntity id_produto = new ProdutoEntity(rs.getString("id_produto"));
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    String link = rs.getString("link");
                    String link_img = rs.getString("link_img");
                    produto = new Produto_MercadoEntity(id_produto_mercado, id_mercado, id_produto, nome, preco, link, link_img);
                }
            }
            return produto;
        }
    }

    public List<Produto_MercadoEntity> getProdutosPorGrupo(ProdutoEntity entity) throws SQLException {
        List<Produto_MercadoEntity> produtos = new ArrayList<>();

        String sql = "SELECT * FROM produto_mercado WHERE id_produto = ?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, entity.id_produto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id_produto_mercado = rs.getInt("id_produto_mercado");
                    MercadoEntity id_mercado = new MercadoEntity(rs.getInt("id_mercado"));
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    String link = rs.getString("link");
                    String link_img = rs.getString("link_img");
                    produtos.add(new Produto_MercadoEntity(id_produto_mercado, id_mercado, entity, nome, preco, link,
                            link_img));
                }
            }
        }
        return produtos;
    }

    public void addProduto(Produto_MercadoEntity produto) throws SQLException {
        String sql = "INSERT INTO produto_mercado (id_mercado, id_produto, nome, preco, link, link_img) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, produto.id_mercado.id_mercado);
            stmt.setString(2, produto.id_produto.id_produto);
            stmt.setString(3, produto.nome);
            stmt.setDouble(4, produto.preco);
            stmt.setString(5, produto.link);
            stmt.setString(6, produto.link_img);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                produto.id_produto_mercado = rs.getInt(1);
            }
        }
    }

    public void updateIdProduto(int idProdutoMercado, String idProduto) throws SQLException {
        String sql = "UPDATE produto_mercado SET id_produto = ? WHERE id_produto_mercado = ?";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, idProduto);
            stmt.setInt(2, idProdutoMercado);
            stmt.executeUpdate();
        }
    }
}
