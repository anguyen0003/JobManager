package com.example.jobmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.*;

public class QuestionsController {

    private final Deque<String> questions = new ArrayDeque<>();
    private String currentQuestion;

    @FXML
    private Label questionLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField addQuestionField;
    @FXML
    private Button deleteButton;
    @FXML
    public void initialize() {
        // Prepopulate the deque with demo questions
        Collections.addAll(questions,
                "Tell me when you solved a hard problem.",
                "Tell me a situation when you showed leadership.",
                "What do you do if there are tight deadlines?",
                "Give an example of how you work in a team.",
                "What do you do if there are conflicts within your team?"
        );

        // Converts deque to list to shuffle
        List<String> questionList = new ArrayList<>(questions);
        Collections.shuffle(questionList);

        // Clears deque & add shuffled questions back
        questions.clear();
        questions.addAll(questionList);

        currentQuestion = questions.peekFirst();
        updateQuestionLabel();

        backButton.setOnAction(event -> showPreviousQuestion());
        forwardButton.setOnAction(event -> showNextQuestion());
        addButton.setOnAction(event -> addQuestion());
        deleteButton.setOnAction(event -> deleteCurrentQuestion());
    }

    // Moves current question to end & gets last question
    private void showPreviousQuestion() {
        if (!questions.isEmpty() && currentQuestion != null) {
            questions.addFirst(currentQuestion); // Move current to start
            currentQuestion = questions.pollLast(); // Take last as current
            updateQuestionLabel();
        }
    }

    // Moves current question to start & gets first question
    private void showNextQuestion() {
        if (!questions.isEmpty() && currentQuestion != null) {
            questions.addLast(currentQuestion); // Move current to end
            currentQuestion = questions.pollFirst(); // Take first as current
            updateQuestionLabel();
        }
    }

    private void addQuestion() {
        String newQuestion = addQuestionField.getText();
        if (newQuestion != null && !newQuestion.trim().isEmpty()) {
            questions.addLast(newQuestion.trim());
            addQuestionField.clear();
            System.out.println("Question added: " + newQuestion);
        }
    }

    private void deleteCurrentQuestion() {
        if (currentQuestion != null) {
            System.out.println("Deleting question: " + currentQuestion);
            questions.remove(currentQuestion);
            currentQuestion = questions.peekFirst(); // Moves to next question
            updateQuestionLabel();
        }
    }

    private void updateQuestionLabel() {
        if (currentQuestion != null) {
            questionLabel.setText(currentQuestion);
        } else {
            questionLabel.setText("No questions available. Try adding one!");
        }
    }
}