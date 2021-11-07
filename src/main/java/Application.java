public class Application {
    public static void main(String[] args) {
        try {
            new Game().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}