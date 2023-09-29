package com.trabalho.programacao;

public enum ModosDeJogo {
    SUPER_FACIL(100),
    FACIL(80),
    MEDIO_FACIL(60),
    MEDIO(40),
    MEDIO_DIFICIL(30),
    DIFICIL(20);

    private final int nivelModoDeJogo;

    ModosDeJogo(int nivelModoDeJogo) {
        this.nivelModoDeJogo = nivelModoDeJogo;
    }

    public int getNivelModoDeJogo() {
        return nivelModoDeJogo;
    }
}
