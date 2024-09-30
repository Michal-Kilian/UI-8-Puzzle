public class Testing {

    public static void main(String[] args) {
        App.main(args);
    }

    public static void main2(String[] args) {

        long totalTimeMS = 0;

        for (int i = 0; i < 1000; i++) {
            long startTime = System.nanoTime();

            App.main(args);
            
            long endTime   = System.nanoTime();
            totalTimeMS += endTime - startTime;
        }
        System.out.println("Average time in ms: " + totalTimeMS / 1000);
        
    }

}
