package com.trabalho.programacao;

import javax.swing.*;

public class Main {

    private static ModosDeJogo modo;
    private static Usuario usuarioAtual;

    public static void main(String[] args) {
        String[] opcoesMain = {
                "Jogar",
                "Infos",
                "Apagar Usuário",
                "Sair do programa"
        };

        Arquivo.criarArquivoCSV();

        JOptionPane.showMessageDialog(null, "Seja bem-vindo(a) ao jogo Arithack(Arithmetic Attack)");

        while(true) {
            LoginHandler.decidirCadastroOuLogin();

            if (Usuario.getUsuarioAtual().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Para usar o programa é necessário fazer seu cadastro/login");
                continue;
            }

            break;
        }

        usuarioAtual = new Usuario(Usuario.getInfoUsuarioPeloNome(Usuario.getUsuarioAtual()));

        loop:
        while (true) {
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "O que deseja fazer?",
                    "O Que Fazer?",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoesMain,
                    opcoesMain[0]
            );

            switch (escolha) {

                case 0 -> {
                    int escolhaJogar = JOptionPane.showConfirmDialog(null, "Aviso por limitações técnicas será necessário relogar após jogar. Ainda deseja jogar? ", "AVISO", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (escolhaJogar == 0) {
                        break loop;
                    }
                }
                case 1 -> infos();
                case 2 -> Usuario.apagarUsuario(usuarioAtual);

                default -> {
                    JOptionPane.showMessageDialog(null, "Finalizando programa...");
                    System.exit(0);
                }
            }
        }

        jogar();
    }

    private static void infos() {
        JOptionPane.showMessageDialog(null, "Arithack é um jogo de matemática básica, que vai desde adição até raiz quadrada e fatorial. A pontuação é baseada no tempo gasto e na dificuldade");
        JOptionPane.showMessageDialog(
                null,
                "Informações do usuário: \nNome: " + usuarioAtual.getNome() +
                "\nHighscore: " + usuarioAtual.getHighScore() +
                "\nModo: " + usuarioAtual.getModoDeJogo() +
                "\nQuantidade de jogos: " + usuarioAtual.getQuantidadeDeJogos()
        );
        // TODO: Explicar contas
    }

    private static void jogar() {
        JOptionPane.showMessageDialog(null, "Para jogar é necessário selecionar uma dificuldade. Para mais informações acesse a área de infos");
        selecionarDificuldade();

        if (modo != null) {
            new Jogo(modo, usuarioAtual.getNome());
            return;
        }

        JOptionPane.showMessageDialog(null, "Para jogar é necessário selecionar uma dificuldade");
    }

    private static void selecionarDificuldade() {
        ModosDeJogo[] opcoesModosDeJogo = {
                ModosDeJogo.SUPER_FACIL,
                ModosDeJogo.FACIL,
                ModosDeJogo.MEDIO_FACIL,
                ModosDeJogo.MEDIO,
                ModosDeJogo.MEDIO_DIFICIL,
                ModosDeJogo.DIFICIL
        };

        while (true) {
            ModosDeJogo modoEscolhido = (ModosDeJogo) JOptionPane.showInputDialog(null, "Qual a dificuldade desejada? ", "Dificuldade", JOptionPane.QUESTION_MESSAGE, null, opcoesModosDeJogo, opcoesModosDeJogo[0]);

            if (modoEscolhido != null) {
                modo = modoEscolhido;
                return;
            }

            if (UtilsComunsInput.deveCancelarOperacao()) {
                JOptionPane.showMessageDialog(null, "Cancelando operação...");
                System.exit(0);
            }

            JOptionPane.showMessageDialog(null, "Por favor tente novamente.");
        }
    }
}
