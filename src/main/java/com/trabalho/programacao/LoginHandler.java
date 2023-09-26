package com.trabalho.programacao;

import javax.swing.JOptionPane;

public class LoginHandler {

    // Métodos para gerenciar cadastros

    public static void primeiroCadastro() {
        JOptionPane.showMessageDialog(null, "Nenhum usuário foi encontrado; será necessário fazer o seu cadastro.");
        while (true){
            if (!cadastro()) {
                var escolha = JOptionPane.showConfirmDialog(null, "Para utilizar o programa é necessário criar um usuário. Deseja sair? ", "Sair?", JOptionPane.YES_NO_OPTION);

                if (escolha == 0) {
                    JOptionPane.showMessageDialog(null, "Finalizando programa... Volte sempre!");
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

            if (!usuario.infoUsuario.isEmpty()) { break; }

            var escolha = JOptionPane.showConfirmDialog(null,  "Deseja cancelar o processo de cadastro? ", "Cancelar?", JOptionPane.YES_NO_OPTION);

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

}
