package com.trabalho.programacao;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LoginHandler {

    private static final ArrayList<LinkedHashMap<String, String>> usuariosRegistrados = Arquivo.lerArquivoCSV();

    public static void decidirCadastroOuLogin() {
        String[] opcoes = {"Login", "Cadastro", "Cancelar"};

        while (true) {
            if (usuariosRegistrados.isEmpty()) {
                int escolha = JOptionPane.showConfirmDialog(null, "Nenhum usuário encontrado; será necessário fazer o seu cadastro", "Cadastro Necessário", JOptionPane.OK_CANCEL_OPTION);

                if (escolha == -1 || escolha == 0) {
                    if (UtilsComunsInput.deveCancelarOperacao()) {
                        JOptionPane.showConfirmDialog(null, "Operação necessária cancelada. Finalizando o programa...");
                        System.exit(0);
                    }
                }

                primeiroCadastro();
                return;
            }

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Qual opção desejada?",
                    "Login ou Cadastro",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            switch (escolha) {
                case 0 -> {
                    login();
                    return;
                }
                case 1 -> {
                    cadastro();
                    return;
                }
                case -1, 2 -> {
                    if (UtilsComunsInput.deveCancelarOperacao()) {
                        JOptionPane.showMessageDialog(null, "Operação necessária cancelada. Finalizando o programa...");
                        System.exit(0);
                    }
                }
            }
        }
    }

    // Métodos para gerenciar cadastros

    public static void primeiroCadastro() {
        JOptionPane.showMessageDialog(null, "Nenhum usuário foi encontrado; será necessário fazer o seu cadastro.");
        while (true) {
            if (!cadastro()) {
                var escolha = JOptionPane.showConfirmDialog(null, "Para utilizar o programa é necessário criar um usuário. Deseja sair? ", "Sair?", JOptionPane.YES_NO_OPTION);

                if (escolha == 0) {
                    JOptionPane.showMessageDialog(null, "Finalizando programa... Vote sempre!");
                    System.exit(0);
                }

                continue;
            }

            return;
        }
    }

    public static boolean cadastro() {
        Usuario usuario;
        JOptionPane.showMessageDialog(null, "Por favor realize seu cadastro.");
        while (true) {
            usuario = new Usuario(new CriarUsuario().usuario);

            if (!usuario.infoUsuario.isEmpty()) {
                break;
            }

            var escolha = JOptionPane.showConfirmDialog(null, "Deseja cancelar o processo de cadastro? ", "Cancelar?", JOptionPane.YES_NO_OPTION);

            if (escolha == 0) {
                JOptionPane.showMessageDialog(null, "Cadastro cancelado.");
                return false;
            }

            JOptionPane.showMessageDialog(null, "Por favor realize seu cadastro.");
        }

        Arquivo.adicionarUsuarioAoArquivo(usuario.infoUsuario);
        Usuario.setUsuarioAtual(usuario.getNome());
        JOptionPane.showMessageDialog(null, "Usuário criado e salvo.");
        return true;
    }

    // Métodos para gerenciar o login
    public static void login() {
        String nome;

        while (true) {
            nome = inputNome();

            if (nome.equals(UtilsComunsInput.CANCELAR_OPERACAO)) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }


            String senha = verificarSenha(nome);

            if (senha.equals(UtilsComunsInput.VALOR_REFAZER)) {
                continue;
            }

            if (senha.equals(UtilsComunsInput.CANCELAR_OPERACAO)) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
                return;
            }

            break;
        }

        JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!");
        Usuario.setUsuarioAtual(nome);
    }

    private static String inputNome() {
        while (true) {
            String nome = UtilsComunsInput.pegarInputUsuario("Nome de usuário");

            if (nome.equals(UtilsComunsInput.CANCELAR_OPERACAO)) {
                return nome;
            }

            for (var usuario : usuariosRegistrados) {
                if (nome.equals(usuario.get("nome"))) return nome;
            }

            int escolha = JOptionPane.showConfirmDialog(
                    null,
                    "Nome de usuário não encontrado; Por tente novamente ou cancele",
                    "Aviso",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            switch (escolha) {
                case -1, 2 -> {
                    if (UtilsComunsInput.deveCancelarOperacao()) return UtilsComunsInput.CANCELAR_OPERACAO;
                }
            }
        }
    }

    private static String verificarSenha(String nome) {
        String senha = UtilsComunsInput.pegarInputUsuario("senha", false);

        if (senha.equals(UtilsComunsInput.CANCELAR_OPERACAO)) return senha;

        for (var usuario : usuariosRegistrados) {
            if (nome.equals(usuario.get("nome"))) {
                Usuario usuarioEscolhido = new Usuario(usuario);

                if (usuarioEscolhido.getHashSenha().equals(DataHash.dataHasher(senha, usuarioEscolhido.getSalt()))) {
                    return "";
                }

                String[] opcoes = {"Tentar novamente", "Mudar usuário", "Cancelar"};
                JOptionPane.showMessageDialog(null, "Senha incorreta!", "Aviso", JOptionPane.WARNING_MESSAGE);
                int escolha = JOptionPane.showOptionDialog(
                        null,
                        "O que deseja fazer? ",
                        "Pergunta",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]
                );


                switch (escolha) {
                    case 0 -> {
                    }
                    case 1 -> {
                        return UtilsComunsInput.VALOR_REFAZER;
                    }
                    default -> {
                        if (UtilsComunsInput.deveCancelarOperacao()) return UtilsComunsInput.CANCELAR_OPERACAO;
                    }
                }

                JOptionPane.showMessageDialog(null, "Por favor tente novamente.");
                break;
            }
        }

        return UtilsComunsInput.CANCELAR_OPERACAO;
    }
}
