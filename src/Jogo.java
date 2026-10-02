import app.Main1;
import app.Main2;
import model.robo.Robo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jogo {

    private static String[] cores = {"vermelho", "azul", "verde", "amarelo", "laranja", "roxo", "preto"};
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== JOGO ROBÔS ===");
        System.out.println("Escolha um modo de jogo:");
        System.out.println("1 - Player");
        System.out.println("2 - Robo vs Robo");
        System.out.println("3 - Robo vs Robo Inteligente");
        System.out.println("4 - Obstaculos");

        int modo = scan.nextInt();

        switch (modo) {
            case 1: {
                List<Robo> rs = criarRobos(1);
                javax.swing.SwingUtilities.invokeLater(() ->
                        new Main1(rs));
                break;
            }
            case 2: {
                List<Robo> rs = criarRobos(2);
                javax.swing.SwingUtilities.invokeLater(() ->
                        new Main2(rs));
                break;
            }
            }
        }

    private static List<Robo> criarRobos(int quantidadeRobos) {

        List<Robo> robos = new ArrayList<>();

        System.out.println("Escolha a cor do Robô(s):");
        for (int j = 0; j < quantidadeRobos; j++) {

            for (int i = 0; i < 7; i++) {

                System.out.println(i + " " + cores[i]);

            }

            int corRobo = scan.nextInt();
            robos.add(new Robo(cores[corRobo]));
        }

        return robos;
    }

}
