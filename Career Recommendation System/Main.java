import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize Database
        DatabaseSetup.initializeDatabase();

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CareerGui().setVisible(true);
            }
        });
    }
}
