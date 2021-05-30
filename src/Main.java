import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.LinkedList;

public class Main extends Application {

    public static void main(String[] args)  {
        launch(args);
    }

    static GridPane menuPane;
    static BorderPane storylinePane = new BorderPane();

    static Scene menuScene;
    static Scene storylineScene;

    static Stage primaryStage;

    static LinkedList<Note> notes = new LinkedList<Note>();

    @Override
    public void start(Stage stage) throws Exception {
        // Testing Notes, Remove once UI functionality works
        //notes.add(new Note("My first note.", 2, 1));
        //notes.add(new Note("My 2nd note.", 4, 1));
        //notes.add(new Note("My third note.", 4, 2));
        //notes.add(new Note("My test note.", 6, 2));
        //notes.add(new Note("My 12345 note.", 8, 1));
        //notes.add(new Note("My 12345 note.", 10, 3));
        //notes.add(new Note("My 12345 note.", 12, 3));
        //notes.add(new Note("My 12345 note.", 12, 1));
        //notes.add(new Note("My 12345 note.", 14, 1));
        //notes.add(new Note("My 12345 note.", 14, 4));
        //notes.add(new Note("My 12345 note.", 16, 3));
        //notes.add(new Note("My 12345 note.", 16, 4));
        //notes.add(new Note("My 12345 note.", 18, 2));
        //notes.add(new Note("My 12345 note.", 18, 3));
        //notes.add(new Note("My 12345 note.", 20, 1));
        //notes.add(new Note("My 12345 note.", 20, 2));

        // Create a reference to the primary stage
        // This allows changing the scene from other classes
        primaryStage = stage;

        // Will generate the start menu
        stage.setTitle("Storyline Tool");
        menuPane = UI.initStartMenu();
        System.out.println(menuPane.getChildren());
        menuScene = new Scene(menuPane, 1300, 750);

        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage.setScene(menuScene);
        stage.show();
    }
}
