package view;

import excecoes.MovimentoInvalidoException;
import model.robo.Robo;
import view.PainelTabuleiro;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Function;

public abstract class MainBase extends JFrame {

    protected static final String[] DIRECOES = {"up", "down", "left", "right"};

    protected final List<Robo> robos;
    protected final PainelTabuleiro painel = new PainelTabuleiro();
    private final JLabel status = new JLabel(" ", SwingConstants.CENTER);
    private Timer timer;

    protected MainBase(String titulo, List<Robo> robos, boolean manual) {
        super(titulo);
        this.robos = robos;
        robos.forEach(painel::adicionarRobo);

        if (manual) {
            add(criarControles(), BorderLayout.NORTH);
        }
        add(painel, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel criarControles() {
        JPanel controles = new JPanel(new GridLayout(0, 1));
        for (Robo robo : robos) {
            JPanel linha = new JPanel();
            linha.add(new JLabel("Robô " + (robos.indexOf(robo) + 1)));
            for (String dir : DIRECOES) {
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

    /** Move os robôs sozinhos a cada delayMs, usando a estratégia para escolher a direção. */
    protected void automatizar(int delayMs, Function<Robo, String> estrategia) {
        timer = new Timer(delayMs, e -> {
            for (Robo robo : robos) {
                if (!timer.isRunning()) {
                    break;
                }
                mover(robo, estrategia.apply(robo));
            }
        });
        timer.start();
    }

    protected void parar() {
        if (timer != null) {
            timer.stop();
        }
    }

    protected void mover(Robo robo, String direcao) {
        try {
            robo.mover(direcao);
            setStatus("Robô " + (robos.indexOf(robo) + 1)
                    + ": (" + robo.getX() + ", " + robo.getY() + ")");
        } catch (MovimentoInvalidoException e) {
            setStatus(e.getMessage());
        }
        painel.repaint();
        aoMover(robo);
    }

    protected void setStatus(String texto) {
        status.setText(texto);
    }

    protected int pedirCoordenada(String eixo) {
        Object escolha = JOptionPane.showInputDialog(null, "Comida " + eixo + ":", "Comida",
                JOptionPane.QUESTION_MESSAGE, null, new Integer[]{0, 1, 2, 3}, 3);
        if (escolha == null) {
            System.exit(0);
        }
        return (int) escolha;
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