package com.trabalho.programacao;

import javax.swing.JOptionPane;
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
}
