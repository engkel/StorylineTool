package domain;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.LinkedList;

public class Main extends Application {

    public static void main(String[] args)  {
        launch(args);
    }

    static GridPane menuPane;
    static Scene menuScene;
    static Stage primaryStage;

    static LinkedList<Note> notes = new LinkedList<Note>();

    // This ID can be used to more easily add support
    // for saving multiple projects in the same database.
    // Currently the projectID is hardcoded to always be 0.
    static public int projectID = 0;

    // Stores the user mode, false = single user (file), true = multi user (database)
    static boolean userMode;

    @Override
    public void start(Stage stage) throws Exception {
        // Create a reference to the primary stage
        // This allows changing the scene from other classes
        primaryStage = stage;

        // Will generate the start menu
        stage.setTitle("Storyline Tool");
        menuPane = UI.initStartMenu();
        menuScene = new Scene(menuPane, 1300, 750);

        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setScene(menuScene);
        stage.show();
    }
}
