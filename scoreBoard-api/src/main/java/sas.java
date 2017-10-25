import java.util.concurrent.CompletableFuture;

public class sas {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<String> x = CompletableFuture.supplyAsync(() -> {
            return "hello";
        });
        x.thenApply(s -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("11");
            return s.toUpperCase();
        });

        x.thenApply(s -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("22");
            return s.toUpperCase();
        });
        Thread.sleep(10000);
    }


}
