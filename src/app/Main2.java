package app;

import model.robo.Robo;
import view.MainBase;

import java.util.List;

public class Main2 extends MainBase {

    // tempo entre dois movimentos (um robô se move por vez)
    private static final int ATRASO_MS = 500;

    private final int alimX;
    private final int alimY;

    public Main2(List<Robo> robos) {
        super("Jogo Robô - Modo Robo x Robo", robos, false);
        int[] alimento = pedirAlimento();
        alimX = alimento[0];
        alimY = alimento[1];
        painel.setAlimento(alimX, alimY);
        exibir();
        automatizar(ATRASO_MS, robo -> direcaoAleatoria());
    }

    @Override
    protected void aoMover(Robo robo) {
        if (robo.encontrouAlimento(alimX, alimY)) {
            StringBuilder sb = new StringBuilder(descrever(robo) + " encontrou o alimento!\n");
            for (Robo r : robos) {
                sb.append("\n").append(resumo(r));
            }
            encerrar(sb.toString());
        }
    }
}