package io.hdmpedro.app;
import io.hdmpedro.model.CepModel;
import io.hdmpedro.util.*;
import io.hdmpedro.model.ApiData;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String cep = "35560000";
        String API_URL = ApiData.API_URL.formatarUrl(cep);
        String user = "";
        String password = "";

        CEPApiClient api = new CEPApiClient();
        CepModel ab = new CepModel();

        CepModel model = api.consultarCEP(API_URL, cep, user, password);

        System.out.println(model);


    }

}
