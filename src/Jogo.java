import app.Main1;
import app.Main2;
import app.Main3;
import app.Main4;
import model.robo.Robo;
import model.robo.RoboInteligente;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class Jogo {

    private static final String[] MODOS = {
            "Player", "Robo vs Robo", "Robo vs Robo Inteligente", "Obstáculos"};
    private static final String[] CORES = {
            "vermelho", "azul", "verde", "amarelo", "laranja", "roxo", "preto"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Jogo::iniciar);
    }

    private static void iniciar() {
        int modo = JOptionPane.showOptionDialog(null, "Escolha um modo de jogo:", "JOGO ROBÔS",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, MODOS, MODOS[0]);

        switch (modo) {
            case 0:
                new Main1(criarRobos(1, 0));
                break;
            case 1:
                new Main2(criarRobos(2, 0));
                break;
            case 2:
                new Main3(criarRobos(1, 1));
                break;
            case 3:
                new Main4(criarRobos(1, 1));
                break;
            default:
                System.exit(0); // fechou a janela sem escolher
        }
    }

    // cria primeiro os robôs normais e depois os inteligentes
    private static List<Robo> criarRobos(int normais, int inteligentes) {
        List<Robo> robos = new ArrayList<>();
        List<String> disponiveis = new ArrayList<>(List.of(CORES));

        for (int j = 0; j < normais + inteligentes; j++) {
            boolean inteligente = j >= normais;
            String quem = "Robô " + (j + 1) + (inteligente ? " (inteligente)" : " (normal)");

            Object escolha = JOptionPane.showInputDialog(null, "Escolha a cor do " + quem + ":",
                    "Cor do robô", JOptionPane.QUESTION_MESSAGE, null,
                    disponiveis.toArray(), disponiveis.get(0));
            if (escolha == null) {
                System.exit(0);
            }

            String cor = (String) escolha;
            disponiveis.remove(cor); // a próxima escolha não oferece essa cor
            robos.add(inteligente ? new RoboInteligente(cor) : new Robo(cor));
        }
        return robos;
    }
}