package com.trabalho.programacao;


import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Arquivo {

    private static final String CAMINHO_RESOURCES = "src/main/resources/";
    private static final String CAMINHO_ARQUIVO = CAMINHO_RESOURCES + "users.csv";
    private static final File arquivoCSV = new File(CAMINHO_ARQUIVO);
    private static final Logger logger = Logger.getLogger(Arquivo.class.getName());
    private static final String CAMPOS_CSV = "nome;senha;salt;highscore;modo de jogo (hscore);quantidade de jogos;\n";

    protected static void criarArquivoCSV() {
        if (arquivoCSV.exists()) return;

        File pastaResources = new File(CAMINHO_RESOURCES);

        logger.log(Level.INFO, "Pasta resources criada: " + pastaResources.mkdir());

        try (FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO, false)) {
            logger.log(Level.INFO, "Arquivo users.csv criado: " + !arquivoCSV.createNewFile());
            fileWriter.write(CAMPOS_CSV);
        } catch (IOException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    protected static void adicionarUsuarioAoArquivo(HashMap<String, String> usuario) {
        try (FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO, true)) {
            StringBuilder linha = new StringBuilder();

            for (String value : usuario.values()) {
                linha.append(value).append(";");
            }

            linha.append('\n');

            fileWriter.append(linha.toString());

        } catch (IOException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public static void sobrescreverArquivo(ArrayList<LinkedHashMap<String, String>> usuarios) {
        try (FileWriter fileWriter = new FileWriter(arquivoCSV, false)) {
            fileWriter.write(CAMPOS_CSV);

            for (LinkedHashMap<String, String> usuario: usuarios) {
                String line = String.join(";", usuario.values()) + "\n";
                fileWriter.write(line);
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }
    }

    public static void atualizarArquivo(LinkedHashMap<String, String> infoUsuario) {
        if (infoUsuario.isEmpty()) return;

        var usuarios = lerArquivoCSV();

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).get("nome").equals(infoUsuario.get("nome"))) {
                usuarios.set(i, infoUsuario);
            }
        }

        sobrescreverArquivo(usuarios);
    }

    public static ArrayList<LinkedHashMap<String, String>> lerArquivoCSV() {
        var usuarios = new ArrayList<LinkedHashMap<String, String>>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String[] camposCSV = bufferedReader.readLine().split(";");
            String linhaRegistro;
            while ((linhaRegistro = bufferedReader.readLine()) != null) {
                LinkedHashMap<String, String> usuario = new LinkedHashMap<>();

                String[] infoUsuario = linhaRegistro.split(";");

                for (int i = 0; i < infoUsuario.length; i++) {
                    usuario.put(camposCSV[i], infoUsuario[i]);
                }

                usuarios.add(usuario);
            }

        } catch (IOException e) {
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
        }

        return usuarios;
    }
}
