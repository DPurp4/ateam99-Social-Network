package application;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 750;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "Social Network Visualizer";
    private static final String USER_PROFILE_TXT = "./user_profile.txt";
    private static final String N_A = "N/A";


    private BorderPane welcomePane = new BorderPane();
    private BorderPane mainPane = new BorderPane();
    private Button loginButton = new Button("Log In");
    private Button signUpButton = new Button("Sign Up");
    private TextField usernameTextField = new TextField();
    private PasswordField passwordTextField = new PasswordField();
    private Scene mainScene = new Scene(mainPane, 1000, 1000);
    private Scene welcomeScene = new Scene(welcomePane, WINDOW_WIDTH, WINDOW_HEIGHT);
    private Stage simpleAlert = new Stage();

    private Hashtable<String, String> userProfiles = new Hashtable<>();

    private Button addPersonButton = new Button("Add Person");
    private Button removePersonButton = new Button("Remove Person");
    private Button clearNetworkButton = new Button("Clear Network");
    private Button loadNetworkButton = new Button("Load Network");
    private Button exportNetworkButton = new Button("Export Network");
    private Button displayButton = new Button("Display Network");
    private Button addFriendButton = new Button("Add Friend");
    private Button removeFriendButton = new Button("Remove Friend");
    private Button centralButton = new Button("Central User Perspective");
    private Button setCentralButton = new Button("Set Central User");

    private Button[] mainButtons = {addPersonButton, removePersonButton, clearNetworkButton, loadNetworkButton, exportNetworkButton, displayButton, addFriendButton, removeFriendButton, centralButton, setCentralButton};

    private boolean isNetworkLoaded = false;
    private SocialNetwork socialNetwork = new SocialNetwork();

    private ObservableList<String> peopleList = FXCollections.observableArrayList();
    private ListView<String> leftListView = new ListView<>(peopleList);

    private Label statusLabel = new Label(N_A);
    private Label nameLabel = new Label(N_A);
    private Label friendListLabel = new Label(N_A);
    private Label socialNetworkLabel = new Label(N_A);
    private Label mutualFriendLabel = new Label(N_A);

    private void setupWelcomeBorderPane(Stage primaryStage) {
        setupWelcomeUI();
        // setup button handler
        handleLogin(primaryStage);
        handleSignUp(primaryStage);
    }

    private void setupWelcomeUI() {
        // setup welcome label
        Label welcomeLabel = new Label("Welcome to Social Network Visualizer!");
        welcomeLabel.setFont(Font.font("Verdana", 20));
        welcomeLabel.setMaxWidth(Double.MAX_VALUE);
        AnchorPane.setLeftAnchor(welcomeLabel, 0.0);
        AnchorPane.setRightAnchor(welcomeLabel, 0.0);
        welcomeLabel.setAlignment(Pos.CENTER);
        welcomeLabel.setPadding(new Insets(36, 0, 0, 0));
        welcomePane.setTop(welcomeLabel);

        // setup center box which displays text fields
        VBox centerBox = new VBox();
        HBox usernameBox = new HBox();
        HBox passwordBox = new HBox();

        // set username
        usernameTextField.setPromptText("Username");
        usernameBox.getChildren().addAll(new Label("Username"), usernameTextField);
        usernameBox.setSpacing(10);

        // set password
        passwordTextField.setPromptText("Password");
        passwordBox.getChildren().addAll(new Label("Password"), passwordTextField);
        passwordBox.setSpacing(10);

        // set center
        centerBox.getChildren().addAll(usernameBox, passwordBox);
        centerBox.setSpacing(8);
        usernameBox.setAlignment(Pos.CENTER);
        passwordBox.setAlignment(Pos.CENTER);
        centerBox.setAlignment(Pos.CENTER);
        welcomePane.setCenter(centerBox);

        // set bottom
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(15, 12, 15, 12));
        bottomBox.setSpacing(10);
        bottomBox.setStyle("-fx-background-color: #336699;");

        // set buttons in bottom
        loginButton.setPrefSize(100, 20);
        signUpButton.setPrefSize(100, 20);

        bottomBox.getChildren().addAll(loginButton, signUpButton);
        welcomePane.setBottom(bottomBox);
    }

    private void setupMainBorderPane(Stage primaryStage) {
        setLeftPaneUI(mainPane);
        setupMainLabels(mainPane);
        setupMainButtons(mainPane, primaryStage);
        setBottomPaneUI(mainPane, primaryStage);
    }

    private void setupUserProfile() {
        File userProfile = new File(USER_PROFILE_TXT);
        try {
            boolean ignored = userProfile.createNewFile();
            Scanner profileScanner = new Scanner(userProfile);
            // load all user profiles
            while (profileScanner.hasNextLine()) {
                String singleProfile = profileScanner.nextLine();
                String[] singleProfileInfo = singleProfile.split(":");
                String username = singleProfileInfo[0];
                String password = singleProfileInfo[1];
                userProfiles.put(username, password);
            }

        } catch (IOException e) {
            displayAlertBox("Error: Unable to load user_profile.txt" + e.getLocalizedMessage(), null);
        }
    }

    private void initialize() {
        simpleAlert.setTitle("Alert");
        simpleAlert.initModality(Modality.APPLICATION_MODAL);
        simpleAlert.setMinWidth(256);
        simpleAlert.setMinHeight(128);
    }

    @Override
    public void start(Stage primaryStage) {
        initialize();
        // setup user profile
        setupUserProfile();
        // setup panes
        setupWelcomeBorderPane(primaryStage);
        setupMainBorderPane(primaryStage);

        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
    }

    private void handleSignUp(Stage primaryStage) {
        signUpButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                displayAlertBox("Both username and password should not be empty", null);
                return;
            }

            if (password.length() < 5 || username.length() < 5) {
                displayAlertBox("Both username and password should has more than 4 characters", null);
                return;
            }

            if (userProfiles.containsKey(username)) {
                displayAlertBox("Username has already been used", null);
                return;
            }

            File userProfile = new File(USER_PROFILE_TXT);

            try {
                FileWriter fileWriter = new FileWriter(userProfile);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(username + ":" + password);
                printWriter.close();
                userProfiles.put(username, password);
                displayAlertBox("Sign up succeed", event -> {
                    primaryStage.setScene(mainScene);
                    simpleAlert.close();
                });
            } catch (IOException err) {
                displayAlertBox("Error: Unable to load user_profile.txt" + err.getLocalizedMessage(), null);
            }

        });
    }

    private void handleLogin(Stage primaryStage) {
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                displayAlertBox("Both username and password should not be empty", null);
                return;
            }

            if (!userProfiles.containsKey(username)) {
                displayAlertBox("This user is the user profile, please sign up", null);
                return;
            }

            if (userProfiles.get(username).equals(password)) {
                displayAlertBox("Log in succeed", event -> {
                    primaryStage.setScene(mainScene);
                    simpleAlert.close();
                });
            } else {
                displayAlertBox("Incorrect password or username", null);
            }
        });
    }


    private void setBottomPaneUI(BorderPane mainPane, Stage primaryStage) {
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(0, 0, 10 ,20));
        mainPane.setBottom(bottomBox);

        Button reportError = new Button("Report Error");
        Button save = new Button("Save");
        Pane largeSpacer = new Pane();
        largeSpacer.setMinSize(775, 1);
        Pane smallSpacer = new Pane();
        smallSpacer.setMinSize(10, 1);

        Button exit = new Button("Exit");
        bottomBox.getChildren().add(exit);
        bottomBox.getChildren().addAll(smallSpacer);
        bottomBox.getChildren().add(reportError);
        bottomBox.getChildren().addAll(largeSpacer);
        bottomBox.getChildren().add(save);

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
        handleListSelection(leftListView);
        leftListView.setPrefWidth(150);
        leftListView.setPrefHeight(150);
        mainPane.setLeft(leftListView);
    }

    private void handleListSelection(ListView<String> leftListView) {
        leftListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            nameLabel.setText(selectedPersonName);
            LinkedList<String> friendList = new LinkedList<>();
            for (Person person : socialNetwork.getFriends(selectedPersonName))
                friendList.add(person.name());
            friendListLabel.setText(friendList.toString().replace("[", "").replace("]", ""));
        });
    }

    private void setupMainButtons(BorderPane mainPane, Stage primaryStage) {
        setupMainButtonsUI(mainPane);
        handleLoadNetwork(primaryStage);
        handleClearNetwork();
        handleExportNetwork(primaryStage);
        handleAddPerson();
        handleRemovePerson();
        handleAddFriend();
        removeFriendButton.setOnAction(e -> {
            if (leftListView.getSelectionModel().getSelectedItem() == null) {
                displayAlertBox("Select a person on the left first", null);
                return;
            }
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            Stage removeFriendStage = new Stage();
            removeFriendStage.setTitle("Remove Friend of " + selectedPersonName);
            removeFriendStage.initModality(Modality.APPLICATION_MODAL);
            removeFriendStage.setMinWidth(256);
            removeFriendStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button addButton = new Button("Remove");

            addButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }

                boolean hasThisFriend = false;
                for (Person friend : socialNetwork.getFriends(selectedPersonName)) {
                    if (friend.name().equals(personName)) {
                        hasThisFriend = true;
                        break;
                    }
                }

                if (!hasThisFriend) {
                    displayAlertBox("This person is not his/her friend", null);
                    return;
                }

                socialNetwork.removeFriends(selectedPersonName, personName);
                removeFriendStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField, addButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            removeFriendStage.setScene(scene);
            removeFriendStage.showAndWait();
        });
    }

    private void handleAddFriend() {
        addFriendButton.setOnAction(e -> {
            if (leftListView.getSelectionModel().getSelectedItem() == null) {
                displayAlertBox("Select a person on the left first", null);
                return;
            }
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            Stage addFriendStage = new Stage();
            addFriendStage.setTitle("Add Friend to " + selectedPersonName);
            addFriendStage.initModality(Modality.APPLICATION_MODAL);
            addFriendStage.setMinWidth(256);
            addFriendStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button addButton = new Button("Add");

            addButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }
                if (!peopleList.contains(personName)) {
                    displayAlertBox("This person does not exist", null);
                    return;
                }
                socialNetwork.addFriends(selectedPersonName, personName);
                addFriendStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField , addButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            addFriendStage.setScene(scene);
            addFriendStage.showAndWait();
        });
    }

    private void handleRemovePerson() {
        removePersonButton.setOnAction(e -> {
            Stage removePersonStage = new Stage();
            removePersonStage.setTitle("Remove Person");
            removePersonStage.initModality(Modality.APPLICATION_MODAL);
            removePersonStage.setMinWidth(256);
            removePersonStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button removeButton = new Button("Remove");

            removeButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }
                socialNetwork.removeUser(personName);
                reloadPeopleList();
                removePersonStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField , removeButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            removePersonStage.setScene(scene);
            removePersonStage.showAndWait();
        });
    }

    private void handleAddPerson() {
        addPersonButton.setOnAction(e -> {
            Stage addPersonStage = new Stage();
            addPersonStage.setTitle("Add Person");
            addPersonStage.initModality(Modality.APPLICATION_MODAL);
            addPersonStage.setMinWidth(256);
            addPersonStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button addButton = new Button("Add");

            addButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.contains(" ") || personNameTextField.getText().contains("\"")) {
                    displayAlertBox("Person name should not contains space or quotation mark", null);
                    return;
                }
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }
                socialNetwork.addUser(personName);
                reloadPeopleList();
                addPersonStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField , addButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            addPersonStage.setScene(scene);
            addPersonStage.showAndWait();
        });
    }

    private void handleExportNetwork(Stage primaryStage) {
        exportNetworkButton.setOnAction(e -> {
            DirectoryChooser exportDirectoryChooser = new DirectoryChooser();
            File exportTemp = exportDirectoryChooser.showDialog(primaryStage);
            if (exportTemp != null) {
                File exportFile = new File(exportTemp.getAbsolutePath() + "/export.txt");
                socialNetwork.saveToFile(exportFile);
            }
            else
                displayAlertBox("Something is wrong with your system", null);
        });
    }

    private void handleClearNetwork() {
        clearNetworkButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to clear network, everything will be gone");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // TODO: more things needed to be cleaned
                socialNetwork = new SocialNetwork();
                peopleList.clear();
                for (Button button : mainButtons)
                    button.setDisable(!button.getText().equals("Load Network"));
            }
            else
                alert.close();
        });
    }

    private void handleLoadNetwork(Stage primaryStage) {
        loadNetworkButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Network File");
            File inputFile = fileChooser.showOpenDialog(primaryStage);
            if (inputFile != null) {
                if (loadFile(inputFile)) {
                    for (Button button : mainButtons)
                        button.setDisable(false);
                    isNetworkLoaded = true;
                }
                else
                    displayAlertBox("Error, unable to load this file", null);
            } else {
                displayAlertBox("Error: something is wrong with the file", null);
            }
        });
    }

    private boolean loadFile(File file) {
        socialNetwork.loadFromFile(file);
        // TODO, need to check if the file is valid
        reloadPeopleList();
        return true;
    }

    private void reloadPeopleList() {
        peopleList.clear();
        for (Person person : socialNetwork.getAllPeople())
            peopleList.add(person.name());
    }

    private void setupMainButtonsUI(BorderPane mainPane) {
        Label networkLabel = new Label("Network Operations");
        HBox networkBox = new HBox();
        networkBox.setSpacing(8);

        Label peopleLabel = new Label("People Operations");
        HBox peopleBox = new HBox();
        peopleBox.setSpacing(8);

        Label userLabel = new Label("User Operations");
        HBox userBox = new HBox();
        userBox.setSpacing(8);

        for (Button button : mainButtons) {
            if (button.getText().contains("Network"))
                networkBox.getChildren().add(button);
            else if (!button.getText().contains("User"))
                peopleBox.getChildren().add(button);
            else
                userBox.getChildren().add(button);
        }

        VBox centerBox = new VBox(networkLabel, networkBox, peopleLabel, peopleBox, userLabel, userBox);
        centerBox.setSpacing(8);
        centerBox.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setCenter(centerBox);

        // if there is no network loaded, disable all but load network button
        if (!isNetworkLoaded)
            for (Button button : mainButtons)
                button.setDisable(!button.getText().equals("Load Network"));
    }

    private void setupMainLabels(BorderPane mainPane) {
        VBox rightBox = new VBox();
        rightBox.setSpacing(8);
        final String placeholder = "                  ";
        Label statusTitleLabel = new Label("Status: " + placeholder);
        statusTitleLabel.setFont(Font.font("Verdana", 16));
        Label nameTitleLabel = new Label("Name: " + placeholder);
        nameTitleLabel.setFont(Font.font("Verdana", 16));
        Label friendListTitleLabel = new Label("Friend List: " + placeholder);
        friendListTitleLabel.setFont(Font.font("Verdana", 16));
        Label socialNetworkTitleLabel = new Label("Social Network: " + placeholder);
        socialNetworkTitleLabel.setFont(Font.font("Verdana", 16));
        Label mutualFriendTitleLabel = new Label("Mutual Friends: " + placeholder);
        mutualFriendTitleLabel.setFont(Font.font("Verdana", 16));

        friendListLabel.setWrapText(true);

        rightBox.getChildren().addAll(
                statusTitleLabel,
                statusLabel,
                nameTitleLabel,
                nameLabel,
                friendListTitleLabel,
                friendListLabel,
                socialNetworkTitleLabel,
                socialNetworkLabel,
                mutualFriendTitleLabel,
                mutualFriendLabel
        );

        mainPane.setRight(rightBox);
    }

    public static void main(String[] args) {
        launch(args);
    }

    // This method creates a pop-up window that displays alerts
    private void displayAlertBox(String message, EventHandler<ActionEvent> event){
        Label label = new Label(message);

        Button button = new Button("OK");
        button.setOnAction(Objects.requireNonNullElseGet(event, () -> e -> simpleAlert.close()));

        VBox layout = new VBox(16);
        layout.getChildren().addAll(label , button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        simpleAlert.setScene(scene);
        simpleAlert.showAndWait();
    }
}

