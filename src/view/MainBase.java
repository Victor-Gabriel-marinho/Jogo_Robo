package view;

import excecoes.MovimentoInvalidoException;
import model.robo.Robo;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public abstract class MainBase extends JFrame {

    protected final List<Robo> robos;
    protected final PainelTabuleiro painel = new PainelTabuleiro();

    // robôs que já acharam a comida: o Timer deixa de movê-los
    protected final Set<Robo> finalizados = new HashSet<>();

    private final JLabel status = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel aviso = new JLabel(" ", SwingConstants.CENTER);
    private final Random sorteio = new Random();
    private Timer timer;
    private int vez = 0; // índice do próximo robô a se mover (rodízio)

    protected MainBase(String titulo, List<Robo> robos, boolean manual) {
        super(titulo);
        this.robos = robos;
        robos.forEach(painel::adicionarRobo);

        if (manual) {
            add(criarControles(), BorderLayout.NORTH);
        }
        add(painel, BorderLayout.CENTER);
        JPanel rodape = new JPanel(new GridLayout(2, 1));
        rodape.add(status);
        rodape.add(aviso);
        add(rodape, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel criarControles() {
        JPanel controles = new JPanel(new GridLayout(0, 1));
        for (Robo robo : robos) {
            JPanel linha = new JPanel();
            linha.add(new JLabel("Robô " + (robos.indexOf(robo) + 1)));
            for (String dir : Robo.DIRECOES) {
                JButton botao = new JButton(dir);
                botao.addActionListener(e -> mover(robo, dir));
                linha.add(botao);
            }
            controles.add(linha);
        }
        return controles;
    }

    /** Regra do modo: chamado depois de cada movimento, com o robô que se moveu. */
    protected abstract void aoMover(Robo robo);

    /**
     * Move os robôs sozinhos, um de cada vez: a cada delayMs apenas UM robô se move,
     * em rodízio, usando a estratégia para escolher a direção. Robôs finalizados são pulados.
     */
    protected void automatizar(int delayMs, Function<Robo, String> estrategia) {
        timer = new Timer(delayMs, e -> {
            Robo robo = proximoRobo();
            if (robo != null) {
                mover(robo, estrategia.apply(robo));
            }
        });
        timer.start();
    }

    /** Próximo robô do rodízio que ainda não terminou (null se todos terminaram). */
    private Robo proximoRobo() {
        for (int i = 0; i < robos.size(); i++) {
            Robo candidato = robos.get(vez);
            vez = (vez + 1) % robos.size();
            if (!finalizados.contains(candidato)) {
                return candidato;
            }
        }
        return null;
    }

    protected void parar() {
        if (timer != null) {
            timer.stop();
        }
    }

    protected String direcaoAleatoria() {
        return Robo.DIRECOES.get(sorteio.nextInt(Robo.DIRECOES.size()));
    }

    protected void mover(Robo robo, String direcao) {
        int invalidosAntes = robo.getMovimentoInvalido();
        try {
            robo.mover(direcao);
            String texto = descrever(robo) + ": (" + robo.getX() + ", " + robo.getY() + ")";
            if (robo.getMovimentoInvalido() > invalidosAntes) {
                // o RoboInteligente corrigiu sozinho um movimento inválido
                texto += " - corrigiu um movimento inválido";
            }
            setStatus(texto);
        } catch (MovimentoInvalidoException e) {
            setStatus(descrever(robo) + ": " + e.getMessage());
        }
        painel.repaint();
        aoMover(robo);
    }

    /** Ex.: "Robô 2 (azul)". */
    protected String descrever(Robo robo) {
        return "Robô " + (robos.indexOf(robo) + 1) + " (" + robo.getCor() + ")";
    }

    /** Movimentos feitos pelo robô (válidos + inválidos). */
    protected int totalMovimentos(Robo robo) {
        return robo.getMovimentoValido() + robo.getMovimentoInvalido();
    }

    /** Ex.: "Robô 2 (azul): 7 válidos, 2 inválidos". */
    protected String resumo(Robo robo) {
        return descrever(robo) + ": " + robo.getMovimentoValido() + " válidos, "
                + robo.getMovimentoInvalido() + " inválidos";
    }

    /** Legenda do último movimento (muda a cada jogada). */
    protected void setStatus(String texto) {
        status.setText(texto);
    }

    /** Linha de aviso que permanece na tela até ser trocada (não é sobrescrita pelos movimentos). */
    protected void setAviso(String texto) {
        aviso.setText(texto);
    }

    protected int pedirCoordenada(String eixo) {
        Integer[] opcoes = new Integer[Robo.TAMANHO];
        for (int i = 0; i < opcoes.length; i++) {
            opcoes[i] = i;
        }
        Object escolha = JOptionPane.showInputDialog(null, "Comida " + eixo + ":", "Comida",
                JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[opcoes.length - 1]);
        if (escolha == null) {
            System.exit(0);
        }
        return (int) escolha;
    }

    /** Pede x e y da comida, recusando a posição inicial dos robôs (0,0). */
    protected int[] pedirAlimento() {
        int x;
        int y;
        do {
            x = pedirCoordenada("x");
            y = pedirCoordenada("y");
            if (x == 0 && y == 0) {
                JOptionPane.showMessageDialog(null,
                        "A comida não pode ficar na posição inicial (0,0).");
            }
        } while (x == 0 && y == 0);
        return new int[]{x, y};
    }

    protected void encerrar(String mensagem) {
        parar();
        JOptionPane.showMessageDialog(this, mensagem);
        System.exit(0);
    }

    protected void exibir() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}