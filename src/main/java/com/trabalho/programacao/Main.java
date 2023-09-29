package com.trabalho.programacao;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        while(true) {
            LoginHandler.decidirCadastroOuLogin();

            if (Usuario.getUsuarioAtual().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Para usar o programa é necessário fazer seu cadastro/login");
                continue;
            }

            break;
        }

        Usuario.setUsuarioAtual("Heber");
        Usuario usuarioAtual = new Usuario(Usuario.getInfoUsuarioPeloNome(Usuario.getUsuarioAtual()));
        Jogo jogo = new Jogo(ModosDeJogo.DIFICIL, usuarioAtual.getNome());
    }
}
