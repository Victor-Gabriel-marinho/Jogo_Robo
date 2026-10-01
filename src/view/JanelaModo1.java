package view;

import excecoes.MovimentoInvalidoException;
import model.robo.Robo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JanelaModo1 extends JFrame {

    private final Robo robo;
    private final PainelTabuleiro painel = new PainelTabuleiro();
    private final JLabel status = new JLabel("Defina a posição da comida.");
    private final JTextField campoX = new JTextField("3", 3);
    private final JTextField campoY = new JTextField("3", 3);
    private final JButton btnDefinir = new JButton("Definir comida");
    private final JButton btnCima = new JButton("Cima");
    private final JButton btnBaixo = new JButton("Baixo");
    private final JButton btnEsquerda = new JButton("Esquerda");
    private final JButton btnDireita = new JButton("Direita");

    private int alimX;
    private int alimY;
    private boolean comidaDefinida = false;
    private boolean fimDeJogo = false;

    public JanelaModo1(Robo robo) {
        super("Jogo Robô - Modo Player");
        this.robo = robo;
        painel.adicionarRobo(robo);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // topo: posição da comida
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topo.add(new JLabel("Comida  x:"));
        topo.add(campoX);
        topo.add(new JLabel("y:"));
        topo.add(campoY);
        topo.add(btnDefinir);
        add(topo, BorderLayout.NORTH);

        add(painel, BorderLayout.CENTER);

        // base: botões de movimento + status
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoes.add(btnEsquerda);
        botoes.add(btnCima);
        botoes.add(btnBaixo);
        botoes.add(btnDireita);

        JPanel base = new JPanel(new BorderLayout());
        base.add(botoes, BorderLayout.NORTH);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        base.add(status, BorderLayout.SOUTH);
        add(base, BorderLayout.SOUTH);

        btnDefinir.addActionListener(e -> definirComida());
        btnCima.addActionListener(e -> mover("up"));
        btnBaixo.addActionListener(e -> mover("down"));
        btnEsquerda.addActionListener(e -> mover("left"));
        btnDireita.addActionListener(e -> mover("right"));
        configurarTeclas();

        habilitarMovimentos(false);
        pack();
        setLocationRelativeTo(null);
    }

    private void definirComida() {
        int x;
        int y;
        try {
            x = Integer.parseInt(campoX.getText().trim());
            y = Integer.parseInt(campoY.getText().trim());
        } catch (NumberFormatException ex) {
            status.setText("Digite números inteiros para x e y.");
            return;
        }

        int max = PainelTabuleiro.LADO - 1;
        if (x < 0 || x > max || y < 0 || y > max) {
            status.setText("A comida deve ficar entre 0 e " + max + " em x e y.");
            return;
        }
        if (robo.encontrouAlimento(x, y)) {
            status.setText("A comida está na posição do robô. Escolha outra.");
            return;
        }

        alimX = x;
        alimY = y;
        comidaDefinida = true;
        painel.setAlimento(x, y);
        btnDefinir.setEnabled(false);
        campoX.setEnabled(false);
        campoY.setEnabled(false);
        habilitarMovimentos(true);
        status.setText("Posição do robô: (" + robo.getX() + ", " + robo.getY() + ")");
    }

    private void mover(String direcao) {
        if (!comidaDefinida || fimDeJogo) {
            return;
        }
        try {
            robo.mover(direcao);
            status.setText("Posição do robô: (" + robo.getX() + ", " + robo.getY() + ")");
        } catch (MovimentoInvalidoException e) {
            status.setText(e.getMessage());
        }
        painel.repaint();

        if (robo.encontrouAlimento(alimX, alimY)) {
            fimDeJogo = true;
            habilitarMovimentos(false);
            JOptionPane.showMessageDialog(this,
                    "O robô encontrou o alimento!\n"
                            + "Movimentos válidos: " + robo.getMovimentoValido() + "\n"
                            + "Movimentos inválidos: " + robo.getMovimentoInvalido());
        }
    }

    private void habilitarMovimentos(boolean ativo) {
        btnCima.setEnabled(ativo);
        btnBaixo.setEnabled(ativo);
        btnEsquerda.setEnabled(ativo);
        btnDireita.setEnabled(ativo);
    }

    // setas do teclado também movem o robô
    private void configurarTeclas() {
        JRootPane raiz = getRootPane();
        InputMap im = raiz.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = raiz.getActionMap();
        associar(im, am, "UP", "up");
        associar(im, am, "DOWN", "down");
        associar(im, am, "LEFT", "left");
        associar(im, am, "RIGHT", "right");
    }

    private void associar(InputMap im, ActionMap am, String tecla, String direcao) {
        im.put(KeyStroke.getKeyStroke(tecla), "mover-" + direcao);
        am.put("mover-" + direcao, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mover(direcao);
            }
        });
    }
}
