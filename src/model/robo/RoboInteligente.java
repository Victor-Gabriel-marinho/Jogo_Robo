package model.robo;

import excecoes.MovimentoInvalidoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoboInteligente extends Robo {

    private final Random sorteio = new Random();

    public RoboInteligente(String cor) {
        super(cor);
    }

    /**
     * Sobrescreve mover(String). Como mover(int) delega para mover(String),
     * este comportamento vale para as duas versões.
     *
     * Se a direção pedida for inválida, o robô NÃO repete essa direção: ele sorteia
     * entre as outras até conseguir um movimento válido. Cada tentativa inválida
     * continua sendo contada (a contagem é feita pelo Robo).
     */
    @Override
    public void mover(String direcao) throws MovimentoInvalidoException {
        try {
            super.mover(direcao);
        } catch (MovimentoInvalidoException falha) {
            // só corrige direções conhecidas; texto desconhecido continua sendo erro
            if (!DIRECOES.contains(direcao)) {
                throw falha;
            }

            List<String> alternativas = new ArrayList<>(DIRECOES);
            alternativas.remove(direcao); // nunca repete a direção que acabou de falhar

            while (!alternativas.isEmpty()) {
                String outra = alternativas.remove(sorteio.nextInt(alternativas.size()));
                try {
                    super.mover(outra);
                    return; // movimento válido: terminou
                } catch (MovimentoInvalidoException tambemInvalida) {
                    // também inválida (já contada): sorteia outra entre as que sobraram
                }
            }

            // nenhuma direção funcionou (não acontece na área sem obstáculos)
            throw falha;
        }
    }
}