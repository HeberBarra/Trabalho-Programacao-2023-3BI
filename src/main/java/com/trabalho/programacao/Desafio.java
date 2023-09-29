package com.trabalho.programacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Desafio extends JFrame {

    private final ModosDeJogo modo;
    private final ValorOperacoes valorOperacao;
    private final JLabel labelTempo = new JLabel();
    private Object teste = new Object();
    private final Timer timer = new Timer();
    private double pontuacao;
    private static boolean fimDeJogo = false;
    private boolean fimDoDesafio = false;
    private double tempo;

    private double calculoTempoMaximo() {
        return Math.floor((this.modo.getNivelModoDeJogo() * valorOperacao.getValor()) / 2);
    }

    private double diminuirTempo() {
        if (tempo == -1) {
            this.dispose();
            fimDeJogo = true;
            timer.cancel();
        }

        return tempo--;
    }

    private void opcaoErrada() {
        dispose();
        JOptionPane.showMessageDialog(null, "Você errou! Fim de jogo.");
    }

    private void opcaoCerta() {
        fimDoDesafio = true;
        pontuacao = calculoPontuacao(tempo);
    }

    private double calculoPontuacao(double tempoRestante) {
        return Math.floor((Math.pow(tempoRestante, 2) * 2 + tempoRestante * modo.getNivelModoDeJogo() + Math.pow(tempoRestante, 3)) / 150);
    }

    public double getPontuacao() {
        return pontuacao;
    }

    public static boolean getFinalDeJogo() {
        return fimDeJogo;
    }

    public boolean getFimDoDesafio() { return fimDoDesafio; }

    public Desafio(ValorOperacoes valorOperacao, ModosDeJogo modoDeJogo) {
        this.modo = modoDeJogo;
        this.valorOperacao = valorOperacao;

        final int HEIGHT_DESAFIO = 700;
        final int WIDTH_DESAFIO = 1200;
        final int DELAY = 1000;
        final int PERIOD = 1000;

        Random random = new Random();
        JButton[] botoes = new JButton[4];
        final int botaoCerto = random.nextInt(botoes.length);
        final int ESPACAMENTO_BOTOES = 10;
        int posicaoXAnterior = 0;

        tempo = calculoTempoMaximo();
        labelTempo.setText(String.valueOf(tempo));

        Container container = new Container();
        container.setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO / 5 + 10);

        System.out.println(botaoCerto);

        for (int i = 0; i < botoes.length; i++) {
            botoes[i] = new JButton();
            botoes[i].setSize(WIDTH_DESAFIO / 5, HEIGHT_DESAFIO / 10);
            botoes[i].setVerticalAlignment(SwingConstants.CENTER);
            botoes[i].setBackground(Color.GRAY);
            botoes[i].setForeground(Color.WHITE);
            botoes[i].setText("Teste " + i);
            container.add(botoes[i]);
            botoes[i].setLocation(posicaoXAnterior, container.getHeight() / 2);
            posicaoXAnterior += ESPACAMENTO_BOTOES + botoes[i].getWidth();

            if (i != botaoCerto) {
                botoes[i].addActionListener(e -> opcaoErrada());
                continue;
            }

            botoes[i].addActionListener(e -> opcaoCerta());
        }



        JButton sair = new JButton("SAIR");
        add(sair);
        sair.setLocation(15, 15);
        sair.setSize(WIDTH_DESAFIO / 10, HEIGHT_DESAFIO / 10);
        sair.setBackground(Color.GRAY);
        sair.setForeground(Color.WHITE);
        sair.setFocusable(false);
        sair.addActionListener(e -> dispose());

        int novoX = (WIDTH_DESAFIO - (botoes.length * botoes[0].getWidth() + ESPACAMENTO_BOTOES * (botoes.length))) / 2;
        container.setLocation(novoX, HEIGHT_DESAFIO - container.getHeight() - botoes[0].getHeight());
        add(container);
        setSize(WIDTH_DESAFIO, HEIGHT_DESAFIO);
        add(labelTempo);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        Thread t = new Thread(() -> {
           synchronized (teste) {
               System.out.println(this.getFimDoDesafio());
               while (!this.getFimDoDesafio()) {
                   try {
                       teste.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }

            System.out.println("Fim");
        });

        t.start();

        addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                int novoX = (getWidth() - (botoes.length * botoes[0].getWidth() + ESPACAMENTO_BOTOES * (botoes.length))) / 2;
                container.setLocation(novoX, HEIGHT_DESAFIO - container.getHeight() - botoes[0].getHeight());
            }

            public void componentMoved(ComponentEvent e) {

            }
            public void componentShown(ComponentEvent e) {

            }
            public void componentHidden(ComponentEvent e) {

            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fimDoDesafio = true;

                synchronized (teste) {
                    setVisible(false);
                    teste.notify();
                }
            }


            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("saindo");
                fimDeJogo = true;
                timer.cancel();
                teste = null;
            }
        });

        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        labelTempo.setText(String.valueOf(diminuirTempo()));
                    }
                },
                DELAY,
                PERIOD
        );

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
