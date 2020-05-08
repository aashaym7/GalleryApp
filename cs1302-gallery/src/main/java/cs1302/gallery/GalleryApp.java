package cs1302.gallery;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Represents an iTunes GalleryApp!.
 */
public class GalleryApp extends Application {
    VBox vBox = new VBox();
    TextField textField;
    int counter;
    String userInput;
    int numResults;
    String[] urls;
    Image image;
    ImageView imageView;
    GridPane gridPane = new GridPane();
    MenuBar menuBar;
    ToolBar toolBar;
    Button playPause;
    Button updateImage;
    Text searchQuery;
    KeyFrame keyFrame;
    Timeline timeline;
    int randomCol;
    int randomRow;
    ProgressBar progressBar;
    Double progress = 0.0;
    Scene scene;

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws Exception {
        vBox.setPrefSize(500, 500);
        vBox.getChildren().addAll(menu(), createToolBar());
        defaultImages("jack johnson");
        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(progressBar());
        scene = new Scene(vBox);
        stage.setResizable(false);
        stage.setTitle("GalleryApp!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    } // start

    /**
     * This method creates a MenuBar and it's components.
     *
     * @return returns the contents of the MenuBar
     */
    public MenuBar menu() {
        menuBar = new MenuBar();
        // Creates a Menu option for a file and adds it to Menu Bar
        Menu file = new Menu("File");
        // An Exit option for File.
        MenuItem exit = new MenuItem("Exit");
        // Adds the exit to the file.
        file.getItems().add(exit);
        exit.setOnAction(event -> System.exit(0));

        //Adds the help menu where the info of a developer is shown.
        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().add(about);
        about.setOnAction(event -> info());

        Menu theme = new Menu("Themes");
        MenuItem theme1 = new MenuItem("Theme 1");
        MenuItem theme2 = new MenuItem("Theme 2");
        MenuItem defualtTheme = new MenuItem("Defualt theme");
        theme.getItems().addAll(theme1, theme2, defualtTheme);
        themes(theme1, theme2, defualtTheme);

        menuBar.getMenus().addAll(file, theme, help);
        return menuBar;
    } // Menu

    /**
     * This method creates a new pop up window for "About" menuItem.
     */
    public void info() {
        Runnable r = () -> {
            Platform.runLater(() -> {
                Stage window = new Stage();

                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("About Aashay Mehta");

                Text info = new Text();
                info.setText("Name: Aashay Mehta \n" +
                        "Email: anm75263@uga.edu \n" +
                        "Version: Gallery 1.8");

                Image myPic = new Image("https://i.imgur.com/rzbGnj3.jpg");
                ImageView myView = new ImageView(myPic);
                myView.setFitWidth(400);
                myView.setFitHeight(480);
                VBox myDetail = new VBox(10);
                myDetail.getChildren().addAll(info, myView);

                Scene infoGraph = new Scene(myDetail);
                window.setScene(infoGraph);
                window.setResizable(false);
                window.showAndWait();
            });
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } // info

    /**
     * This method designs a themes for "Theme" menuItem.
     *
     * @param theme1 the #1 theme
     * @param theme2 the #2 theme
     * @param defaultTheme the defualt theme
     */
    public void themes (MenuItem theme1, MenuItem theme2, MenuItem defaultTheme) {
        theme1.setOnAction(event -> {
            scene.getStylesheets().clear();
            menuBar.getStylesheets().clear();
            toolBar.getStylesheets().clear();
            vBox.getStylesheets().clear();
            playPause.getStylesheets().clear();
            updateImage.getStylesheets().clear();
            menuBar.setStyle("-fx-background-color: BEIGE;");
            toolBar.setStyle("-fx-background-color: #eb5454;");
            playPause.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            updateImage.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
            vBox.setStyle("-fx-background-color: BEIGE;");
        });

        theme2.setOnAction(e -> {
            scene.getStylesheets().clear();
            menuBar.getStylesheets().clear();
            toolBar.getStylesheets().clear();
            vBox.getStylesheets().clear();
            playPause.getStylesheets().clear();
            updateImage.getStylesheets().clear();
            menuBar.setStyle("-fx-background-color: #46e3d3;");
            toolBar.setStyle("-fx-background-color: #b899f2;");
            playPause.setStyle("-fx-background-color: #E71989; -fx-text-fill: #213970;");
            updateImage.setStyle("-fx-background-color: #E71989; -fx-text-fill: #213970;");
            vBox.setStyle("-fx-background-color: #46e3d3;");
        });

        defaultTheme.setOnAction(event -> {
            scene.getStylesheets().clear();
            menuBar.getStylesheets().clear();
            toolBar.getStylesheets().clear();
            vBox.getStylesheets().clear();
            playPause.getStylesheets().clear();
            updateImage.getStylesheets().clear();
            menuBar.setStyle(null);
            toolBar.setStyle(null);
            playPause.setStyle(null);
            updateImage.setStyle(null);
            vBox.setStyle(null);
        });

    } // themes


    /**
     * This method sets the timeline for images to be randomly placed.
     *
     * @param handler the handler for the random image event to occur
     */
    public void setTimeLine(EventHandler<ActionEvent> handler) {
        keyFrame = new KeyFrame(Duration.seconds(2), handler);
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }  // setTimeLine

    /**
     * This method creates a ToolBar and it's components.
     *
     * @return returns the ToolBar components.
     */
    public ToolBar createToolBar() {
        toolBar = new ToolBar();
        playPause = new Button("Pause");
        EventHandler<ActionEvent> handler = event -> {
            Platform.runLater(this::randomImages);
        };
        setTimeLine(handler);
        playPause.setOnAction(event -> {
            Platform.runLater(() -> {
                if (playPause.getText().equals("Pause")) {
                    timeline.pause();
                    playPause.setText("Play");
                } else {
                    timeline.play();
                    playPause.setText("Pause");
                } // if
            });
        });
        searchQuery = new Text("Search Query: ");
        textField = new TextField();
        updateImage = new Button("Update Images");
        updateImage.setOnAction(event -> {
            inputImages(textField);
        });
        toolBar.getItems().add(playPause);
        toolBar.getItems().addAll(new Separator(), searchQuery, textField, updateImage);
        return toolBar;
    } // createToolBar

    /**
     * This is a helper method for randomly displaying images.
     */
    public void randomImages() {
        if (numResults >= 21) {
            Random rand = new Random();
            randomCol = rand.nextInt(5);
            randomRow = rand.nextInt(4);
            imageView = new ImageView(new Image(urls[rand.nextInt(numResults - 1)]));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            gridPane.add(imageView, randomCol, randomRow);
        } // if
    } // randomImages

    /**
     * This method helps in taking a String input and parsing it through Json
     * and according to given conditions saving those parsed links to array.
     *
     * @param input the input that has to be parsed in Json
     */
    public void parser(String input) {
        String userQuery = "https://itunes.apple.com/search?term=" +
                input.replaceAll(" ", "+") + "&limit=50&media=music";
        URL url = null;
        try {
            url = new URL(userQuery);
        } catch (MalformedURLException e) {
            System.err.print(e.getMessage());
        } //try
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            System.err.print(e.getMessage());
        } // try
        JsonElement je = JsonParser.parseReader(reader);
        // root of response
        JsonObject root = je.getAsJsonObject();
        // "results" array
        JsonArray results = root.getAsJsonArray("results");

        // "results" array size
        numResults = results.size();
        urls = new String[numResults];
        for (int i = 0; i < numResults; i++) {
            JsonObject result = results.get(i).getAsJsonObject();
            JsonElement artworkUrl100 = result.get("artworkUrl100");
            if (artworkUrl100 != null) {
                urls[i] = artworkUrl100.getAsString();
                image = new Image(urls[i]);
                imageView = new ImageView(image);
                gridPane.getChildren().add(imageView);
            } // if
        } // for
    } // parser

    /**
     * This is a defualt method where images are loaded as soon as
     * the app starts.
     *
     * @param input the defualt input
     */
    public void defaultImages(String input) {
        textField.setText("jack johnson");
        counter = 0;
        parser(input);
        Platform.runLater(() -> progressBar.setProgress(0.0));
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 4; k++) {
                image = new Image(urls[counter]);
                imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                gridPane.add(imageView, j, k);
                incrementProgress();
                counter++;
            } // for
        } // for
    } // addImages

    /**
     * This is a warning method which pops up if the results of
     * the search query's input has less than 21 images.
     */
    public void alert() {
        Runnable r = () -> {
            Platform.runLater(() -> {
                Stage window = new Stage();

                window.initModality(Modality.APPLICATION_MODAL);
                window.setTitle("Error!");
                window.setWidth(380);
                window.setHeight(80);

                Label label = new Label();
                label.setText("There are not enough results, please try again!");

                Button errorButton = new Button("Try Again!");
                errorButton.setAlignment(Pos.BOTTOM_CENTER);
                errorButton.setOnAction(event -> window.close());

                VBox error = new VBox(10);
                error.setAlignment(Pos.BOTTOM_CENTER);
                error.getChildren().addAll(label, errorButton);

                Scene alert = new Scene(error);
                window.setScene(alert);
                window.setResizable(false);
                window.showAndWait();
            });
        };
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    } // alert

    /**
     * This is a method for loading images according to the
     * user's input.
     *
     * @param textField the textfield where the user inputs
     */
    public void inputImages(TextField textField) {
        Platform.runLater(() -> progressBar.setProgress(0.0));
        String userInput = textField.getText();
        parser(userInput);
        counter = 0;

        if (numResults < 21) {
            alert();
        } else {
            gridPane.getChildren().clear();
            try {
                Platform.runLater(() -> progressBar.setProgress(0));
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < 4; k++) {
                        image = new Image(urls[counter]);
                        imageView = new ImageView(image);
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
                        gridPane.add(imageView, j, k);
                        incrementProgress();
                        counter++;
                    } // for
                } // for
            } catch (Exception e) {
                System.err.print(e.getMessage());
            } // try
        } // if
    } // inputImages

    /**
     * This is a method for adding ProgressBar and it's components.
     *
     * @return the ProgressBar's components.
     */
    public HBox progressBar() {
        HBox hBox = new HBox();
        Text text = new Text(" Images provided courtesy of iTunes");
        progressBar = new ProgressBar();
        hBox.getChildren().addAll(progressBar, text);
        return hBox;
    } // progressBar

    /**
     * This is a helper method for the progressBar.
     */
    public void incrementProgress() {
        Platform.runLater(() -> {
            progressBar.setProgress(progressBar.getProgress() + 0.05);
        });
    } // incrementProgress

    /**
     * This is a  method for threads.
     *
     * @param target the target to be runned along with other task
     */
    public void runNow(Runnable target) {
        Thread t = new Thread(target);
        t.setDaemon(true);
        t.start();
    } // runNow

} // GalleryApp
