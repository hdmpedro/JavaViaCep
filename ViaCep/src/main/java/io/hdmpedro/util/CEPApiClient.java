package io.hdmpedro.util;

import com.google.gson.Gson;
import io.hdmpedro.model.ApiData;
import io.hdmpedro.model.CepModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CEPApiClient {
    private static final Gson gson = new Gson();

    public CepModel consultarCEP(String cep) throws IOException {
        HttpURLConnection connection = null;
        try {
            String apiUrl = "https://viacep.com.br/ws/" + cep + "/json/";
            URL url = new URL(apiUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String jsonResponse = lerResposta(connection.getInputStream());
                salvarArquivoLocal(jsonResponse, cep);
                return gson.fromJson(jsonResponse, CepModel.class);
            } else {
                throw new IOException("Erro na requisição. Código HTTP: " + status);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String lerResposta(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private void salvarArquivoLocal(String json, String cep) throws IOException {
        String sanitizedCep = cep.replaceAll("[^0-9]", "");
        String fileName = "cep_" + sanitizedCep + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        }
    }
}