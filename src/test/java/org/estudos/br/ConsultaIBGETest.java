package org.estudos.br;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.estudos.br.ConsultaIBGE.consultarDistrito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class ConsultaIBGETest {
    private static final String ESTADOS_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";


    @Test
    @DisplayName("Teste para consulta única de um estado")
    public void testConsultarEstado() throws IOException {
        // Arrange
        String uf = "SP"; // Define o estado a ser consultado

        // Act
        String resposta = ConsultaIBGE.consultarEstado(uf); // Chama o método a ser testado

        // Assert
        // Verifica se a resposta não está vazia
        assert !resposta.isEmpty();

        // Verifica se o status code é 200 (OK)
        HttpURLConnection connection = (HttpURLConnection) new URL(ESTADOS_API_URL + uf).openConnection();
        int statusCode = connection.getResponseCode();
        assertEquals(200, statusCode, "O status code da resposta da API deve ser 200 (OK)");
    }


    private static final String DISTRITOS_API_URL = "https://example.com/distritos/";

    @Test
    @DisplayName("Teste para consultar o distrito")
    public void testconsultarDistrito() throws IOException {
        String jsonResposta = "[{\"id\":150010705,\"nome\":\"Abaetetuba\",\"municipio\":{\"id\":1500107,\"nome\":\"Abaetetuba\",\"microrregiao\":{\"id\":15011,\"nome\":\"Cametá\",\"mesorregiao\":{\"id\":1504,\"nome\":\"Nordeste Paraense\",\"UF\":{\"id\":15,\"sigla\":\"PA\",\"nome\":\"Pará\",\"regiao\":{\"id\":1,\"sigla\":\"N\",\"nome\":\"Norte\"}}}},\"regiao-imediata\":{\"id\":150003,\"nome\":\"Abaetetuba\",\"regiao-intermediaria\":{\"id\":1501,\"nome\":\"Belém\",\"UF\":{\"id\":15,\"sigla\":\"PA\",\"nome\":\"Pará\",\"regiao\":{\"id\":1,\"sigla\":\"N\",\"nome\":\"Norte\"}}}}}}]";

        HttpURLConnection connectionMock = mock(HttpURLConnection.class);
        when(connectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(jsonResposta.getBytes()));
        when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        String distritoJson = consultarDistrito(150010705);

        assertEquals(jsonResposta, distritoJson);
    }


    @Test
    @DisplayName("Teste para testar a opção de sair do menu")
    public void testSair() {
        InputStream in = new ByteArrayInputStream("3\n".getBytes());
        System.setIn(in);

        Main.main(new String[]{});

        assertTrue(true);
    }

    @Test
    @DisplayName("Teste para testar uma opção inválida do menu")
    public void testOpcaoInvalida() {
        InputStream in = new ByteArrayInputStream("4\n3\n".getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        // Verifica se a mensagem de erro foi impressa
        assertTrue(outContent.toString().contains("Opção inválida."));
    }
}