package model.obstaculo;

import model.robo.Robo;

public class Bomba extends Obstaculo {

    public Bomba(int id, int x, int y) {
        super(id, x, y);
    }

    /** O robô explode (não anda mais) e a bomba desaparece do tabuleiro. */
    @Override
    public void bater(Robo robo) {
        if (!isAtivo()) {
            return; // bomba já explodiu
        }
        robo.explodir();
        desativar();
    }
}