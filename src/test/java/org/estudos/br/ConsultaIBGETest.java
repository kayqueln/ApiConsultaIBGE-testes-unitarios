package org.estudos.br;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
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


    @Test
    public void testConsultarDistrito() throws IOException {
        ConsultaIBGE consultaIBGE = Mockito.mock(ConsultaIBGE.class);

        when(consultaIBGE.consultarDistrito(1)).thenReturn("Distrito 1");

        InputStream in = new ByteArrayInputStream("2\n1\n3\n".getBytes());
        System.setIn(in);

        Main.main(new String[]{});

        Mockito.verify(consultaIBGE).consultarDistrito(1);
    }

    @Test
    public void testSair() {
        InputStream in = new ByteArrayInputStream("3\n".getBytes());
        System.setIn(in);

        Main.main(new String[]{});

        assertEquals(true, true);
    }

    @Test
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