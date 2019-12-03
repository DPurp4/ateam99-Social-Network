package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.Optional;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 750;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "Social Network Visualizer";


    // This method creates a pop-up window that displays alerts
    private void displayAlertBox(String message){
        Label label = new Label(message);
        Stage window = new Stage();
        window.setTitle("Alert");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(256);
        window.setMinHeight(128);

        Button button = new Button("OK");
        button.setOnAction(e -> window.close());

        VBox layout = new VBox(16);
        layout.getChildren().addAll(label , button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        // welcome window

        /**
         *Top panel - Contains welcome label
         */
        Label topLabel = new Label("Welcome to Social Network Visualizer!");
        BorderPane root = new BorderPane();
        root.setTop(topLabel);

        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(15, 12, 15, 12));
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-background-color: #336699;");

        Button login = new Button("Log In");

        login.setPrefSize(100, 20);
        Button signUp = new Button("Sign Up");

        signUp.setPrefSize(100, 20);
        Button loadPrev = new Button("Load Prev");
        loadPrev.setPrefSize(100, 20);
        Button reportError = new Button("Report Error");
        reportError.setPrefSize(100, 20);
        bottomBox.getChildren().addAll(login, signUp, loadPrev, reportError);
        root.setBottom(bottomBox);

/**
 *Center panel - logo/graphic for social network visualizer
 */
        FileInputStream input = new FileInputStream("graphic.png");
        Image graphic = new Image(input); // graphic for social network
        ImageView iv2 = new ImageView(); // necessary to display image
        iv2.setImage(graphic);
        iv2.setFitWidth(250);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setCache(true);
        root.setCenter(iv2); // image set in center panel

        Scene welcomeScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main Windows
        BorderPane mainPane = new BorderPane();

        // left
        setLeftPaneUI(mainPane);

        // center
        setLabelPaneUI(mainPane);
        setButtonPaneUI(mainPane);

        // bottom
        setBottomPaneUI(primaryStage, mainPane);

        // window setup

        login.setOnAction(e -> {
            Scene mainScene = new Scene(mainPane, 1000, 1000);
            primaryStage.setScene(mainScene);
        });

        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Social Network");
        primaryStage.show();
    }


    private void setBottomPaneUI(Stage primaryStage, BorderPane mainPane) {
        HBox bottomHBox = new HBox();
        bottomHBox.setPadding(new Insets(0, 0, 10 ,20));
        mainPane.setBottom(bottomHBox);

        Button reportError = new Button("Report Error");
        Button save = new Button("Save");
        Pane largeSpacer = new Pane();
        largeSpacer.setMinSize(775, 1);
        Pane smallSpacer = new Pane();
        smallSpacer.setMinSize(10, 1);

        Button exit = new Button("Exit");
        bottomHBox.getChildren().add(exit);
        bottomHBox.getChildren().addAll(smallSpacer);
        bottomHBox.getChildren().add(reportError);
        bottomHBox.getChildren().addAll(largeSpacer);
        bottomHBox.getChildren().add(save);

        Alert alert = new Alert(Alert.AlertType.NONE);

        exit.setOnAction(e -> {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to exit, all unsaved data will be gone.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                primaryStage.close();
            else
                alert.close();
        });


        save.setOnAction(e -> {
            System.out.println("SAVE CLICKED");
        });
    }

    private void setLeftPaneUI(BorderPane mainPane) {
        ObservableList<String> list = FXCollections.observableArrayList();
        ListView<String> leftListView = new ListView<>(list);
        leftListView.setPrefWidth(150);
        leftListView.setPrefHeight(150);
        list.add("Person 1");
        list.add("Person 2");
        list.add("Person 3");
        list.add("Person 4");
        list.add("Person 5");
        mainPane.setLeft(leftListView);
    }

    private void setButtonPaneUI(BorderPane mainPane) {
        Button addPerson = new Button("Add Person");
        Button removePerson = new Button("Remove Person");
        Button clearNetwork = new Button("Clear Network");
        Button load = new Button("Load Network");
        Button export = new Button("Export Network");
        Button display = new Button("Display Network");
        Button addFriend = new Button("Add Friend");
        Button removeFriend = new Button("Remove Friend");
        Button central = new Button("Central User Perspective");
        Button setCentral = new Button("Set Central User");
        GridPane buttonGrid = new GridPane();
        buttonGrid.add(addPerson, 0, 0, 1, 1);
        buttonGrid.add(removePerson, 1, 0, 1, 1);
        buttonGrid.add(clearNetwork, 2, 0, 1, 1);
        buttonGrid.add(load, 0, 1, 1, 1);
        buttonGrid.add(export, 1, 1, 1, 1);
        buttonGrid.add(display, 2, 1, 1, 1);
        buttonGrid.add(addFriend, 0, 2, 1, 1);
        buttonGrid.add(removeFriend, 1, 2, 1, 1);
        buttonGrid.add(setCentral, 0, 3, 1, 1);
        buttonGrid.add(central, 1, 3, 1, 1);
        mainPane.setCenter(buttonGrid);
    }

    private void setLabelPaneUI(BorderPane mainPane) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        Label status = new Label("Status");
        Label name = new Label("Name");
        Label friendList = new Label("Friend List");
        Label socialNetwork = new Label("Social Network");
        Label mutualFriend = new Label("Mutual Friends");
        gridPane.add(status, 0, 0, 2, 2);
        gridPane.add(name, 0, 2, 2, 2);
        gridPane.add(friendList, 0, 4, 2, 2);
        gridPane.add(socialNetwork, 0, 6, 2, 2);
        gridPane.add(mutualFriend, 0, 8, 2, 2);
        mainPane.setRight(gridPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
