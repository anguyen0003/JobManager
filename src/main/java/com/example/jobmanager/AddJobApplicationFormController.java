package com.example.jobmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddJobApplicationFormController {

    private final ObservableList<String> applicationStatusList = FXCollections.observableArrayList(
            "In Progress",
            "Under Consideration",
            "For Future Consideration",
            "Interviewing",
            "Offer",
            "Offer Accepted",
            "Offer Declined",
            "Rejected"
    );
    @FXML
    private TextField companyNameBox;
    @FXML
    private TextField jobTitleBox;
    @FXML
    private DatePicker appliedDateBox;
    @FXML
    private TextArea descriptionBox;
    @FXML
    private TextField jobLinkBox;
    @FXML
    private ChoiceBox<String> statusBox;
    @FXML
    private Button saveJobApplicationButton;
    @FXML
    private Button cancelJobApplicationButton;
    private Application createdApplication;

    @FXML
    public void initialize() {
        statusBox.getItems().addAll(applicationStatusList);
        statusBox.setValue("In Progress");
        saveJobApplicationButton.setOnAction(event -> handleSaveJobApplication());
        cancelJobApplicationButton.setOnAction(event -> handleCancelJobApplication());
    }

    private void handleSaveJobApplication() {
        String companyName = companyNameBox.getText();
        String jobTitle = jobTitleBox.getText();
        String appliedDate = (appliedDateBox.getValue() != null) ? appliedDateBox.getValue().toString() : "";
        String description = descriptionBox.getText();
        String jobLink = jobLinkBox.getText();
        String status = statusBox.getValue();

        createdApplication = new Application(companyName, jobTitle, appliedDate, jobLink, description, status);
        closeWindow();
    }

    private void handleCancelJobApplication() {
        createdApplication = null;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveJobApplicationButton.getScene().getWindow();
        stage.close();
    }

    public Application getApplicationDetails() {
        return createdApplication;
    }

    public void setApplicationEdit(Application application) {
        companyNameBox.setText(application.getCompanyName());
        jobTitleBox.setText(application.getJobTitle());

        if (application.getAppliedDate() != null && !application.getAppliedDate().isEmpty()) {
            appliedDateBox.setValue(java.time.LocalDate.parse(application.getAppliedDate()));
        }

        descriptionBox.setText(application.getDescription());
        jobLinkBox.setText(application.getJobLink());
        statusBox.setValue(application.getCurrentAppStatus());
    }
}