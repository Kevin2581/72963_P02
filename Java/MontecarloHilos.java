import java.util.Random;

public class MonteCarloConcurrente {

    private static int contadorGlobal = 0;
    private static final Object cerrojo = new Object();

    static class HiloCalculador extends Thread {
        private final int muestras;
        private final int idHilo;

        public HiloCalculador(int muestras, int idHilo) {
            this.muestras = muestras;
            this.idHilo = idHilo;
        }

        @Override
        public void run() {
            Random generador = new Random();
            int contadorLocal = 0;

            for (int i = 0; i < muestras; i++) {
                double x = generador.nextDouble();
                double y = generador.nextDouble();
                if (x * x + y * y <= 1.0) {
                    contadorLocal++;
                }
            }

            synchronized (cerrojo) {
                System.out.println("Hilo " + idHilo + " añade " + contadorLocal + " puntos al total.");
                contadorGlobal += contadorLocal;
            }
        }
    }

    public static void main(String[] args) {
        int totalMuestras = 1_000_000;
        int cantidadHilos = 4;
        int muestrasPorHilo = totalMuestras / cantidadHilos;

        Thread[] hilos = new Thread[cantidadHilos];

        long inicio = System.currentTimeMillis();

        for (int i = 0; i < cantidadHilos; i++) {
            hilos[i] = new HiloCalculador(muestrasPorHilo, i);
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long fin = System.currentTimeMillis();

        double piEstimado = 4.0 * contadorGlobal / totalMuestras;

        System.out.println("\nTotal de puntos generados: " + totalMuestras);
        System.out.println("Puntos dentro del círculo: " + contadorGlobal);
        System.out.println("Estimación de π: " + piEstimado);
        System.out.println("Error absoluto: " + Math.abs(piEstimado - Math.PI));
        System.out.println("Tiempo de ejecución: " + (fin - inicio) + " ms");
    }
}

//Kevin del Jesus Gonzalez Maas - 72963