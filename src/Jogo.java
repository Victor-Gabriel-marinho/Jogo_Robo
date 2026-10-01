import app.Main1;

import java.util.Scanner;

public class Jogo {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("=== JOGO ROBÔS ===");
        System.out.println("Escolha um modo de jogo:");
        System.out.println("1 - Player");
        System.out.println("2 - Robo vs Robo");
        System.out.println("3 - Robo vs Robo Inteligente");
        System.out.println("4 - Obstaculos");

        int modo = scan.nextInt();

        switch (modo) {
            case 1: {
                javax.swing.SwingUtilities.invokeLater(() ->
                        new view.JanelaModo1(new model.robo.Robo("azul")).setVisible(true));
                break;
            }
        }
    }
}
