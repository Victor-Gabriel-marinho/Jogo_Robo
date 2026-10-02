package model.obstaculo;

import model.robo.Robo;

public class Rocha extends Obstaculo {

    public Rocha(int id, int x, int y) {
        super(id, x, y);
    }

    /** O robô volta para a posição de onde veio. A rocha continua no tabuleiro. */
    @Override
    public void bater(Robo robo) {
        robo.voltarParaPosicaoAnterior();
    }
}