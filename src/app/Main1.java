package app;

import model.robo.Robo;
import view.MainBase;

import java.util.List;

public class Main1 extends MainBase {

    private final int alimX;
    private final int alimY;

    public Main1(List<Robo> robos) {
        super("Jogo Robô - Modo Player", robos, true);
        int[] alimento = pedirAlimento();
        alimX = alimento[0];
        alimY = alimento[1];
        painel.setAlimento(alimX, alimY);
        exibir();
    }

    @Override
    protected void aoMover(Robo robo) {
        if (robo.encontrouAlimento(alimX, alimY)) {
            encerrar("O robô encontrou o alimento!\n"
                    + "Movimentos válidos: " + robo.getMovimentoValido() + "\n"
                    + "Movimentos inválidos: " + robo.getMovimentoInvalido());
        }
    }
}