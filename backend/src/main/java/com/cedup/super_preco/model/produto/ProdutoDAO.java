package com.cedup.super_preco.model.produto;

import com.cedup.super_preco.ConnectionSingleton;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProdutoDAO {

    public ProdutoEntity insertGrupo(ProdutoEntity grupo) throws SQLException {
        String sql = "INSERT INTO produto (id_produto, nome) VALUES (?, ?)";
        try (PreparedStatement stmt = ConnectionSingleton.getConnection().prepareStatement(sql)) {
            stmt.setString(1, grupo.id_produto);
            stmt.setString(2, grupo.nome);
            stmt.executeUpdate();
        }
        return grupo;
    }

}
