package app;

import model.robo.Robo;
import view.MainBase;

import java.util.List;
import java.util.Random;

public class Main2 extends MainBase {
    private final Random sorteio = new Random();
    private final int alimX;
    private final int alimY;

    public Main2(List<Robo> robos) {
        super("Jogo Robô - Modo Robo x Robo", robos, false);
        alimX = pedirCoordenada("x");
        alimY = pedirCoordenada("y");
        painel.setAlimento(alimX, alimY);
        exibir();
        automatizar(1000, this::aleatorio);
    }

    private String aleatorio(Robo robo) {
            return DIRECOES[sorteio.nextInt(DIRECOES.length)];
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
