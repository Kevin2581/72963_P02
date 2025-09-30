import java.util.Random;

public class MonteCarloPi {

    public static void main(String[] args) {
        int muestrasTotales = 1_000_000;
        int dentroDelCirculo = 0;

        Random generador = new Random();
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < muestrasTotales; i++) {
            double coordX = generador.nextDouble();
            double coordY = generador.nextDouble();

            if (coordX * coordX + coordY * coordY <= 1.0) {
                dentroDelCirculo++;
            }
        }

        long fin = System.currentTimeMillis();

        double piEstimado = 4.0 * dentroDelCirculo / muestrasTotales;

        System.out.println("Total de muestras: " + muestrasTotales);
        System.out.println("Puntos dentro del círculo: " + dentroDelCirculo);
        System.out.println("Estimación de π: " + piEstimado);
        System.out.println("Error absoluto: " + Math.abs(piEstimado - Math.PI));
        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");
    }
}

//Kevin del Jesus Gonzalez Maas - 72963