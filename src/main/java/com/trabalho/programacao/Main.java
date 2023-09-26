package com.trabalho.programacao;

public class Main {

    public static void main(String[] args) {

        Arquivo.criarArquivoCSV();
        Usuario usuario = null;
        var usuariosRegistrados = Arquivo.lerArquivoCSV();

        if (usuariosRegistrados.isEmpty()) {
            LoginHandler.primeiroCadastro();
        }

        usuariosRegistrados = Arquivo.lerArquivoCSV();
    }
}
