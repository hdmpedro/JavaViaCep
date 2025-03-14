package io.hdmpedro.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ManipularBase {

    public static void abrirBase(){

        try(Connection conn = DatabaseConnection.getConnection()){
            if (conn != null){
                System.out.println("-=-= Conexão estabelecida com sucesso! =-=-");
            try(Statement stmt = conn.createStatement()){
                String query = "SELECT";
            }

            }
        }catch(SQLException ex){
            System.out.println("Erro na conexão ou consulta:");
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        abrirBase();

    }



}