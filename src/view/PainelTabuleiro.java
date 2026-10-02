package view;

import model.robo.Robo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PainelTabuleiro extends JPanel {

    // lado da área em células (coordenadas de 0 a LADO - 1)
    public static final int LADO = Robo.TAMANHO;
    private static final int CELULA_PADRAO = 110;

    private final List<Robo> robos = new ArrayList<>();
    private int alimX = -1;
    private int alimY = -1;

    public PainelTabuleiro() {
        setPreferredSize(new Dimension(LADO * CELULA_PADRAO, LADO * CELULA_PADRAO));
        setBackground(Color.WHITE);
    }

    public void adicionarRobo(Robo robo) {
        robos.add(robo);
        repaint();
    }

    public void setAlimento(int x, int y) {
        this.alimX = x;
        this.alimY = y;
        repaint();
    }

    // converte o nome da cor do robô (String) em java.awt.Color
    private static Color corDe(String nome) {
        if (nome == null) {
            return Color.GRAY;
        }
        switch (nome.trim().toLowerCase()) {
            case "vermelho": case "red":    return new Color(220, 50, 47);
            case "azul":     case "blue":   return new Color(38, 110, 220);
            case "verde":    case "green":  return new Color(40, 160, 70);
            case "amarelo":  case "yellow": return new Color(235, 190, 30);
            case "laranja":  case "orange": return new Color(240, 130, 30);
            case "roxo":     case "purple": return new Color(140, 70, 190);
            case "preto":    case "black":  return Color.BLACK;
            default:                        return Color.GRAY;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cel = Math.min(getWidth(), getHeight()) / LADO;

        // células (y cresce para cima, então a linha 0 fica embaixo)
        for (int x = 0; x < LADO; x++) {
            for (int y = 0; y < LADO; y++) {
                int px = x * cel;
                int py = (LADO - 1 - y) * cel;
                g2.setColor(new Color(245, 245, 245));
                g2.fillRect(px, py, cel, cel);
                g2.setColor(Color.GRAY);
                g2.drawRect(px, py, cel, cel);
                g2.setColor(new Color(170, 170, 170));
                g2.drawString("(" + x + "," + y + ")", px + 5, py + 14);
            }
        }

        // comida
        if (alimX >= 0 && alimY >= 0) {
            int px = alimX * cel;
            int py = (LADO - 1 - alimY) * cel;
            int d = cel / 2;
            g2.setColor(new Color(240, 130, 30));
            g2.fillOval(px + (cel - d) / 2, py + (cel - d) / 2, d, d);
            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(px + (cel - d) / 2, py + (cel - d) / 2, d, d);
        }

        // robôs (se dividirem a célula, ficam levemente deslocados)
        int tam = cel / 2;
        for (int i = 0; i < robos.size(); i++) {
            Robo r = robos.get(i);
            int desloc = i * cel / 8;
            int px = r.getX() * cel + (cel - tam) / 2 + desloc;
            int py = (LADO - 1 - r.getY()) * cel + (cel - tam) / 2 + desloc;
            g2.setColor(corDe(r.getCor()));
            g2.fillRoundRect(px, py, tam, tam, 14, 14);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(px, py, tam, tam, 14, 14);
        }

        g2.dispose();
    }
}