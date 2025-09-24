package Java;

import java.util.Random;

public class MonteCarloHilos {
    // Contador total compartido por todos los hilos
    private static long globlalCount = 0; 
    // Objeto para asegurar exclusión mutua
    private static final Object lock = new Object();

    // Clase que representa la tarea que ejecuta cada hilo
    static class MonteCarloPI extends Thread {
        private final int numSamples;
        private final int threadId;

        public MonteCarloPI(int numSamples, int threadId) {
            this.numSamples = numSamples;
            this.threadId = threadId;
        }

        @Override 
        public void run() {
            Random rand = new Random();
            long localCount = 0;

            // Generar coordenadas aleatorias y verificar si caen dentro del círculo
            for(int i = 0; i < numSamples; i++) {
                double x = rand.nextDouble();
                double y = rand.nextDouble();
                if(x * x + y * y <= 1.0) {
                    localCount++;
                }
            }
            
            // Actualización del contador global (zona protegida)
            synchronized (lock) {
                System.out.println("Hilo " + threadId + ": suma " + localCount + " al total");
                globlalCount += localCount;
            }
        }
    }

    public static void main(String[] args){
        int totalSamples = 1_000_000; 
        int numThreads = 4; 
        int samplesPerThread = totalSamples / numThread;

        Thread[] threads = new Thread[numThreads];

        // Crear los hilos y ponerlos en ejecución
        for (int i = 0; i < numThreads; i++){
            threads[i] = new MonteCarloPI(samplesPerThread, i);
            threads[i].start();
        }

        // Esperar que todos los hilos finalicen antes de continuar
        for(int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Calcular la aproximación de PI y mostrar los resultados
        double piApprox = (4.0 * globlalCount) / totalSamples;
        System.out.println("Cantidad de puntos generados: " + totalSamples);
        System.out.println("Puntos que cayeron dentro del círculo: " + globlalCount);
        System.out.println("Valor aproximado de pi: " + piApprox);
        System.out.println("Diferencia con Math.PI: " + Math.abs(piApprox - Math.PI));
    }
}
