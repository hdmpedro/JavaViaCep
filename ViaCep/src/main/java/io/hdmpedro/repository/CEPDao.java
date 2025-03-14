package io.hdmpedro.repository;

import io.hdmpedro.model.CepModel;
import io.hdmpedro.model.CepModel;

import java.sql.*;

public class CEPDao {
    private Connection connection;

    public CEPDao(Connection connection) {
        this.connection = connection;
    }

    public void salvarCEP(CepModel cep) throws SQLException {
        String sql = "INSERT INTO ceps (cep, logradouro, complemento, bairro, localidade, uf, ibge, gia, ddd, siafi) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cep.getCep());
            stmt.setString(2, cep.getLogradouro());
            stmt.setString(3, cep.getComplemento());
            stmt.setString(4, cep.getBairro());
            stmt.setString(5, cep.getLocalidade());
            stmt.setString(6, cep.getUf());
            stmt.setString(7, cep.getIbge());
            stmt.setString(8, cep.getGia());
            stmt.setString(9, cep.getDdd());
            stmt.setString(10, cep.getSiafi());

            stmt.executeUpdate();
        }
    }
}