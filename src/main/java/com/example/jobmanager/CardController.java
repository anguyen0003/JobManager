package com.example.jobmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CardController {

    @FXML
    private Label companyNameLabel;
    @FXML
    private Label jobTitleLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Button viewButton;
    @FXML
    private Button deleteButton;

    private Runnable onViewCallback;
    private Runnable onDeleteCallback;

    public void setData(Application application) {
        companyNameLabel.setText(application.getCompanyName());
        jobTitleLabel.setText(application.getJobTitle());
        statusLabel.setText("Status: " + application.getCurrentAppStatus());
        dateLabel.setText(application.getAppliedDate());

        setupButtonActions();
    }

    public void setOnViewCallback(Runnable onViewCallback) {
        this.onViewCallback = onViewCallback;
    }

    public void setOnDeleteCallback(Runnable onDeleteCallback) {
        this.onDeleteCallback = onDeleteCallback;
    }

    private void setupButtonActions() {
        viewButton.setOnAction(event -> {
            if (onViewCallback != null) {
                onViewCallback.run();
            }
        });

        deleteButton.setOnAction(event -> {
            if (onDeleteCallback != null) {
                onDeleteCallback.run();
            }
        });
    }
}
