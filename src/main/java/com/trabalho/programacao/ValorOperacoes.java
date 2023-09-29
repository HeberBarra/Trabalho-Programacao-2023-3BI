package com.trabalho.programacao;

public enum ValorOperacoes {
    ADICAO(1),
    SUBTRACAO(1.1),
    MULTIPLICAO(2),
    FATORIAL(3),
    DIVISAO(3),
    POTENCIA(3),
    RAIZ_QUADRADA(4);

    private final double valor;

    ValorOperacoes(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
