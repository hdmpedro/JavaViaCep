Sistema que usa a API ViaCep para realizar consultas de CEP em Java 8.
Temos 2 modos de consulta possíveis, podendo ser feito pelo número do CEP, que retorna as informações do endereço, ou pelo próprio endereço (UF, Cidade & Logradouro) onde receberá uma lista com todos os CEPs deste endereço. 
Todos os dados consultados e recebidos são salvos internamente como um modelo da aplicação, localmente como arquivos json e em um banco de dados MySQL.
