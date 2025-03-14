package io.hdmpedro.repository;

import io.hdmpedro.model.CepModel;
import java.sql.*;

public class CEPDao {
    private final Connection connection;

    public CEPDao(Connection connection) {
        this.connection = connection;
    }

    public void salvarCEP(CepModel cep) throws SQLException {
        String sql = "INSERT INTO buscas (" +
                "cep, logradouro, complemento, unidade, bairro, " +
                "localidade, uf, estado, regiao, ibge, gia, ddd, siafi" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cep.getCep());
            stmt.setString(2, cep.getLogradouro());
            stmt.setString(3, cep.getComplemento());
            stmt.setString(4, cep.getUnidade());
            stmt.setString(5, cep.getBairro());
            stmt.setString(6, cep.getLocalidade());
            stmt.setString(7, cep.getUf());
            stmt.setString(8, cep.getEstado());
            stmt.setString(9, cep.getRegiao());
            stmt.setString(10, cep.getIbge());
            stmt.setString(11, cep.getGia());
            stmt.setString(12, cep.getDdd());
            stmt.setString(13, cep.getSiafi());

            stmt.executeUpdate();
        }
    }

    public boolean cepExiste(String cep) throws SQLException {
        String sql = "SELECT COUNT(*) FROM buscas WHERE cep = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cep);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}