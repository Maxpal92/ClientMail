package com.uha.mo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by maxime on 17/11/2016.
 */
public class sendMailController implements Initializable {
    @FXML
    private Label sendToLabel;
    @FXML
    private TextField sendToTextField;
    @FXML
    private TextArea mailContentTextArea;
    @FXML
    private Button sendButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
