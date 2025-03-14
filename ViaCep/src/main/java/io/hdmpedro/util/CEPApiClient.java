package io.hdmpedro.util;

import com.google.gson.reflect.TypeToken;
import io.hdmpedro.model.ApiData;
import io.hdmpedro.model.CepModel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.gson.Gson;

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

    public List<CepModel> consultarPorEndereco(String uf, String cidade, String logradouro) throws IOException {
        HttpURLConnection connection = null;
        try {
            String ufCodificado = URLEncoder.encode(uf, StandardCharsets.UTF_8.name())
                    .replace("+", "%20");

            String cidadeCodificado = URLEncoder.encode(cidade, StandardCharsets.UTF_8.name())
                    .replace("+", "%20");

            String logradouroCodificado = URLEncoder.encode(logradouro, StandardCharsets.UTF_8.name())
                    .replace("+", "%20");

            String apiUrl = String.format("https://viacep.com.br/ws/%s/%s/%s/json/",
                    ufCodificado,
                    cidadeCodificado,
                    logradouroCodificado);

            URL url = new URL(apiUrl);
            System.out.println("URL da consulta: " + apiUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                String jsonResponse = lerResposta(connection.getInputStream());
                salvarArquivoLocalEndereco(jsonResponse, uf, cidade, logradouro);

                return gson.fromJson(jsonResponse, new TypeToken<ArrayList<CepModel>>() {
                }.getType());
            } else {
                String errorResponse = lerResposta(connection.getErrorStream());
                throw new IOException("Erro na requisição. Código HTTP: " + status + "\nResposta: " + errorResponse);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


    }

    private void salvarArquivoLocalEndereco(String json, String uf, String cidade, String logradouro) throws IOException {
        String sanitizedUf = uf.replaceAll("[^a-zA-Z0-9]", "_");
        String sanitizedCidade = cidade.replaceAll("[^a-zA-Z0-9]", "_");
        String sanitizedLogradouro = logradouro.replaceAll("[^a-zA-Z0-9]", "_");

        String fileName = String.format("endereco_%s_%s_%s.json", sanitizedUf, sanitizedCidade, sanitizedLogradouro);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        }
    }

}