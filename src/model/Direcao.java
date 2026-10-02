package model;

import java.awt.event.KeyEvent;

public enum Direcao {
    ESQUERDA("Esquerda", "left", KeyEvent.VK_LEFT),
    CIMA("Cima", "up", KeyEvent.VK_UP),
    BAIXO("Baixo", "down", KeyEvent.VK_DOWN),
    DIREITA("Direita", "right", KeyEvent.VK_RIGHT);

    private final String rotulo;
    private final String comando;
    private final int tecla;

    Direcao(String rotulo, String comando, int tecla) {
        this.rotulo = rotulo;
        this.comando = comando;
        this.tecla = tecla;
    }

    public int getTecla() {
        return tecla;
    }

    public String getComando() {
        return comando;
    }

    public String getRotulo() {
        return rotulo;
    }
}
