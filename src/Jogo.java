import app.Main1;
import app.Main2;
import app.Main3;
import app.Main4;
import model.robo.Robo;
import model.robo.RoboInteligente;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Jogo {

    private static final String[] cores = {"vermelho", "azul", "verde", "amarelo", "laranja", "roxo", "preto"};
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== JOGO ROBÔS ===");
        System.out.println("Escolha um modo de jogo:");
        System.out.println("1 - Player");
        System.out.println("2 - Robo vs Robo");
        System.out.println("3 - Robo vs Robo Inteligente");
        System.out.println("4 - Obstaculos");

        int modo = lerInteiro("> ", 1, 4);

        switch (modo) {
            case 1: {
                List<Robo> rs = criarRobos(1, 0);
                SwingUtilities.invokeLater(() -> new Main1(rs));
                break;
            }
            case 2: {
                List<Robo> rs = criarRobos(2, 0);
                SwingUtilities.invokeLater(() -> new Main2(rs));
                break;
            }
            case 3: {
                List<Robo> rs = criarRobos(1, 1);
                SwingUtilities.invokeLater(() -> new Main3(rs));
                break;
            }
            case 4: {
                List<Robo> rs = criarRobos(1, 1);
                SwingUtilities.invokeLater(() -> new Main4(rs));
                break;
            }
            default:
                System.out.println("Modo inválido.");
        }
    }

    // lê um inteiro entre min e max, repetindo a pergunta enquanto a entrada for inválida
    private static int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem);
            if (scan.hasNextInt()) {
                int valor = scan.nextInt();
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } else {
                scan.next(); // descarta o texto inválido
            }
            System.out.println("Valor inválido. Digite um número de " + min + " a " + max + ".");
        }
    }

    // cria primeiro os robôs normais e depois os inteligentes
    private static List<Robo> criarRobos(int normais, int inteligentes) {
        List<Robo> robos = new ArrayList<>();
        Set<String> usadas = new HashSet<>();

        for (int j = 0; j < normais + inteligentes; j++) {
            boolean inteligente = j >= normais;
            String quem = "Robô " + (j + 1) + (inteligente ? " (inteligente)" : " (normal)");
            String cor = escolherCor(quem, usadas);
            usadas.add(cor);
            robos.add(inteligente ? new RoboInteligente(cor) : new Robo(cor));
        }

        return robos;
    }

    private static String escolherCor(String quem, Set<String> usadas) {
        while (true) {
            System.out.println("Escolha a cor do " + quem + ":");
            for (int i = 0; i < cores.length; i++) {
                System.out.println(i + " " + cores[i]);
            }
            String cor = cores[lerInteiro("> ", 0, cores.length - 1)];
            if (!usadas.contains(cor)) {
                return cor;
            }
            System.out.println("Essa cor já foi escolhida por outro robô.");
        }
    }
}