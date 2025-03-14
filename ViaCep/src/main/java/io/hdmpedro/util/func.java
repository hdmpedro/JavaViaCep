package io.hdmpedro.util;

import io.hdmpedro.model.CepModel;
import io.hdmpedro.repository.CEPDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class func {

    public static void menuPrincipal() throws SQLException, IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        CEPApiClient api = new CEPApiClient();
        CEPDao dao = new CEPDao(DatabaseConnection.getConnection());
        CepModel modelCep = new CepModel();

        while (true) {
            System.out.println("Digite o CEP que deseja consultar: ");
            String cep = sc.nextLine();

            boolean cepExiste = dao.cepExiste(cep);

            if (cepExiste) {
                System.out.println("O cep informado já existe no banco de dados, confira a consulta.");
            } else {
                CepModel model = api.consultarCEP(cep);
                dao.salvarCEP(model);
                System.out.println("Consulta realizada, os dados foram salvos localmente e no banco de dados com sucesso!");
                System.err.println(model);
                TimeUnit.SECONDS.sleep(1);
            }


        }


    }


}
