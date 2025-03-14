package io.hdmpedro.util;

import io.hdmpedro.model.CepModel;
import io.hdmpedro.repository.CEPDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class func {

   public static void menuPrincipal() throws SQLException, IOException, InterruptedException {
    Scanner sc = new Scanner(System.in); // Movido para fora do loop
    CEPDao dao = new CEPDao(DatabaseConnection.getConnection()); // Movido para fora do loop
    
    while(true) {
        CEPApiClient api = new CEPApiClient();
        CepModel cp = new CepModel();

        System.out.println("\nDeseja consultar pelo número do CEP ou Endereço?");
        System.out.println("CEP[1]");
        System.out.println("Endereço[2]");
        System.out.println("Sair[3]"); // Adicionada opção de saída

        int input;
        try {
            input = sc.nextInt();
            sc.nextLine(); // Limpa o buffer do teclado
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite 1 ou 2.");
            sc.nextLine(); // Limpa entrada inválida
            continue;
        }

        if(input == 1){
            System.out.println("Digite o número do CEP (apenas dígitos):");
            String cep = sc.nextLine().trim();

            try {
                cp = api.consultarCEP(cep);
                dao.salvarCEP(cp);
                System.out.println("Consulta realizada com sucesso. Dados salvos:");
                System.out.println(cp);
            } catch (Exception e) {
                System.out.println("Erro na consulta: " + e.getMessage());
            }

        } else if (input == 2) {
            System.out.println("Digite o UF:");
            String uf = sc.nextLine().toUpperCase().trim();

            System.out.println("Digite a cidade:");
            String cidade = sc.nextLine().trim();

            System.out.println("Digite o nome da rua:");
            String rua = sc.nextLine().trim(); 

            try {
                List<CepModel> resultados = api.consultarPorEndereco(uf, cidade, rua);
                if(resultados.isEmpty()) {
                    System.out.println("Nenhum resultado encontrado.");
                } else {
                    for(CepModel resultado : resultados) {
                        dao.salvarCEP(resultado); 
                    }
                    System.out.println(resultados.size() + " resultados encontrados e salvos:");
                    resultados.forEach(System.out::println);
                }
            } catch (Exception e) {
                System.out.println("Erro na consulta: " + e.getMessage());
            }

        } else if(input == 3) {
            System.out.println("Saindo do sistema...");
            break; // Sai do loop
        } else {
            System.out.println("Opção inválida! Tente novamente.");
        }
    }
    sc.close(); 
}
