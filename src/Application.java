public class Application {

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    private void run() {
        MenuClass menu = new MenuClass();
        while (true) {   //this loop is terminated in the mainMenu method, which allows the user to call system.exit to halt the program.
            menu.mainMenu();
        }
    }

}




