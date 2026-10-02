package model.obstaculo;

import model.robo.Robo;

public abstract class Obstaculo {

    protected int id;
    private final int x;
    private final int y;
    private boolean ativo = true;

    protected Obstaculo(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /** Obstáculo inativo já foi "gasto" (ex.: bomba que explodiu) e deve sair do tabuleiro. */
    public boolean isAtivo() {
        return ativo;
    }

    protected void desativar() {
        ativo = false;
    }

    /** True se o obstáculo ainda está no tabuleiro e ocupa a posição (x, y). */
    public boolean estaEm(int x, int y) {
        return ativo && this.x == x && this.y == y;
    }

    /** Efeito do obstáculo sobre o robô que encostou nele. */
    public abstract void bater(Robo robo);
}