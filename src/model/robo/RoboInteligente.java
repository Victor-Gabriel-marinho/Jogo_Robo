package model.robo;

import excecoes.MovimentoInvalidoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoboInteligente extends Robo {

    private final Random sorteio = new Random();
    private final List<String> invalidas = new ArrayList<>();

    public RoboInteligente(String cor) {
        super(cor);
    }


    @Override
    public void mover(String direcao) throws MovimentoInvalidoException {
        // se essa direção já falhou neste mesmo lugar, troca por outra
        if (invalidas.contains(direcao)) {
            direcao = sortearOutra();
        }
        try {
            super.mover(direcao);
            invalidas.clear(); // o robô saiu do lugar: as falhas anteriores não valem mais
        } catch (MovimentoInvalidoException falha) {
            if (DIRECOES.contains(direcao)) {
                invalidas.add(direcao);
            }
            throw falha;
        }
    }

    private String sortearOutra() {
        List<String> alternativas = new ArrayList<>(DIRECOES);
        alternativas.removeAll(invalidas);
        if (alternativas.isEmpty()) {
            invalidas.clear();
            alternativas = new ArrayList<>(DIRECOES);
        }
        return alternativas.get(sorteio.nextInt(alternativas.size()));
    }
}