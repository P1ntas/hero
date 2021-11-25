public class Application {

    public static void main(String[] args) {
        try {
            Game game = new Game(60, 30);
            game.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}