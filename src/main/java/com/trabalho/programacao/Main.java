package com.trabalho.programacao;

public class Main {

    public static void main(String[] args) {
        CriarUsuario novo = new CriarUsuario();

        for (String key: novo.usuario.keySet()) {
            System.out.println(key + ": " + novo.usuario.get(key));
        }
    }

}
