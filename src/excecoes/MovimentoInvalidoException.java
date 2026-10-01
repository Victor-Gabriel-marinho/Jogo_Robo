package excecoes;

public class MovimentoInvalidoException extends Exception {
    public MovimentoInvalidoException(String message) {
        super("Movimento invalido:  " + message);
    }
}
