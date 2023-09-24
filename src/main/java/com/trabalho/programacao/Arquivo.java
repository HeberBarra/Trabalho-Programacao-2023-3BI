package com.trabalho.programacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Arquivo {

    private static final String CAMINHO_ARQUIVO = "src/main/resources/users.csv";

    private static void criarArquivoCSV()  {
        File arquivo = new File(CAMINHO_ARQUIVO);

        try {
            var resultado = arquivo.createNewFile();
            System.out.println("Arquivo criado: " + resultado);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void adicionarAoArquivo(HashMap<String, String> usuario) {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) { criarArquivoCSV(); }

    }

    public static void lerArquivoCSV() {
        Scanner lerArquivo;
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) { criarArquivoCSV(); }

        try {
            lerArquivo = new Scanner(arquivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        lerArquivo.useDelimiter(";");

        while (lerArquivo.hasNext()) {
            System.out.println(lerArquivo.next());
        }
    }
}
