package com.trabalho.programacao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class Usuario {

    private static final String VALOR_REFAZER = "refazer", CANCELAR_OPERACAO = "cancelar";
    private static final List<String> VALORES_PROIBIDOS = Arrays.stream(new String[]{"", VALOR_REFAZER, CANCELAR_OPERACAO}).toList();

    public static String confirmarEscolha(String valor, String nomeValor, String tituloValor) {
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

    private static boolean verificarValorString(String valor, String nomeValor) {
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

    private static boolean deveCancelarOperacao() {
        return JOptionPane.showConfirmDialog(
                null,
                "Deseja cancelar a operação? ",
                "Cancelar",
                JOptionPane.YES_NO_OPTION
        ) == 0;
    }

    private static String criarNomeDeUsuario() {
        String nome;

        while (true) {

            try {
                nome = JOptionPane.showInputDialog("Nome de usuário: ").strip();
            } catch (NullPointerException e) {
                if (deveCancelarOperacao()) return CANCELAR_OPERACAO;
                continue;
            }

            if (verificarValorString(nome, "Nome de usuário")) continue;
            nome = confirmarEscolha(nome, "Nome de usuário", "Nome de Usuário");
            if (nome.equals(VALOR_REFAZER)) continue;

            return nome;
        }
    }

    private static String criarSenha() {
        var jPanel = new JPanel();
        var labelSenha = new JLabel("Digite uma senha:");
        var fieldSenha = new JPasswordField(10);
        var labelConfirmarSenha = new JLabel("Redigite a senha: ");
        var fieldConfirmarSenha = new JPasswordField(10);

        jPanel.add(labelSenha);
        jPanel.add(fieldSenha);
        jPanel.add(labelConfirmarSenha);
        jPanel.add(fieldConfirmarSenha);

        while (true) {

            int escolha = JOptionPane.showConfirmDialog(
                    null,
                    jPanel,
                    "Senha",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if ((escolha == -1 || escolha == 2) && deveCancelarOperacao()) {
                return CANCELAR_OPERACAO;
            }

            if (verificarValorString(String.valueOf(fieldSenha.getPassword()), "Senha")) {
                continue;
            }

            if (!(Arrays.equals(fieldSenha.getPassword(), fieldConfirmarSenha.getPassword()))) {
                JOptionPane.showMessageDialog(
                        null,
                        "Senhas são diferentes! Por favor digite novamente.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            return String.valueOf(fieldSenha.getPassword());
        }
    }


    public static void criarUsuario() {
        String nome, senha = "", salt;

        nome = criarNomeDeUsuario();
        if (!nome.equals(CANCELAR_OPERACAO)) senha = criarSenha();

        if (nome.equals(CANCELAR_OPERACAO) || senha.equals(CANCELAR_OPERACAO)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Operação cancelada",
                    "Cancelada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        String[] resultado = DataHash.dataHasher(senha);

        senha = resultado[0];
        salt = resultado[1];

        HashMap<String, String> usuario = new LinkedHashMap<>();

        usuario.put("nome", nome);
        usuario.put("senha", senha);
        usuario.put("salt", salt);
        usuario.put("highscore", "0");
        usuario.put("modo de jogo (hscore)", null);
        usuario.put("quantidade de jogos", "0");
        usuario.put("data de criação", String.valueOf(Calendar.getInstance().get(Calendar.DATE)));



        for (String key: usuario.keySet()) {
            System.out.println(key + ": " + usuario.get(key));
        }
    }
}
