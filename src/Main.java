import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time

        List<Future<?>> threadList = new ArrayList<>();

        ExecutorService threadPool = Executors.newFixedThreadPool(25);


        for (String text : texts) {
            threadList.add(threadPool.submit(() -> {
                int maxSize = 0;

                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println("выполнил " + Thread.currentThread().getName());
                return text.substring(0, 100) + " -> " + maxSize;

            }));
        }

        threadList.forEach(x -> {
            try {
                System.out.println(x.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        long endTs = System.currentTimeMillis(); // end time

        threadPool.shutdown();
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }

        return text.toString();
    }
}