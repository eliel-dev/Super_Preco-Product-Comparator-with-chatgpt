package com.cedup.super_preco.model.produto;

import com.cedup.super_preco.ConnectionSingleton;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class ProdutoDAO {

    public void addGrupo(ProdutoEntity grupo) throws SQLException {
        String sql = "INSERT INTO produto (id_produto, nome) VALUES (?, ?)";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, grupo.id_produto);
            stmt.setString(2, grupo.nome);
            stmt.executeUpdate();
        }
    }
}
