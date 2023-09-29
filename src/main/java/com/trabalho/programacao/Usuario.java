package com.trabalho.programacao;


import javax.swing.*;
import java.util.LinkedHashMap;

public class Usuario {

    private static String usuarioAtual;
    protected LinkedHashMap<String, String> infoUsuario;

    public Usuario(LinkedHashMap<String, String> info) {
        this.infoUsuario = info;
    }

    public static void setUsuarioAtual(String nomeUsuario) {
        usuarioAtual = nomeUsuario;
    }

    public static String getUsuarioAtual() {
        return  usuarioAtual;
    }

    public String getNome() {
        return this.infoUsuario.getOrDefault("nome", "");
    }

    public String getHashSenha() {
        return this.infoUsuario.getOrDefault("senha", "");
    }

    public byte[] getSalt() {
        String saltString = this.infoUsuario.getOrDefault("salt", "");
        byte[] salt = new byte[16];

        if (saltString.isEmpty()) return new byte[0];

        String[] partesSalt = saltString.replace("[", "").replace("]", "").split(", ");

        for (int i = 0; i < partesSalt.length; i++) {
            salt[i] = (byte) (Integer.parseInt(partesSalt[i]));
        }

        return salt;
    }

    public double getHighScore() {
        return Double.parseDouble(this.infoUsuario.getOrDefault("highscore", "0"));
    }

    public void setHighScore(double novoHighScore) {
        this.infoUsuario.replace("highscore", String.valueOf(novoHighScore));
    }

    public String getModoDeJogo() {
        return this.infoUsuario.getOrDefault("modo de jogo (hscore)", "");
    }

    public void setModoDeJogo(String modoDeJogo) {
        this.infoUsuario.replace("modo de jogo(hscore)", modoDeJogo);
    }

    public int getQuantidadeDeJogos() {
        return Integer.parseInt(this.infoUsuario.getOrDefault("quantidade de jogos", "0"));
    }

    public void aumentarQuantidadeDeJogos() {
        this.infoUsuario.replace("quantidade jogos", String.valueOf(getQuantidadeDeJogos() + 1));
    }

    public static boolean compararSenha(String hashSenha, String senha, byte[] salt) {
        return hashSenha.equals(DataHash.dataHasher(senha, salt));
    }

    public static LinkedHashMap<String, String> getInfoUsuarioPeloNome(String nome) {
        for (var usuario: Arquivo.lerArquivoCSV()) {
            if (usuario.get("nome").equals(nome)) {
                return usuario;
            }
        }

        return new LinkedHashMap<>();
    }

    public static boolean apagarUsuario(Usuario usuario) {
        while (true) {
            int escolha = JOptionPane.showConfirmDialog(null, "Apagar usuário: " + usuario.getNome(), "Apagar?", JOptionPane.YES_NO_OPTION);

            if (escolha == -1 || escolha == 1) return false;

            String senha = UtilsComunsInput.pegarInputUsuario("Senha", false);

            if (usuario.getHashSenha().equals(DataHash.dataHasher(senha, usuario.getSalt()))) {
                JOptionPane.showMessageDialog(null, "Usuário será apagado");
                return true;
            }

            escolha = JOptionPane.showConfirmDialog(null, "Senha incorreta! Deseja tentar novamente? ", "Tentar Novamente?", JOptionPane.YES_NO_OPTION);

            if (escolha == 1) { continue; }
            return false;
        }
    }
}
