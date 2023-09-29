package com.trabalho.programacao;

import javax.swing.JOptionPane;

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
        JOptionPane.showMessageDialog(null, """
                Dificuldades disponíveis:
                *As operações disponíveis num modo estão presentes nos posteriores.
                Super Fácil: adição;
                Fácil: subtração;
                Médio Fácil: levemente mais difícil que o anterior;
                Médio: Multiplicação;
                Médio Difícil: divisão, fatorial e potência;
                Difícil: raiz quadrada."""
        );
        JOptionPane.showMessageDialog(null, """
                Explicações básicas contas:
                Adição: combinação dos valores de números
                Subtração: diferença dos valores de números
                Multiplicação: adição multiplas, basicamente a combinação dos valores de grupos de números. 5 * 4 = cinco grupos de 4 ou quatro grupos de 5, e o total do valor dos grupos é 20
                Divisão: divisão de um valor em grupos de x elementos, ou seja, 20 / 5 = 4, em outras palavras, 20 dividido em 5 grupos de 4
                Potência: Multiplicação múltipla de um mesmo valor, a quantidade multiplicações é definida pelo expoente.
                Raiz quadrada: O oposto da potência. Em outras palavras x é um número que precisa elevado a y, neste caso 2, vezes para encontrar o valor da raiz.
                Fatorial: Multiplicação múltipla onde o próximo valor é reduzido por 1 até chegar em 1, é indicado por um ! ao lado do número, exemplo: 4! = 4 * 3 * 2 * 1 = 24."""
        );
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
