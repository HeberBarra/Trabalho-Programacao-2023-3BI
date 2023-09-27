package com.trabalho.programacao;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class UtilsComunsInput {
    protected static final String VALOR_REFAZER = "refazer", CANCELAR_OPERACAO = "cancelar";
    protected static final ArrayList<String> VALORES_PROIBIDOS = new ArrayList<>(Arrays.asList("", VALOR_REFAZER, CANCELAR_OPERACAO));

    protected static String confirmarEscolha(String valor, String nomeValor, String tituloValor) {
        while (true) {
            int escolha = JOptionPane.showConfirmDialog(
                    null,
                    nomeValor + " " + valor + "\nEstá certo?",
                    tituloValor,
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            switch (escolha) {
                case 0 -> {
                    return valor;
                }
                case 1 -> {
                    return VALOR_REFAZER;
                }
                case -1, 2 -> {
                    if (deveCancelarOperacao()) return CANCELAR_OPERACAO;
                }
            }
        }
    }

    protected static boolean deveCancelarOperacao() {
        return JOptionPane.showConfirmDialog(
                null,
                "Deseja cancelar a operação? ",
                "Cancelar",
                JOptionPane.YES_NO_OPTION
        ) == 0;
    }

    protected static boolean verificarValorString(String valor, String nomeValor) {
        if (VALORES_PROIBIDOS.contains(valor)) {
            JOptionPane.showMessageDialog(
                    null,
                    nomeValor + " inválido(a)",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return true;
        }

        return false;
    }

    public static String pegarInputUsuario(String nomeValor) {
        return pegarInputUsuario(nomeValor, true);
    }

    public static String pegarInputUsuario(String nomeValor, boolean mostrarValor) {
        String valor;

        nomeValor = nomeValor.strip().toLowerCase();
        String nomeValorPrimeiraMaiscula = nomeValor.substring(0, 1).toUpperCase() + nomeValor.substring(1);
        String nomeValorPrimeirasMaisculas = String.join("", Arrays.stream(nomeValor.split(" ")).map(String::toUpperCase).toList());

        while (true) {
            try {
                pegarValor:
                {
                    if (mostrarValor) {
                        valor = JOptionPane.showInputDialog("Digite o(a) " + nomeValor + ":");
                        break pegarValor;
                    }

                    var jPanel = new JPanel();
                    var labelValor = new JLabel("Digite o(a) " + nomeValor + ":");
                    var fieldValor = new JPasswordField(10);
                    jPanel.add(labelValor);
                    jPanel.add(fieldValor);

                    int escolha = JOptionPane.showConfirmDialog(null, jPanel, nomeValorPrimeiraMaiscula, JOptionPane.OK_CANCEL_OPTION);

                    if (escolha == -1 || escolha == 2) {
                        if (deveCancelarOperacao()) return CANCELAR_OPERACAO;
                        JOptionPane.showMessageDialog(null, "Por favor tente novamente");
                        continue;
                    }

                    valor = String.valueOf(fieldValor.getPassword());
                }
            } catch (NullPointerException e) {
                if (deveCancelarOperacao()) return CANCELAR_OPERACAO;
                continue;
            }



            if (UtilsComunsInput.verificarValorString(valor, nomeValorPrimeiraMaiscula)) continue;

            if (valor == null) {
                if (deveCancelarOperacao()) return CANCELAR_OPERACAO;
            }

            if (!mostrarValor) { return valor; }

            String escolha = UtilsComunsInput.confirmarEscolha(valor, nomeValorPrimeiraMaiscula, nomeValorPrimeirasMaisculas);

            switch (escolha) {
                case VALOR_REFAZER -> { }

                case CANCELAR_OPERACAO -> {
                    if (deveCancelarOperacao()) return "";
                }

                default -> {
                    return valor;
                }
            }
        }
    }
}
