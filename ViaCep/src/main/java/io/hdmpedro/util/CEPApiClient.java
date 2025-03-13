package io.hdmpedro.util;

import io.hdmpedro.model.ApiData;
import io.hdmpedro.model.CepModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CEPApiClient {


    public CepModel consultarCEP(String API_URL, String cep, String usuario, String senha) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();

            String auth = usuario + ":" + senha;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"cep\":\"" + cep + "\"}";

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }


            if (connection.getResponseCode() == 200) {
                String jsonResponse = lerResposta(connection.getInputStream());
                salvarArquivoLocal(jsonResponse, cep);
                return parseJsonParaCEP(jsonResponse);
            }
            throw new IOException("Erro na requisição: " + connection.getResponseCode());

        } finally {
            if (connection != null) connection.disconnect();
        }
    }


    public String lerResposta(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    public void salvarArquivoLocal(String json, String cep) throws IOException {
        String fileName = "cep_" + cep.replace("-", "") + ".json";
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
        }
    }

    public CepModel parseJsonParaCEP(String json) {
        CepModel cep = new CepModel();
        json = json.replaceAll("[{}\"]", "");
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "cep": cep.setCep(value); break;
                    case "logradouro": cep.setLogradouro(value); break;
                    case "complemento": cep.setComplemento(value); break;
                    case "unidade": cep.setUnidade(value); break;
                    case "bairro": cep.setBairro(value); break;
                    case "localidade": cep.setLocalidade(value); break;
                    case "uf": cep.setUf(value); break;
                    case "estado": cep.setEstado(value); break;
                    case "regiao": cep.setRegiao(value); break;
                    case "ibge": cep.setIbge(value); break;
                    case "gia": cep.setGia(value); break;
                    case "ddd": cep.setDdd(value); break;
                    case "siafi": cep.setSiafi(value); break;



                }
            }
        }
        return cep;
    }
}