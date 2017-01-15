package com.uha.mo.view;

import com.uha.mo.App;
import com.uha.mo.model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ToolBar menuBar;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView settingsButton;
    @FXML
    private ImageView refreshButton;
    @FXML
    private HBox accounts;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox fenetre;

    private Stage stage;
    private App app;
    private double xOffset;
    private double yOffset;

    private double x;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.prefHeightProperty().bind(fenetre.heightProperty());
        scrollPane.setFitToHeight(true);

        menuBar.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        menuBar.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        this.menuBar.setPrefWidth(this.fenetre.getWidth());

        /********************************* EXIT BUTTON *********************************/

        exitButton.setOnMouseClicked(event -> System.exit(0));
        exitButton.setOnMouseEntered(event -> exitButton.setImage(new Image("images/delete_hover.png")));
        exitButton.setOnMouseExited(event -> exitButton.setImage(new Image("images/delete.png")));

        /********************************* SETTINGS BUTTON *********************************/

        settingsButton.setOnMouseClicked(event -> {
            try {
                app.setScene("settings");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        settingsButton.setOnMouseEntered(event -> settingsButton.setImage(new Image("images/settings_hover.png")));
        settingsButton.setOnMouseExited(event -> settingsButton.setImage(new Image("images/settings.png")));

        /********************************* REFRESH BUTTON *********************************/

        refreshButton.setOnMouseClicked(event -> {
            app.refreshModel();
            app.initRootLayout();
        });
        refreshButton.setOnMouseEntered(event -> refreshButton.setImage(new Image("images/refresh_hover.png")));
        refreshButton.setOnMouseExited(event -> refreshButton.setImage(new Image("images/refresh.png")));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public HBox getAccounts() {
        return this.accounts;
    }

    public VBox getRoot() {
        return this.fenetre;
    }

    public void setModel(Model model) {
        if(model.getAccounts().size() == 1)
            this.fenetre.setPrefWidth(640*model.getAccounts().size()+2);
        else
            this.fenetre.setPrefWidth(640*2+2);
    }
}

