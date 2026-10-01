package model.robo;

import excecoes.MovimentoInvalidoException;

public class Robo {
    private int X;
    private int Y;
    private String cor;
    private int movimentoValido;
    private int movimentoInvalido;


    public Robo (String cor){
        this.X = 0;
        this.Y = 0;
        this.cor = cor;
        movimentoInvalido = 0;
        movimentoValido = 0;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
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

    public void setPosicaoX(int X) {
        this.X = X;
    }

    public void setPosicaoY(int Y) {
        this.Y = Y;
    }

    public void mover (String direcao) throws MovimentoInvalidoException {
        int novoX = X;
        int novoY = Y;

        switch (direcao) {
            case "up": novoY++;break;
            case "down": novoY--;break;
            case "right": novoX++;break;
            case "left": novoX--;break;
            default:
                throw new MovimentoInvalidoException(direcao);
        }

        if (novoX < 0 || novoX > 3 || novoY<0 || novoY >3){
            movimentoInvalido++;
            throw  new MovimentoInvalidoException(direcao);
        }

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
                throw new MovimentoInvalidoException("código " + direcao);
        }
    }

    public boolean encontrouAlimento (int XAlim, int YAlmim) {
        if (X == XAlim && Y == YAlmim){
            return true;
        }
        return false;
    }
}
