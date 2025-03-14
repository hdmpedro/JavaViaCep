package io.hdmpedro.model;

public enum ApiData {

    API_URL("https://viacep.com.br/ws/%s/json/");


    private final String value;

    ApiData(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public String formatarUrl(String cep){
        return String.format(this.value, cep);
    }


}
