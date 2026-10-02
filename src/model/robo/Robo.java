package model.robo;

import excecoes.MovimentoInvalidoException;

import java.util.List;

public class Robo {

    // lado da área de locomoção: coordenadas válidas de 0 a TAMANHO - 1
    public static final int TAMANHO = 4;

    // direções aceitas por mover(String)
    public static final List<String> DIRECOES = List.of("up", "down", "left", "right");

    private int X;
    private int Y;
    private int xAnterior;
    private int yAnterior;
    private String cor;
    private int movimentoValido;
    private int movimentoInvalido;
    private boolean explodido;

    public Robo(String cor) {
        this.X = 0;
        this.Y = 0;
        this.xAnterior = 0;
        this.yAnterior = 0;
        this.cor = cor;
        this.movimentoInvalido = 0;
        this.movimentoValido = 0;
        this.explodido = false;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getXAnterior() {
        return xAnterior;
    }

    public int getYAnterior() {
        return yAnterior;
    }

    public String getCor() {
        return cor;
    }

    public int getMovimentoInvalido() {
        return movimentoInvalido;
    }

    public int getMovimentoValido() {
        return movimentoValido;
    }

    public boolean isExplodido() {
        return explodido;
    }

    public void setPosicaoX(int X) {
        this.X = X;
    }

    public void setPosicaoY(int Y) {
        this.Y = Y;
    }

    public void mover(String direcao) throws MovimentoInvalidoException {
        if (explodido) {
            // robô explodido não anda mais (e a tentativa não conta como movimento)
            throw new MovimentoInvalidoException("robô explodido");
        }

        int novoX = X;
        int novoY = Y;

        switch (direcao) {
            case "up":    novoY++; break;
            case "down":  novoY--; break;
            case "right": novoX++; break;
            case "left":  novoX--; break;
            default:
                movimentoInvalido++;
                throw new MovimentoInvalidoException(direcao);
        }

        if (novoX < 0 || novoX >= TAMANHO || novoY < 0 || novoY >= TAMANHO) {
            movimentoInvalido++;
            throw new MovimentoInvalidoException(direcao);
        }

        xAnterior = X;
        yAnterior = Y;
        X = novoX;
        Y = novoY;
        movimentoValido++;
    }

    public void mover(int direcao) throws MovimentoInvalidoException {
        switch (direcao) {
            case 1: mover("up"); break;
            case 2: mover("down"); break;
            case 3: mover("right"); break;
            case 4: mover("left"); break;
            default:
                movimentoInvalido++;
                throw new MovimentoInvalidoException("código " + direcao);
        }
    }

    public boolean encontrouAlimento(int xAlim, int yAlim) {
        return X == xAlim && Y == yAlim;
    }

    /** Usado pela Rocha: desfaz o último movimento e passa a contá-lo como inválido. */
    public void voltarParaPosicaoAnterior() {
        if (X == xAnterior && Y == yAnterior) {
            return; // não há movimento a desfazer
        }
        X = xAnterior;
        Y = yAnterior;
        movimentoValido--;
        movimentoInvalido++;
    }

    /** Usado pela Bomba: o robô explode e não anda mais. */
    public void explodir() {
        explodido = true;
    }
}