package com.trabalho.programacao;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        while(true) {
            LoginHandler.decidirCadastroOuLogin();

            if (Usuario.getUsuarioAtual().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Para usar o programa é necessário fazer seu cadastro/login");
                continue;
            }

            break;
        }

        ModosDeJogo[] opcoes = {
                ModosDeJogo.SUPER_FACIL,
                ModosDeJogo.FACIL,
                ModosDeJogo.MEDIO_FACIL,
                ModosDeJogo.MEDIO,
                ModosDeJogo.MEDIO_DIFICIL,
                ModosDeJogo.DIFICIL
        };

        ModosDeJogo modoEscolhido = (ModosDeJogo) JOptionPane.showInputDialog(null, "Qual a dificuldade desejada? ", "Dificuldade", JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        Usuario usuarioAtual = new Usuario(Usuario.getInfoUsuarioPeloNome(Usuario.getUsuarioAtual()));
        Jogo jogo = new Jogo(modoEscolhido, usuarioAtual.getNome());
    }
}
