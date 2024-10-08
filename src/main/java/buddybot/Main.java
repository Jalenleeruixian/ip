package buddybot;

import java.io.IOException;
//message
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private BuddyBot buddy = new BuddyBot();

    @Override

    public void start(Stage stage) {
        //@@author david-eom
        //Reused from https://github.com/david-eom/CS2103T-IP
        //with minor modifications
        try {
            FXMLLoader myFxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane anchorPane = myFxmlLoader.load();

            stage.setTitle("BuddyBot");

            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            myFxmlLoader.<MainWindow>getController().setBuddy(buddy);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}