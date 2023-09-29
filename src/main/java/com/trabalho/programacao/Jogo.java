package com.trabalho.programacao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.TimerTask;
import java.util.Timer;
import java.util.Random;
import java.util.ArrayList;

public class Jogo {

    private final ModosDeJogo modo;
    private final String nomeDoUsuario;
    private double pontuacaoTotal = 0;
    private JFrame desafio;
    private JLabel labelTempo;
    private final Random random = new Random();

    public Jogo(ModosDeJogo modo, String nomeDoUsuario) {
        this.modo = modo;
        this.nomeDoUsuario = nomeDoUsuario;
        proximoDesafio(escolherOperacao(), new double[]{calculoTempoMaximo(ValorOperacoes.ADICAO)});
    }

    private ArrayList<ValorOperacoes> pegarOperacoesModo() {
        ArrayList<ValorOperacoes> operacoes = new ArrayList<>();

        switch (modo) {
            case DIFICIL:
                operacoes.add(0, ValorOperacoes.RAIZ_QUADRADA);

            case MEDIO_DIFICIL:
                operacoes.add(0, ValorOperacoes.POTENCIA);
                operacoes.add(0, ValorOperacoes.DIVISAO);
                operacoes.add(0, ValorOperacoes.FATORIAL);

            case MEDIO:
                operacoes.add(0, ValorOperacoes.MULTIPLICAO);

            case FACIL:
            case MEDIO_FACIL:
                operacoes.add(0, ValorOperacoes.SUBTRACAO);
        }

        operacoes.add(0, ValorOperacoes.ADICAO);

        return operacoes;
    }

    private void proximoDesafio(ValorOperacoes operacao, double[] tempo) {
        final int HEIGHT_DESAFIO = 700;
        final int WIDTH_DESAFIO = 1200;
        final int ESPACAMENTO_BOTOES = 10;

        var contaEResultado = criarContaMatematica(criarValoresConta(operacao), operacao);
        Random random = new Random();
        desafio = new JFrame();
        JButton[] botoes = new JButton[4];
        final int BOTAO_CERTO = random.nextInt(botoes.length);

        labelTempo = new JLabel(String.valueOf(tempo[0]));

        JLabel labelConta = new JLabel(contaEResultado[0] + "\t");
        labelConta.setHorizontalAlignment(SwingConstants.CENTER);
        labelConta.setVerticalAlignment(SwingConstants.CENTER);
        labelConta.setSize(500, 500);
        labelConta.setFont(new Font("Arial", Font.PLAIN, 50));
        labelConta.setForeground(Color.BLACK);
        desafio.add(labelConta);

        Timer timer = new Timer();

        Container containerBotoes = new Container();
        containerBotoes.setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO / 5 + 10);
        int posicaoXAnterior = 0;

        for (int i = 0; i < botoes.length; i++) {
            botoes[i] = new JButton();
            botoes[i].setSize(WIDTH_DESAFIO / 5, HEIGHT_DESAFIO / 10);
            botoes[i].setVerticalAlignment(SwingConstants.CENTER);
            botoes[i].setBackground(Color.GRAY);
            botoes[i].setForeground(Color.WHITE);
            botoes[i].setText(criarRespostasErradas(contaEResultado[1]));
            containerBotoes.add(botoes[i]);
            botoes[i].setLocation(posicaoXAnterior, containerBotoes.getHeight() / 2);
            posicaoXAnterior += ESPACAMENTO_BOTOES + botoes[i].getWidth();

            if (i != BOTAO_CERTO) {
                botoes[i].addActionListener(e -> opcaoErrada());
                continue;
            }

            botoes[i].setText(contaEResultado[1]);
            botoes[i].addActionListener(e -> opcaoCerta());
        }

        JButton botaoSair = new JButton("SAIR");
        desafio.add(botaoSair);
        botaoSair.setLocation(15, 15);
        botaoSair.setSize(WIDTH_DESAFIO / 10, HEIGHT_DESAFIO / 10);
        botaoSair.setBackground(Color.GRAY);
        botaoSair.setForeground(Color.WHITE);
        botaoSair.setFocusable(false);
        botaoSair.addActionListener(e -> desafio.dispose());

        int novoX = (WIDTH_DESAFIO - (botoes.length * botoes[0].getWidth() + ESPACAMENTO_BOTOES * (botoes.length))) / 2;
        containerBotoes.setLocation(novoX, HEIGHT_DESAFIO - containerBotoes.getHeight() - botoes[0].getHeight());
        labelConta.setLocation((desafio.getWidth() - labelConta.getWidth()) / 2, (desafio.getHeight() - labelConta.getHeight()) / 2);
        desafio.add(containerBotoes);
        desafio.setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO);
        desafio.add(labelTempo);
        desafio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        desafio.setLocationRelativeTo(null);
        desafio.setVisible(true);

        labelTempo.setFont(new Font("Arial", Font.BOLD, 15));

        desafio.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int novoX = (desafio.getWidth() - (botoes.length * botoes[0].getWidth() + ESPACAMENTO_BOTOES * (botoes.length))) / 2;
                containerBotoes.setLocation(novoX, HEIGHT_DESAFIO - containerBotoes.getHeight() - botoes[0].getHeight());
                labelConta.setLocation((desafio.getWidth() - labelConta.getWidth()) / 2, (desafio.getHeight() - labelConta.getHeight()) / 2);
            }
        });

        desafio.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                timer.cancel();
            }
        });

        int DELAY = 1000;
        int PERIOD = 1000;
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (tempo[0] == -1) {
                            desafio.dispose();
                            timer.cancel();
                        }

                        tempo[0]--;
                        labelTempo.setText(String.valueOf(tempo[0]));

                    }
                },
                DELAY,
                PERIOD
        );

    }

    private void opcaoErrada() {
        desafio.dispose();
        salvarResultadoJogo();
        JOptionPane.showMessageDialog(null, "Você errou! Fim de jogo.");
    }

    private void opcaoCerta() {
        desafio.dispose();
        setPontuacaoTotal(calculoPontuacao(Double.parseDouble(labelTempo.getText())));
        var proximaOperacao = escolherOperacao();
        proximoDesafio(proximaOperacao, new double[]{calculoTempoMaximo(proximaOperacao)});
    }

    private ValorOperacoes escolherOperacao() {
        var operacoesPossiveis = pegarOperacoesModo();
        return operacoesPossiveis.get(random.nextInt(operacoesPossiveis.size()));
    }

    private ArrayList<String> criarValoresConta(ValorOperacoes operacao) {
        int valorMaximo = 0, valorMinimo = 0;
        int quantidadeValores = 1;
        boolean apenasInts = true;
        ArrayList<String> valores = new ArrayList<>();

        switch (operacao) {
            case ADICAO, SUBTRACAO -> {
                quantidadeValores = 2;
                valorMaximo = 100;
                apenasInts = false;
            }

            case MULTIPLICAO -> {
                quantidadeValores = 2;
                valorMaximo = 40;
                valorMinimo = -20;
            }

            case FATORIAL -> valorMaximo = 10;

            case POTENCIA -> {
                valorMaximo = 50;
                valorMinimo = -50;
            }

            case DIVISAO -> {
                quantidadeValores = 2;
                valorMaximo = 12;
                valorMinimo = -5;
            }

            case RAIZ_QUADRADA -> valorMaximo = 50;
        }

        DecimalFormat formatoNumeros = new DecimalFormat("#.00");
        for (int i = 0; i < quantidadeValores; i++) {
            if (apenasInts) {
                valores.add(String.valueOf(random.nextInt(valorMaximo) + valorMinimo));
                continue;
            }

            valores.add(formatoNumeros.format(random.nextDouble(valorMaximo) + 1));
        }

        return valores;
    }

    private String[] criarContaMatematica(ArrayList<String> valores, ValorOperacoes operacao) {
        String[] contaEResultado = new String[2];
        double resultado = 0;
        DecimalFormat formatoNumeros = new DecimalFormat("0.00");

        if (valores.size() == 1) {
            String valor = valores.get(0);
            String conta = "";

            switch (operacao) {
                case FATORIAL -> {
                    int valorNumerico = Integer.parseInt(valor);
                    int total = 1;

                    for (int i = valorNumerico; i > 0; i--) {
                        total *= i;
                    }

                    resultado = total;
                    conta = valor + "!";
                }

                case POTENCIA -> {
                    resultado = Math.pow(Double.parseDouble(valores.get(0)), 2);
                    conta = valor + "^2";
                }

                case RAIZ_QUADRADA -> {
                    resultado = Math.sqrt(Double.parseDouble(valor));
                    conta = valor + "^½";
                }
            }

            contaEResultado[0] = conta;
            contaEResultado[1] = formatoNumeros.format(resultado);
            return contaEResultado;
        }

        String conta;
        valores.set(0, valores.get(0).replace(',', '.'));
        valores.set(1, valores.get(0).replace(',', '.'));

        switch (operacao) {
            case DIVISAO -> {
                conta = valores.get(0) + "/" + valores.get(1);
                resultado = Double.parseDouble(valores.get(0)) / Double.parseDouble(valores.get(1));
            }

            case MULTIPLICAO -> {
                conta = valores.get(0) + "*" + valores.get(1);
                resultado = Double.parseDouble(valores.get(0)) * Double.parseDouble(valores.get(1));
            }
            case SUBTRACAO -> {
                conta = valores.get(0) + " - " + valores.get(1);
                resultado = Double.parseDouble(valores.get(0)) - Double.parseDouble(valores.get(1));
            }

            default -> {
                conta = valores.get(0) + " + " + valores.get(1);
                resultado = Double.parseDouble(valores.get(0)) + Double.parseDouble(valores.get(1));
            }
        }
        contaEResultado[0] = conta;
        contaEResultado[1] = formatoNumeros.format(resultado);

        return contaEResultado;

    }

    private String criarRespostasErradas(String respostaCerta) {
        int numeroAleatorio = random.nextInt(20);
        int negativo = random.nextBoolean() ? -1 : 1;
        DecimalFormat formatoNumero = new DecimalFormat("#.00");
        respostaCerta = respostaCerta.replace(',', '.');

        return formatoNumero.format(((Double.parseDouble(respostaCerta) * negativo)) - numeroAleatorio + 1);
    }

    private void salvarResultadoJogo() {
        var usuario = new Usuario(Usuario.getInfoUsuarioPeloNome(nomeDoUsuario));

        if (usuario.getHighScore() < getPontuacaoTotal()) {
            usuario.setHighScore(getPontuacaoTotal());
            usuario.setModoDeJogo(modo);
        }

        usuario.aumentarQuantidadeDeJogos();
        Arquivo.atualizarArquivo(usuario.infoUsuario);
    }

    private double calculoTempoMaximo(ValorOperacoes valorOperacao) {
        return Math.floor((this.modo.getNivelModoDeJogo() * valorOperacao.getValor()) / 2);
    }

    private double calculoPontuacao(double tempoRestante) {
        return Math.floor((Math.pow(tempoRestante, 2) * 2 + tempoRestante * modo.getNivelModoDeJogo() + Math.pow(tempoRestante, 3)) / 150);
    }

    public double getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    private void setPontuacaoTotal(double pontos) {
        pontuacaoTotal += pontos;
    }

}
