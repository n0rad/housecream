package net.awired.housecream.client.poc;


public class Main {

    public static void main(String[] args) throws Throwable {
        new HccPocServer(8080, PocNotify.class).start();

        while (true) {
            Thread.sleep(1000);
        }
    }
}
