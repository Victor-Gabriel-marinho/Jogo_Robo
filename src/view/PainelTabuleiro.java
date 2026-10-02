package view;

import model.obstaculo.Bomba;
import model.obstaculo.Obstaculo;
import model.obstaculo.Rocha;
import model.robo.Robo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class PainelTabuleiro extends JPanel {

    // lado da área em células (coordenadas de 0 a LADO - 1)
    public static final int LADO = Robo.TAMANHO;
    private static final int CELULA_PADRAO = 110;

    private final List<Robo> robos = new ArrayList<>();
    private List<Obstaculo> obstaculos = new ArrayList<>();
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

    /** Passa a desenhar esta lista (a mesma referência: mudanças na lista aparecem no próximo repaint). */
    public void setObstaculos(List<Obstaculo> obstaculos) {
        this.obstaculos = obstaculos;
        repaint();
    }

    /** Avisa com as coordenadas (x, y) da célula clicada (y cresce para cima, como no jogo). */
    public void setAoClicarCelula(BiConsumer<Integer, Integer> acao) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cel = Math.min(getWidth(), getHeight()) / LADO;
                if (cel <= 0) {
                    return;
                }
                int x = e.getX() / cel;
                int y = LADO - 1 - e.getY() / cel;
                if (x >= 0 && x < LADO && y >= 0 && y < LADO) {
                    acao.accept(x, y);
                }
            }
        });
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

        // obstáculos ativos
        for (Obstaculo o : obstaculos) {
            if (!o.isAtivo()) {
                continue;
            }
            int px = o.getX() * cel;
            int py = (LADO - 1 - o.getY()) * cel;
            if (o instanceof Bomba) {
                desenharBomba(g2, px, py, cel);
            } else if (o instanceof Rocha) {
                desenharRocha(g2, px, py, cel);
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
            if (r.isExplodido()) {
                desenharExplosao(g2, px, py, tam, corDe(r.getCor()));
                continue;
            }
            g2.setColor(corDe(r.getCor()));
            g2.fillRoundRect(px, py, tam, tam, 14, 14);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(px, py, tam, tam, 14, 14);
        }

        g2.dispose();
    }

    private static void desenharBomba(Graphics2D g2, int px, int py, int cel) {
        int d = cel * 45 / 100;
        int cx = px + cel / 2;
        int cy = py + cel / 2 + cel / 12;
        g2.setColor(new Color(40, 40, 40));
        g2.fillOval(cx - d / 2, cy - d / 2, d, d);
        g2.setColor(new Color(255, 255, 255, 90));
        g2.fillOval(cx - d / 4, cy - d / 3, d / 4, d / 4);
        g2.setColor(new Color(120, 80, 40));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(cx + d / 4, cy - d / 2 + 2, cx + d / 2, cy - d / 2 - cel / 10);
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(240, 60, 40));
        g2.fillOval(cx + d / 2 - 4, cy - d / 2 - cel / 10 - 4, 8, 8);
    }

    private static void desenharRocha(Graphics2D g2, int px, int py, int cel) {
        int w = cel * 60 / 100;
        int h = cel * 45 / 100;
        int rx = px + (cel - w) / 2;
        int ry = py + (cel - h) / 2 + cel / 12;
        g2.setColor(new Color(130, 130, 135));
        g2.fillRoundRect(rx, ry, w, h, h, h);
        g2.setColor(new Color(90, 90, 95));
        g2.drawRoundRect(rx, ry, w, h, h, h);
        g2.setColor(new Color(175, 175, 180));
        g2.fillOval(rx + w / 6, ry + h / 6, w / 4, h / 4);
    }

    // robô explodido: um X na cor dele (com contorno escuro)
    private static void desenharExplosao(Graphics2D g2, int px, int py, int tam, Color cor) {
        g2.setStroke(new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(Color.BLACK);
        g2.drawLine(px, py, px + tam, py + tam);
        g2.drawLine(px + tam, py, px, py + tam);
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(cor);
        g2.drawLine(px, py, px + tam, py + tam);
        g2.drawLine(px + tam, py, px, py + tam);
        g2.setStroke(new BasicStroke(1));
    }
}