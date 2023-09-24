package com.trabalho.programacao;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class CriarUsuario {

    public LinkedHashMap<String, String> usuario = new LinkedHashMap<>();

    private static final String VALOR_REFAZER = "refazer", CANCELAR_OPERACAO = "cancelar";

    private static String criarNomeDeUsuario() {
        String nome;

        while (true) {

            try {
                nome = JOptionPane.showInputDialog("Nome de usuário: ").strip();
            } catch (NullPointerException e) {
                if (UtilsComunsInput.deveCancelarOperacao()) return CANCELAR_OPERACAO;
                continue;
            }

            if (UtilsComunsInput.verificarValorString(nome, "Nome de usuário")) continue;
            nome = UtilsComunsInput.confirmarEscolha(nome, "Nome de usuário", "Nome de Usuário");
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

            if ((escolha == -1 || escolha == 2) && UtilsComunsInput.deveCancelarOperacao()) {
                return CANCELAR_OPERACAO;
            }

            if (UtilsComunsInput.verificarValorString(String.valueOf(fieldSenha.getPassword()), "Senha")) {
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

    public CriarUsuario() {
        String nome, senha = "", salt;

        nome = criarNomeDeUsuario();
        if (!nome.equals(UtilsComunsInput.CANCELAR_OPERACAO)) senha = criarSenha();

        if (nome.equals(CANCELAR_OPERACAO) || senha.equals(CANCELAR_OPERACAO)) {
            JOptionPane.showMessageDialog(
                    null,
                    "Operação cancelada",
                    "Cancelada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        String[] resultadoHash = DataHash.dataHasher(senha);

        this.usuario.put("nome", nome);
        this.usuario.put("senha", resultadoHash[0]);
        this.usuario.put("salt", resultadoHash[1]);
        this.usuario.put("highscore", "0");
        this.usuario.put("modo de jogo (hscore)", null);
        this.usuario.put("quantidade de jogos", "0");
    }

}
