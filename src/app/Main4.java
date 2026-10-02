package app;

import model.obstaculo.Bomba;
import model.obstaculo.Obstaculo;
import model.obstaculo.Rocha;
import model.robo.Robo;
import model.robo.RoboInteligente;
import view.MainBase;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main4 extends MainBase {

    // tempo entre dois movimentos (um robô se move por vez)
    private static final int ATRASO_MS = 500;

    private final int alimX;
    private final int alimY;
    private final List<Obstaculo> obstaculos = new ArrayList<>();
    private int proximoId = 1;
    private boolean iniciado = false;

    private final JRadioButton opBomba = new JRadioButton("Bomba", true);
    private final JRadioButton opRocha = new JRadioButton("Rocha");
    private final JRadioButton opRemover = new JRadioButton("Remover");
    private final JButton btnIniciar = new JButton("Iniciar");

    public Main4(List<Robo> robos) {
        super("Jogo Robô - Modo Obstáculos", robos, false);
        int[] alimento = pedirAlimento();
        alimX = alimento[0];
        alimY = alimento[1];
        painel.setAlimento(alimX, alimY);
        painel.setObstaculos(obstaculos);
        painel.setAoClicarCelula(this::aoClicarCelula);

        add(criarBarra(), BorderLayout.NORTH);
        setStatus("Posicione bombas e rochas e clique em Iniciar.");
        exibir();
    }

    private JPanel criarBarra() {
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(opBomba);
        grupo.add(opRocha);
        grupo.add(opRemover);
        btnIniciar.addActionListener(e -> iniciar());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.CENTER));
        barra.add(opBomba);
        barra.add(opRocha);
        barra.add(opRemover);
        barra.add(btnIniciar);
        return barra;
    }

    // ---------- preparação do tabuleiro ----------

    private void aoClicarCelula(int x, int y) {
        if (iniciado) {
            return;
        }
        Obstaculo existente = obstaculoEm(x, y);

        if (opRemover.isSelected()) {
            if (existente != null) {
                obstaculos.remove(existente);
                setStatus("Obstáculo removido de (" + x + ", " + y + ").");
                painel.repaint();
            }
            return;
        }
        if (x == 0 && y == 0) {
            setStatus("(0,0) é a posição inicial dos robôs.");
            return;
        }
        if (x == alimX && y == alimY) {
            setStatus("Não dá para colocar obstáculo na comida.");
            return;
        }
        if (existente != null) {
            setStatus("Já existe obstáculo em (" + x + ", " + y + "). Use Remover.");
            return;
        }

        boolean bomba = opBomba.isSelected();
        obstaculos.add(bomba ? new Bomba(proximoId++, x, y) : new Rocha(proximoId++, x, y));
        setStatus((bomba ? "Bomba" : "Rocha") + " colocada em (" + x + ", " + y + ").");
        painel.repaint();
    }

    private void iniciar() {
        if (!comidaAlcancavel()) {
            setStatus("As rochas bloqueiam o caminho até a comida.");
            return;
        }
        iniciado = true;
        btnIniciar.setEnabled(false);
        opBomba.setEnabled(false);
        opRocha.setEnabled(false);
        opRemover.setEnabled(false);
        setStatus(" ");
        automatizar(ATRASO_MS, robo -> direcaoAleatoria());
    }

    /**
     * Existe caminho de (0,0) até a comida sem passar por rochas? (Bombas não bloqueiam:
     * se estiverem no caminho, os robôs explodem e o jogo termina do mesmo jeito.)
     * Sem esse caminho nenhum robô chegaria à comida e o jogo nunca acabaria.
     */
    private boolean comidaAlcancavel() {
        boolean[][] visitado = new boolean[Robo.TAMANHO][Robo.TAMANHO];
        Deque<int[]> fila = new ArrayDeque<>();
        fila.add(new int[]{0, 0});
        visitado[0][0] = true;
        int[][] passos = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!fila.isEmpty()) {
            int[] atual = fila.poll();
            if (atual[0] == alimX && atual[1] == alimY) {
                return true;
            }
            for (int[] p : passos) {
                int nx = atual[0] + p[0];
                int ny = atual[1] + p[1];
                if (nx < 0 || ny < 0 || nx >= Robo.TAMANHO || ny >= Robo.TAMANHO || visitado[nx][ny]) {
                    continue;
                }
                if (obstaculoEm(nx, ny) instanceof Rocha) {
                    continue;
                }
                visitado[nx][ny] = true;
                fila.add(new int[]{nx, ny});
            }
        }
        return false;
    }

    private Obstaculo obstaculoEm(int x, int y) {
        for (Obstaculo o : obstaculos) {
            if (o.estaEm(x, y)) {
                return o;
            }
        }
        return null;
    }

    // ---------- regras do jogo ----------

    @Override
    protected void aoMover(Robo robo) {
        Obstaculo obstaculo = obstaculoEm(robo.getX(), robo.getY());
        if (obstaculo != null) {
            obstaculo.bater(robo);
            if (obstaculo instanceof Bomba) {
                setStatus(descrever(robo) + " pisou numa bomba e explodiu!");
            } else {
                setStatus(descrever(robo) + " bateu numa rocha e voltou para ("
                        + robo.getX() + ", " + robo.getY() + ")");
            }
            obstaculos.removeIf(o -> !o.isAtivo()); // a bomba que explodiu some do tabuleiro
        }
        painel.repaint();

        if (robo.isExplodido()) {
            finalizados.add(robo); // não se move mais
            if (finalizados.size() == robos.size()) {
                encerrar(resumoFinal("Os dois robôs explodiram!"));
            } else {
                setAviso(descrever(robo) + " explodiu após " + totalMovimentos(robo) + " movimentos.");
            }
            return;
        }

        if (robo.encontrouAlimento(alimX, alimY)) {
            encerrar(resumoFinal(descrever(robo) + " encontrou o alimento!"));
        }
    }

    private String resumoFinal(String titulo) {
        StringBuilder sb = new StringBuilder(titulo).append("\n");
        for (Robo r : robos) {
            sb.append("\n").append(descrever(r))
                    .append(r instanceof RoboInteligente ? " [inteligente]: " : " [normal]: ")
                    .append(totalMovimentos(r)).append(" movimentos (")
                    .append(r.getMovimentoValido()).append(" válidos, ")
                    .append(r.getMovimentoInvalido()).append(" inválidos)");
            if (r.isExplodido()) {
                sb.append(" - explodiu");
            } else if (r.encontrouAlimento(alimX, alimY)) {
                sb.append(" - achou o alimento");
            } else {
                sb.append(" - não achou");
            }
        }
        return sb.toString();
    }
}