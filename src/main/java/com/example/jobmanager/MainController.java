package com.example.jobmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class MainController {

    @FXML
    private Button addJobButton, sortButton;
    @FXML
    private StackPane contentPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private ChoiceBox<String> sortChoiceBox;
    @FXML
    private Label questionsLabel, dashboardLabel, graphLabel;
    @FXML
    private Pane graphPane;

    private final ObservableList<Application> applications = FXCollections.observableArrayList();
    private final HashSet<String> jobApplicationSet = new HashSet<>();
    private final ApplicationTree applicationTree = new ApplicationTree();

    private int currentRow = 0;
    private int currentCol = 0;
    private static final int NUM_COLUMNS = 3;

    @FXML
    public void initialize() {
        setupNavigationBar();
        setupGrid();
        setupSortingOptions();
    }

    private void setupNavigationBar() {
        graphLabel.setOnMouseClicked(event -> switchToGraphView());
        questionsLabel.setOnMouseClicked(event -> switchToQuestionsView());
        dashboardLabel.setOnMouseClicked(event -> switchToDashboardView());
        addJobButton.setOnAction(event -> handleAddJobButtonClick());
    }

    private void setupGrid() {
        mainGridPane.setMinHeight(GridPane.USE_PREF_SIZE);
        mainGridPane.setPrefHeight(GridPane.USE_COMPUTED_SIZE);
        mainGridPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        mainGridPane.getColumnConstraints().clear();
        for (int i = 0; i < NUM_COLUMNS; i++) {
            ColumnConstraints cc = new ColumnConstraints(225, 225, 225);
            cc.setHalignment(HPos.CENTER);
            mainGridPane.getColumnConstraints().add(cc);
        }
    }

    private void setupSortingOptions() {
        sortChoiceBox.getItems().addAll("Sort by Applied Date", "Sort by Job Title");
        sortChoiceBox.setValue("Sort by Applied Date");
        sortButton.setOnAction(event -> handleSortButtonClick());
    }

    private void switchToGraphView() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(graphPane);
        graphPane.setVisible(true);
        renderGraph();
    }

    private void renderGraph() {
        graphPane.getChildren().clear();

        double[][] positions = {
                {100, 100}, {300, 100}, {500, 100}, {300, 300},
                {100, 500}, {500, 500}, {700, 100}
        };
        String[] statuses = {
                "In Progress", "Under Consideration", "Interviewing",
                "Offer", "Offer Accepted", "Offer Declined", "Rejected"
        };

        // Get statuses from current apps
        HashSet<String> activeStatuses = new HashSet<>();
        applications.forEach(app -> activeStatuses.add(app.getCurrentAppStatus()));

        Circle[] nodes = new Circle[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            if (activeStatuses.contains(statuses[i])) {
                Circle node = new Circle(positions[i][0], positions[i][1], 30);
                node.setStyle("-fx-fill: lightblue; -fx-stroke: black; -fx-stroke-width: 2;");

                Label label = new Label(statuses[i]);
                label.setLayoutX(positions[i][0] - 40);
                label.setLayoutY(positions[i][1] - 50);

                graphPane.getChildren().addAll(node, label);
                nodes[i] = node;
            }
        }

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 4}, {3, 5}, {1, 6}, {2, 6}
        };
        for (int[] edge : edges) {
            if (nodes[edge[0]] != null && nodes[edge[1]] != null) {
                Line line = new Line(
                        nodes[edge[0]].getCenterX(), nodes[edge[0]].getCenterY(),
                        nodes[edge[1]].getCenterX(), nodes[edge[1]].getCenterY()
                );
                line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
                graphPane.getChildren().add(line);
            }
        }
    }

    private void handleSortButtonClick() {
        applicationTree.clear();
        jobApplicationSet.clear();

        boolean sortByDate = "Sort by Applied Date".equals(sortChoiceBox.getValue());
        applications.forEach(application -> {
            String key = application.getCompanyName() + "-" + application.getJobTitle();
            if (jobApplicationSet.add(key)) {
                applicationTree.insert(application, sortByDate);
            }
        });

        refreshGrid(applicationTree.inOrderTraversal());
    }

    private void refreshGrid(List<Application> sortedApplications) {
        mainGridPane.getChildren().clear();
        currentRow = 0;
        currentCol = 0;

        sortedApplications.forEach(this::addCard);
    }

    public void addCard(Application application) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
            Parent card = loader.load();
            CardController cardController = loader.getController();
            cardController.setData(application);

            // Actions for view and delete buttons
            cardController.setOnViewCallback(() -> showApplicationDetails(application));
            cardController.setOnDeleteCallback(() -> {
                applications.remove(application);
                jobApplicationSet.remove(application.getCompanyName() + "-" + application.getJobTitle()); // Remove from tracking
                refreshGrid(applications);
            });

            // Add card to grid
            mainGridPane.add(card, currentCol++, currentRow);

            // Moves to next row if current row full
            if (currentCol == NUM_COLUMNS) {
                currentCol = 0;
                currentRow++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showApplicationDetails(Application application) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddJobApplicationForm.fxml"));
            Parent root = loader.load();
            AddJobApplicationFormController controller = loader.getController();
            controller.setApplicationEdit(application);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Job Application");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            Application updatedApplication = controller.getApplicationDetails();
            if (updatedApplication != null) {
                int index = applications.indexOf(application);
                if (index != -1) {
                    applications.set(index, updatedApplication);
                }
                refreshGrid(applications);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddJobButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddJobApplicationForm.fxml"));
            Parent root = loader.load();
            AddJobApplicationFormController controller = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Main part
            stage.setTitle("Add Job Application");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            Application newApplication = controller.getApplicationDetails();
            if (newApplication != null) {
                String key = newApplication.getCompanyName() + "-" + newApplication.getJobTitle();
                if (jobApplicationSet.add(key)) {
                    applications.add(newApplication);
                    addCard(newApplication);
                    renderGraph();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToQuestionsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionsPage.fxml"));
            Parent questionsView = loader.load();
            sortChoiceBox.setVisible(false);
            sortButton.setVisible(false);
            addJobButton.setVisible(false);
            contentPane.getChildren().clear();
            contentPane.getChildren().add(questionsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToDashboardView() {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(scrollPane);
        sortChoiceBox.setVisible(true);
        sortButton.setVisible(true);
        addJobButton.setVisible(true);
    }
}
