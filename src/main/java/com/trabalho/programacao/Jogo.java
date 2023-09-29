package com.trabalho.programacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.Timer;

public class Jogo {

    private final ModosDeJogo modo;
    private final String nomeDoUsuario;
    private double pontuacaoTotal = 0;
    private JFrame desafio;
    private JLabel labelTempo;
    private boolean fimDeJogo;
    private Timer timer;
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


        desafio = new JFrame();
        Random random = new Random();
        JButton[] botoes = new JButton[4];
        final int BOTAO_CERTO = random.nextInt(botoes.length);
        System.out.println(BOTAO_CERTO);
        labelTempo = new JLabel(String.valueOf(tempo[0]));
//        timer = new Timer();

        Container containerBotoes = new Container();
        containerBotoes.setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO / 5 + 10);
        int posicaoXAnterior = 0;

        for (int i = 0; i < botoes.length; i++) {
            botoes[i] = new JButton();
            botoes[i].setSize(WIDTH_DESAFIO / 5, HEIGHT_DESAFIO / 10);
            botoes[i].setVerticalAlignment(SwingConstants.CENTER);
            botoes[i].setBackground(Color.GRAY);
            botoes[i].setForeground(Color.WHITE);
            botoes[i].setText("Teste " + i);
            containerBotoes.add(botoes[i]);
            botoes[i].setLocation(posicaoXAnterior, containerBotoes.getHeight() / 2);
            posicaoXAnterior += ESPACAMENTO_BOTOES + botoes[i].getWidth();

            if (i != BOTAO_CERTO) {
                botoes[i].addActionListener(e -> opcaoErrada());
                continue;
            }

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
        desafio.add(containerBotoes);
        desafio.setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO);
        desafio.add(labelTempo);
        desafio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        desafio.setLocationRelativeTo(null);
        desafio.setVisible(true);

        desafio.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int novoX = (desafio.getWidth() - (botoes.length * botoes[0].getWidth() + ESPACAMENTO_BOTOES * (botoes.length))) / 2;
                containerBotoes.setLocation(novoX, HEIGHT_DESAFIO - containerBotoes.getHeight() - botoes[0].getHeight());
            }
        });

        desafio.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                fimDeJogo = true;
                //timer.cancel();
            }
        });
/*
        int DELAY = 1000;
        int PERIOD = 1000;
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (tempo[0] == -1) {
                            desafio.dispose();
                            fimDeJogo = true;
                            timer.cancel();
                        }

                        tempo[0]--;
                        labelTempo.setText(String.valueOf(tempo[0]));

                    }
                },
                DELAY,
                PERIOD
        );
        */

    }

    private void opcaoErrada() {
        desafio.dispose();
        JOptionPane.showMessageDialog(null, "Você errou! Fim de jogo.");
    }

    private void opcaoCerta() {
        // TODO: consertar o timer
        //timer.cancel();
        //timer.purge();
        //timer = new Timer();
        desafio.dispose();
        var proximaOperacao = escolherOperacao();
        proximoDesafio(proximaOperacao, new double[]{calculoTempoMaximo(proximaOperacao)});
    }

    private ValorOperacoes escolherOperacao() {
        var operacoesPossiveis = pegarOperacoesModo();
        return operacoesPossiveis.get(random.nextInt(operacoesPossiveis.size()));
    }

    private String criarContaMatematica(ValorOperacoes operacao) {
        int quantidadeMaximaValores = 0, quantidadeMinimaValores = 0, valorMaximo = 0;
        boolean apenasInts = true;
        ArrayList<Double> valores = new ArrayList<>();

        switch (operacao) {
            case ADICAO -> {
                quantidadeMinimaValores = 2;
                quantidadeMaximaValores = 5;
                valorMaximo = 100;
                apenasInts = false;
            }

            case SUBTRACAO -> {
                quantidadeMinimaValores = 2;
                quantidadeMaximaValores = 3;
                valorMaximo = 80;
                apenasInts = false;
            }

            case MULTIPLICAO -> {
                quantidadeMinimaValores = 2;
                quantidadeMaximaValores = 3;
                valorMaximo = 90;
            }

            case FATORIAL -> {
                quantidadeMinimaValores = 1;
                quantidadeMaximaValores = 1;
                valorMaximo = 10;
            }

            case POTENCIA -> {
                quantidadeMinimaValores = 1;
                quantidadeMaximaValores = 1;
                valorMaximo = 50;
            }

            case DIVISAO -> {
                quantidadeMinimaValores = 2;
                quantidadeMaximaValores = 2;
            }

            case RAIZ_QUADRADA -> {
                quantidadeMinimaValores = 1;
                quantidadeMaximaValores = 1;
                valorMaximo = 400;
            }
        }

        var quantidadeValores = random.nextInt(quantidadeMaximaValores - quantidadeMinimaValores) + quantidadeMinimaValores;

        for (int i = 0; i < quantidadeValores; i++) {
            if (apenasInts) {
                valores.add((double) random.nextInt(valorMaximo)+1);
                continue;
            }

            valores.add(random.nextDouble(valorMaximo) + 1);
        }

        return "";
    }

    private void salvarResultadoJogo() {
        var usuario = new Usuario (Usuario.getInfoUsuarioPeloNome(nomeDoUsuario));
        usuario.setHighScore(pontuacaoTotal);
        usuario.setModoDeJogo(modo.toString());
        usuario.aumentarQuantidadeDeJogos();
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

    private void setPontuacaoTotal(int pontos) {
        pontuacaoTotal += pontos;
    }

}
