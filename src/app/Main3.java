package app;

import model.robo.Robo;
import model.robo.RoboInteligente;
import view.MainBase;

import java.util.List;

public class Main3 extends MainBase {

    // tempo entre dois movimentos (um robô se move por vez)
    private static final int ATRASO_MS = 500;

    private final int alimX;
    private final int alimY;

    public Main3(List<Robo> robos) {
        super("Jogo Robô - Modo Robo x Robo Inteligente", robos, false);
        int[] alimento = pedirAlimento();
        alimX = alimento[0];
        alimY = alimento[1];
        painel.setAlimento(alimX, alimY);
        exibir();
        automatizar(ATRASO_MS, robo -> direcaoAleatoria());
    }

    @Override
    protected void aoMover(Robo robo) {
        if (!robo.encontrouAlimento(alimX, alimY)) {
            return;
        }

        // quem achou a comida para de se mover; o jogo continua até todos acharem
        finalizados.add(robo);
        if (finalizados.size() < robos.size()) {
            StringBuilder aviso = new StringBuilder();
            for (Robo r : robos) {
                if (finalizados.contains(r)) {
                    aviso.append(descrever(r)).append(" já achou em ")
                            .append(total(r)).append(" movimentos. ");
                }
            }
            setAviso(aviso + "Aguardando...");
            return;
        }

        StringBuilder sb = new StringBuilder("Todos os robôs encontraram o alimento!\n");
        for (Robo r : robos) {
            sb.append("\n").append(descrever(r))
                    .append(r instanceof RoboInteligente ? " [inteligente]: " : " [normal]: ")
                    .append(total(r)).append(" movimentos (")
                    .append(r.getMovimentoValido()).append(" válidos, ")
                    .append(r.getMovimentoInvalido()).append(" inválidos)");
        }
        encerrar(sb.toString());
    }
    private int total(Robo robo) {
        return robo.getMovimentoValido() + robo.getMovimentoInvalido();
    }
}