///////////////////////////////////////////////////////////////////////////////////////////////////
//                  ALL STUDENTS COMPLETE THESE SECTIONS
// Title:           Social Network Visualizer Program
// Files:           Graph.java, GraphADT.java, Main.java, Person.java,
//             SociaNetwork.java, SocialNetworkADT.java
// Semester:        Autumn 2019
//
// ATeam members:  Devin DuBeau, LEC 002, ddubeau@wisc.edu, dubeau
//                 Mihir Arora, LEC 001, mrarora@wisc.edu, marora
//                 Xiaoyuan Liu, LEC 001, xliu798@wisc.edu, xiaoyuanl
//                 Yuehan Qin, LEC 001, yqin43@wisc.edu, yuehan
//                 Reid Chen, LEC 001, ychen878@wisc.edu, reid
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////////////////////////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of
//                   of any information you find.
//////////////////////////// 100 columns wide /////////////////////////////////////////////////////
package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    // some useful constants
    private static final int WINDOW_WIDTH = 750;
    private static final int WINDOW_HEIGHT = 500;
    private static final String APP_TITLE = "Social Network Visualizer";
    private static final String USER_PROFILE_TXT = "./user_profile.txt";
    private static final String N_A = "N/A";

    // panes
    private BorderPane welcomePane = new BorderPane();
    private BorderPane mainPane = new BorderPane();
    private Button loginButton = new Button("Log In");
    private Button signUpButton = new Button("Sign Up");
    private TextField usernameTextField = new TextField();
    private PasswordField passwordTextField = new PasswordField();

    // scenes and alert
    private Scene mainScene = new Scene(mainPane, 1200, 1000);
    private Scene welcomeScene = new Scene(welcomePane, WINDOW_WIDTH, WINDOW_HEIGHT);
    private Stage simpleAlert = new Stage();

    // user profile to login and signup to the system
    private Hashtable<String, String> userProfiles = new Hashtable<>();

    // all buttons
    private Button addPersonButton = new Button("Add Person");
    private Button removePersonButton = new Button("Remove Person");
    private Button clearNetworkButton = new Button("Clear Network");
    private Button loadNetworkButton = new Button("Load Network");
    private Button exportNetworkButton = new Button("Export Network");
    private Button componentsInNetworkButton = new Button("Show Network Components");
    private Button addFriendButton = new Button("Add Friend");
    private Button removeFriendButton = new Button("Remove Friend");
    private Button findMutualFriendButton = new Button("Find Mutual Friend");
    private Button findShortestPathFriendButton = new Button("Shortest Path");

    // array to manage buttons
    private Button[] mainButtons = {
            addPersonButton,
            removePersonButton,
            clearNetworkButton,
            loadNetworkButton,
            exportNetworkButton,
            addFriendButton,
            removeFriendButton,
            findMutualFriendButton,
            findShortestPathFriendButton,
            componentsInNetworkButton
    };

    // network information
    private boolean isNetworkLoaded = false;
    private SocialNetwork socialNetwork = new SocialNetwork();

    // list to store people
    private ObservableList<String> peopleList = FXCollections.observableArrayList();
    private ListView<String> leftListView = new ListView<>(peopleList);

    // text area and label to display information
    private TextArea statusTextArea = new TextArea(N_A);
    private Label nameLabel = new Label(N_A);
    private TextArea friendListTextArea = new TextArea(N_A);
    private TextArea networkInfoTextArea = new TextArea(N_A);

    /**
     * to setup welcome gui and button actions
     * @param primaryStage
     */
    private void setupWelcomeBorderPane(Stage primaryStage) {
        // setup welcome UI
        setupWelcomeUI();
        // setup button handler
        handleLogin(primaryStage);
        handleSignUp(primaryStage);
    }

    /**
     * to setup welcome UI
     */
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

    /**
     * to setup main pane by regions
     * @param primaryStage
     */
    private void setupMainBorderPane(Stage primaryStage) {
        setLeftPaneUI(mainPane);
        setupMainLabels(mainPane);
        setupMainButtons(mainPane, primaryStage);
        setBottomPaneUI(mainPane, primaryStage);
    }

    /**
     * setup user profile for login and singup, user here is not related to network
     */
    private void setupUserProfile() {
        File userProfile = new File(USER_PROFILE_TXT);
        try {
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
            displayAlertBox("Error: Unable to load user_profile.txt" +
                    e.getLocalizedMessage(), null);
        }
    }

    /**
     * initialize some default value for the program
     */
    private void initialize() {
        simpleAlert.setTitle("Alert");
        simpleAlert.initModality(Modality.APPLICATION_MODAL);
        simpleAlert.setMinWidth(512);
        simpleAlert.setMinHeight(128);
    }

    /**
     * start of the program
     * @param primaryStage
     */
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

    /**
     * handle sign up action
     * @param primaryStage
     */
    private void handleSignUp(Stage primaryStage) {
        signUpButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            // check if empty
            if (username.isEmpty() || password.isEmpty()) {
                displayAlertBox("Both username and " +
                        "password should not be empty", null);
                return;
            }
            // check password length
            if (password.length() < 5 || username.length() < 5) {
                displayAlertBox("Both username and password should " +
                        "has more than 4 characters", null);
                return;
            }
            // check existence of username
            if (userProfiles.containsKey(username)) {
                displayAlertBox("Username has already been used", null);
                return;
            }

            // load profile
            File userProfile = new File(USER_PROFILE_TXT);

            try {
                // write new user info
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
                displayAlertBox("Error: Unable to load" +
                        "user_profile.txt" + err.getLocalizedMessage(), null);
            }

        });
    }

    /**
     * handle login action
     * @param primaryStage
     */
    private void handleLogin(Stage primaryStage) {
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            // check empty
            if (username.isEmpty() || password.isEmpty()) {
                displayAlertBox("Both username and" +
                        "password should not be empty", null);
                return;
            }
            // check existence of username
            if (!userProfiles.containsKey(username)) {
                displayAlertBox("This user is the" +
                        "user profile, please sign up", null);
                return;
            }
            // check if password is correct
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


    /**
     * setup bottom pane
     * @param mainPane
     * @param primaryStage
     */
    private void setBottomPaneUI(BorderPane mainPane, Stage primaryStage) {
        HBox bottomBox = new HBox();
        bottomBox.setPadding(new Insets(10, 10, 10 ,10));
        mainPane.setBottom(bottomBox);
        // setup exit button
        Button exit = new Button("Exit");
        bottomBox.getChildren().add(exit);
        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
        ButtonType saveButton = new ButtonType("Save and Exit",
                ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.NONE,"Are you sure you want to exit?" +
                "All unsaved data will be gone.", exitButton, saveButton);
        // setup button action
        exit.setOnAction(e -> {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == exitButton)
                primaryStage.close();
            else {
                if (peopleList.size() == 0) {
                    primaryStage.close();
                    return;
                }
                DirectoryChooser exportDirectoryChooser = new DirectoryChooser();
                File exportTemp = exportDirectoryChooser.showDialog(primaryStage);
                if (exportTemp != null) {
                    File exportFile = new File(exportTemp.getAbsolutePath() +
                            "/export.txt");
                    socialNetwork.saveToFile(exportFile);
                    primaryStage.close();
                }
                else {
                    displayAlertBox("Please select a directory", null);
                }
            }
        });
    }

    /**
     * setup left pane
     * @param mainPane
     */
    private void setLeftPaneUI(BorderPane mainPane) {
        handleListSelection(leftListView);
        leftListView.setPrefWidth(150);
        leftListView.setPrefHeight(150);
        mainPane.setLeft(leftListView);
    }

    /**
     * handle list selectionaction
     * @param leftListView
     */
    private void handleListSelection(ListView<String> leftListView) {
        leftListView.getSelectionModel().selectedItemProperty().addListener(e -> {
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            socialNetwork.setCentralUser(selectedPersonName);
            nameLabel.setText(selectedPersonName);
            updateCentralUserInfo(selectedPersonName);
        });
    }

    /**
     * update central user information
     * @param selectedPersonName
     */
    private void updateCentralUserInfo(String selectedPersonName) {
        LinkedList<String> friendList = new LinkedList<>();
        if (socialNetwork.getFriends(selectedPersonName) == null) {
            statusTextArea.setText("This user has no friend, how pathetic!");
            friendListTextArea.setText(":(");
        } else {

            for (Person person : socialNetwork.getFriends(selectedPersonName))
                friendList.add(person.name());
            // update friends number
            String friend_s = "";
            if (friendList.size() == 1)
                friend_s = " friend.";
            else
                friend_s = " friends.";

            // update people size
            int peopleSize = socialNetwork.getAllPeople().size();
            String peopleSizeInfo;
            if (peopleSize == 1)
                peopleSizeInfo = "There is 1 person in the network\n";
            else
                peopleSizeInfo = "There are " + peopleSize + " people in the network\n";

            // update component size
            int componentSize = socialNetwork.getConnectedComponents().size();
            String componentInfo;
            if (componentSize == 1)
                componentInfo = "There is 1 component in the network\n";
            else
                componentInfo = "There are " + componentSize + " components in the network\n";

            networkInfoTextArea.setText(peopleSizeInfo + componentInfo);

            statusTextArea.setText("This user has " + friendList.size() + friend_s);
            friendListTextArea.setText(friendList.toString().replace("[", "").
                    replace("]", ""));
        }
    }

    private void setupMainButtons(BorderPane mainPane, Stage primaryStage) {
        setupMainButtonsUI(mainPane);
        handleLoadNetwork(primaryStage);
        handleClearNetwork();
        handleExportNetwork(primaryStage);
        handleAddPerson();
        handleRemovePerson();
        handleAddFriend();
        handleRemoveFriends();
        handleFindMutualFriends();
        handleFindShortestPathFriend();
        handleComponentsInNetwork();
    }

    /**
     * handle components in network action
     */
    private void handleComponentsInNetwork() {
        componentsInNetworkButton.setOnAction(e -> {

            Set<Graph> components = socialNetwork.getConnectedComponents();
            // calculate height
            int heightFactor = components.size();
            LinkedList<Node> nodes = new LinkedList<>();
            for (Graph component : components) {
                heightFactor++;
                LinkedList<String> componentItem = new LinkedList<>();
                for (Person person : component.getAllNodes()) {
                    componentItem.add(person.name());
                }
                nodes.add(new Label(componentItem.toString().replace("]", "")
                        .replace("[", "")));
            }

            Stage showComponentsStage = new Stage();
            showComponentsStage.setTitle("Showing Components");
            showComponentsStage.initModality(Modality.APPLICATION_MODAL);
            showComponentsStage.setMinWidth(512);
            showComponentsStage.setMinHeight(heightFactor * 30);

            VBox layout = new VBox(16);
            layout.getChildren().addAll(nodes);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            showComponentsStage.setScene(scene);
            showComponentsStage.showAndWait();
        });
    }

    /**
     * handle find shortest path
     */
    private void handleFindShortestPathFriend() {
        findShortestPathFriendButton.setOnAction(e -> {
            if (leftListView.getSelectionModel().getSelectedItem() == null) {
                displayAlertBox("Select a person on the left first", null);
                return;
            }
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            Stage shortestPathStage = new Stage();
            shortestPathStage.setTitle("Find the shortest connect between the given person");
            shortestPathStage.initModality(Modality.APPLICATION_MODAL);
            shortestPathStage.setMinWidth(512);
            shortestPathStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button findButton = new Button("Find");

            findButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                // check alot of things
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }
                if (!peopleList.contains(personName)) {
                    displayAlertBox("This person does not exist", null);
                    return;
                }

                if (socialNetwork.getFriends(selectedPersonName).contains(personName)) {
                    displayAlertBox("You two are freinds", null);
                    return;
                }

                LinkedList<String> shortestPathList = new LinkedList<>();
                List<Person> shortest = socialNetwork.getShortestPath(selectedPersonName, personName);

                if (shortest == null) {
                    displayAlertBox("You two have no possible connection", null);
                    return;
                }

                for (Person person : shortest)
                    shortestPathList.add(person.name());

                String info = "Shortest connection with " + personName + " is: " +
                        shortestPathList.toString().replace("]", "").
                                replace("[", "");
                displayAlertBox(info, null);
                reloadPeopleList();
                shortestPathStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField , findButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            shortestPathStage.setScene(scene);
            shortestPathStage.showAndWait();
        });
    }

    /**
     * handle find mutual friends
     */
    private void handleFindMutualFriends() {
        findMutualFriendButton.setOnAction(e -> {
            if (leftListView.getSelectionModel().getSelectedItem() == null) {
                displayAlertBox("Select a person on the left first", null);
                return;
            }
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            Stage findMutualFriendStage = new Stage();
            findMutualFriendStage.setTitle("Find mutual friend with " + selectedPersonName);
            findMutualFriendStage.initModality(Modality.APPLICATION_MODAL);
            findMutualFriendStage.setMinWidth(512);
            findMutualFriendStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button findButton = new Button("Find");

            // check multiple things
            findButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.isEmpty()) {
                    displayAlertBox("Person name should not be empty", null);
                    return;
                }
                if (!peopleList.contains(personName)) {
                    displayAlertBox("This person does not exist", null);
                    return;
                }
                Set<Person> mutualFriends = socialNetwork.getMutualFriends(selectedPersonName,
                        personName);
                LinkedList<String> mutualFriendNames = new LinkedList<>();
                for (Person friend : mutualFriends) {
                    mutualFriendNames.add(friend.name());
                }

                if (mutualFriendNames.size() == 0) {
                    String info = "No Mutual Friend";
                    displayAlertBox(info, null);
                    findMutualFriendStage.close();
                    return;
                }

                String friend_is_are = "";
                if (mutualFriendNames.size() == 1)
                    friend_is_are = "friend is";
                else
                    friend_is_are = "friends are";

                String info = "Mutual " + friend_is_are + ": " +
                        mutualFriendNames.toString().replace("]",
                                "").replace("[", "");
                displayAlertBox(info, null);
                findMutualFriendStage.close();
            });

            VBox layout = new VBox(16);
            layout.getChildren().addAll(personNameTextField , findButton);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout);
            findMutualFriendStage.setScene(scene);
            findMutualFriendStage.showAndWait();
        });
    }

    /**
     * handle remove friends
     */
    private void handleRemoveFriends() {
        removeFriendButton.setOnAction(e -> {
            if (leftListView.getSelectionModel().getSelectedItem() == null) {
                displayAlertBox("Select a person on the left first", null);
                return;
            }
            String selectedPersonName = leftListView.getSelectionModel().getSelectedItem();
            Stage removeFriendStage = new Stage();
            removeFriendStage.setTitle("Remove Friend of " + selectedPersonName);
            removeFriendStage.initModality(Modality.APPLICATION_MODAL);
            removeFriendStage.setMinWidth(512);
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
                reloadPeopleList();
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

    /**
     * handle add friend
     */
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
            addFriendStage.setMinWidth(512);
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
                reloadPeopleList();
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

    /**
     * handle remove person
     */
    private void handleRemovePerson() {
        removePersonButton.setOnAction(e -> {
            Stage removePersonStage = new Stage();
            removePersonStage.setTitle("Remove Person");
            removePersonStage.initModality(Modality.APPLICATION_MODAL);
            removePersonStage.setMinWidth(512);
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
                peopleList.remove(personName);
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

    /**
     * handle add person
     */
    private void handleAddPerson() {
        addPersonButton.setOnAction(e -> {
            Stage addPersonStage = new Stage();
            addPersonStage.setTitle("Add Person");
            addPersonStage.initModality(Modality.APPLICATION_MODAL);
            addPersonStage.setMinWidth(512);
            addPersonStage.setMinHeight(128);

            TextField personNameTextField = new TextField();
            personNameTextField.setPromptText("Enter person name");
            Button addButton = new Button("Add");

            addButton.setOnAction(event -> {
                String personName = personNameTextField.getText();
                if (personName.contains(" ") || personNameTextField.getText().contains("\"")) {
                    displayAlertBox("Person name should not contains space or" +
                            "quotation mark", null);
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

    /**
     * handle export network
     * @param primaryStage stage of the program
     */
    private void handleExportNetwork(Stage primaryStage) {
        exportNetworkButton.setOnAction(e -> {
            DirectoryChooser exportDirectoryChooser = new DirectoryChooser();
            File exportTemp = exportDirectoryChooser.showDialog(primaryStage);
            if (exportTemp != null) {
                File exportFile = new File(exportTemp.getAbsolutePath() + "/export.txt");
                socialNetwork.saveToFile(exportFile);
            }
            else
                displayAlertBox("Please select a directory", null);
        });
    }

    /**
     * clear everything in the network
     */
    private void handleClearNetwork() {
        clearNetworkButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to clear network, everything will be gone");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                socialNetwork = new SocialNetwork();
                peopleList.clear();
                for (Button button : mainButtons)
                    button.setDisable(!button.getText().equals("Load Network"));
                statusTextArea.setText(N_A);
                networkInfoTextArea.setText(N_A);
                friendListTextArea.setText(N_A);
            }
            else
                alert.close();
        });
    }

    /**
     * load things into network
     * @param primaryStage program's stage
     */
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
                displayAlertBox("Please select a file", null);
            }
        });
    }

    /**
     * load file
     * @param file input file
     * @return true if success
     */
    private boolean loadFile(File file) {
        socialNetwork.loadFromFile(file);
        reloadPeopleList();
        return true;
    }

    /**
     * reload data
     */
    private void reloadPeopleList() {
        for (Person person : socialNetwork.getAllPeople()) {
            if (!peopleList.contains(person.name()))
                peopleList.add(person.name());
        }
        leftListView.getSelectionModel().select(socialNetwork.getCentralUser());
        updateCentralUserInfo(socialNetwork.getCentralUser());
    }

    /**
     * setup main buttons GUI
     * @param mainPane
     */
    private void setupMainButtonsUI(BorderPane mainPane) {
        Label networkLabel = new Label("Network Operations");
        HBox networkBox = new HBox();
        networkBox.setSpacing(8);

        Label peopleLabel = new Label("People Operations");
        HBox peopleBox = new HBox();
        peopleBox.setSpacing(8);

        for (Button button : mainButtons) {
            if (button.getText().contains("Network"))
                networkBox.getChildren().add(button);
            else if (!button.getText().contains("User"))
                peopleBox.getChildren().add(button);
        }

        VBox centerBox = new VBox(networkLabel, networkBox, peopleLabel, peopleBox);
        centerBox.setSpacing(8);
        centerBox.setPadding(new Insets(10, 10, 10, 10));
        mainPane.setCenter(centerBox);

        // if there is no network loaded, disable all but load network button
        if (!isNetworkLoaded)
            for (Button button : mainButtons)
                button.setDisable(!button.getText().equals("Load Network"));
    }

    /**
     * setup labels
     * @param mainPane pane to display this section
     */
    private void setupMainLabels(BorderPane mainPane) {
        VBox rightBox = new VBox();

        friendListTextArea.setEditable(false);
        friendListTextArea.setWrapText(true);
        friendListTextArea.setMaxWidth(270);

        statusTextArea.setEditable(false);
        statusTextArea.setWrapText(true);
        statusTextArea.setMaxWidth(270);

        networkInfoTextArea.setEditable(false);
        networkInfoTextArea.setWrapText(true);
        networkInfoTextArea.setMaxWidth(270);

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
        Label networkInfoLabel = new Label("Network Information: " + placeholder);
        networkInfoLabel.setFont(Font.font("Verdana", 16));

        rightBox.getChildren().addAll(
                statusTitleLabel,
                statusTextArea,
                nameTitleLabel,
                nameLabel,
                friendListTitleLabel,
                friendListTextArea,
                networkInfoLabel,
                networkInfoTextArea
        );

        mainPane.setRight(rightBox);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method creates a pop-up window that displays alerts
     * @param message alert message
     * @param event alert event
     */
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

