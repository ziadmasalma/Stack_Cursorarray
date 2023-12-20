package com.example.proj2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main extends Application {
    // This code defines and sets up a GUI for a Java application using the JavaFX library.

    int count = 0; // This variable is not used in back button.

    // Create a CurserStack object to store strings.
    CurserStack<String> curserStack = new CurserStack<>();

    // Override the start method of the Application class.
    @Override
    public void start(Stage stage) {
        // Create a BackgroundFill object with a black color.
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, null, null);

        // Create a Background object and pass it the BackgroundFill object.
        Background background = new Background(backgroundFill);

        // Create a text field with the text "path" and set it to be non-editable.
        TextField textField = new TextField("path");
        textField.setEditable(false);
        textField.setPrefSize(200, 40);

        // Create a button with the label "Back" and set its font, minimum size, and text color.
        Button btback = new Button("Back");
        btback.setFont(new Font(30));
        btback.setMinSize(40, 5);
        btback.setTextFill(Color.BLACK);
        Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 20);
        btback.setFont(font);
        btback.setDisable(true); // Disable the button.

        // Create a button with the label "Load" and set its font, minimum size, and text color.
        Button btload = new Button("Load");
        btload.setFont(new Font(30));
        btload.setMinSize(40, 5);
        btload.setTextFill(Color.BLACK);
        btload.setFont(font);

        // Create a horizontal box layout and place the "Back" button, text field, and "Load" button inside it.
        HBox hBox = new HBox(btback, textField, btload);
        hBox.setAlignment(Pos.CENTER);

        // Create a label with the text "Equations" and set its font.
        Label lequations = new Label("Equations");
        lequations.setFont(new Font(20));

        // Create a text area with a black background color and a rectangular shape, and set it to be non-editable.
        TextArea tequations = new TextArea();
        tequations.setMaxWidth(400);
        tequations.setBackground(background);
        Rectangle rectangle = new Rectangle(200, 100);
        tequations.setShape(rectangle);
        tequations.setEditable(false);

        // Create a vertical box layout and place the "Equations" label and text area inside it.
        VBox vBox1 = new VBox(lequations, tequations);
        vBox1.setAlignment(Pos.CENTER);

        // Create a label with the text "File" and set its font.
        Label lfile = new Label("File");
        lfile.setFont(new Font(20));

        // Create a list view and set its maximum width and height, rectangular shape, and non-editable.
        ListView<String> tfile = new ListView<>();
        tfile.setMaxWidth(400);
        tfile.setMaxHeight(200);
        tfile.setShape(rectangle);
        tfile.setEditable(false);

// Create a vertical box layout and place the "File" label and list view inside it.
        VBox vBox2 = new VBox(lfile, tfile);

// Create a vertical box layout and place the horizontal box layout, vertical box layout 1, and vertical box layout 2 inside it.
        VBox vBox3 = new VBox(hBox, vBox1, vBox2);

// Set the alignment of vertical box layout 2 to be centered.
        vBox2.setAlignment(Pos.CENTER);


        btload.setOnAction(e -> {
            tfile.getItems().clear();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose file");
            File file = fileChooser.showOpenDialog(stage);
            String path = file.getAbsolutePath();
            if (validFile(path)) {
                count++;
                String equation = new String();
                // Read file line by line
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Check if line is an equation
                        if (line.trim().startsWith("<equation>")) {
                            // Extract equation from line and add to stack
                            String equations = line.substring(12, line.length() - 11);
                            String postfix = postfix(equations);

                            if (checkParameters(equations) == false) {
                                equation = equation + equations + "--> " + "UnBalanced" + "\n";
                            } else if (checkParameters(equations) == true) {
                                if (postfix == null) {
                                    equation = equation + equations + "--> " + "inValid" + "\n";
                                } else {
                                    try {
                                        equation = equation + equations + "--> " + postfix + " -->" + evaluatePostfixExpression(postfix) + "\n";
                                    } catch (Exception n) {
                                        equation = equation + equations + "--> " + "inValid" + "\n";
                                    }

                                }
                            }
                        }
                        // Check if line is a file
                        else if (line.trim().startsWith("<file>")) {
                            // Extract file path from line and add to stack
                            String files = line.substring(8, line.length() - 8);
                            tfile.getItems().addAll(files);
                        }
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                textField.setText(path);
                tequations.setText(equation);
                tfile.setEditable(false);
                curserStack.push(path);
            } else {
                textField.setText("invalid file ");
                curserStack.push(path);
                count++;

            }
            if (count > 1) {
                btback.setDisable(false);
            }
        });
        btback.setOnAction(e -> {
            tfile.getItems().clear();
            count--;
            curserStack.pop();
            String path = curserStack.peek();
            Boolean valid = validFile(path);
            if (valid) {
                if (count > 1) {
                    btback.setDisable(false);
                }
                String equation = new String();
                // Read file line by line
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Check if line is an equation
                        if (line.trim().startsWith("<equation>")) {
                            // Extract equation from line and add to stack
                            String equations = line.substring(12, line.length() - 11);
                            String postfix = postfix(equations);
                            if (checkParameters(equations) == false) {
                                equation = equation + equations + "--> " + "UnBalanced" + "\n";
                            } else if (checkParameters(equations) == true) {
                                if (postfix == null) {
                                    equation = equation + equations + "--> " + "inValid" + "\n";
                                } else {
                                    try {
                                        equation = equation + equations + "--> " + postfix + " -->" + evaluatePostfixExpression(postfix) + "\n";
                                    } catch (Exception n) {
                                        equation = equation + equations + "--> " + "inValid" + "\n";
                                    }

                                }
                            }
                        }
                        // Check if line is a file
                        else if (line.trim().startsWith("<file>")) {
                            // Extract file path from line and add to stack
                            String files = line.substring(8, line.length() - 8);
                            tfile.getItems().addAll(files);
                        }
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                textField.setText(path);
                tequations.setText(equation);

            } else {
                textField.setText("invalid file ");
                curserStack.push(path);
                count++;
            }

            if (count == 1)
                btback.setDisable(true);
        });
        tfile.setOnMouseClicked(event -> {
            String path = tfile.getSelectionModel().getSelectedItem().trim();
            Boolean valid = validFile(path);
            tfile.getItems().clear();
            if (valid) {
                count++;
                if (count > 1) {
                    btback.setDisable(false);
                }
                String equation = new String();
                // Read file line by line
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Check if line is an equation
                        if (line.trim().startsWith("<equation>")) {
                            // Extract equation from line and add to stack
                            String equations = line.substring(12, line.length() - 11);
                            String postfix = postfix(equations);

//                            if (postfix != "error") {
//                                String envir = equations + "-->" + postfix + "=" + evaluatePostfixExpression(postfix).toString();
//                                equation += envir + "\n";
//                            } else
//                                equation += postfix + "\n";
                            if (checkParameters(equations) == false) {
                                equation = equation + equations + "--> " + "UnBalanced" + "\n";
                            } else if (checkParameters(equations) == true) {
                                if (postfix == null) {
                                    equation = equation + equations + "--> " + "inValid" + "\n";
                                } else {
                                    try {
                                        equation = equation + equations + "--> " + postfix + " -->" + evaluatePostfixExpression(postfix) + "\n";
                                    } catch (Exception e) {
                                        equation = equation + equations + "--> " + "inValid" + "\n";
                                    }

                                }
                            }
                        }
                        // Check if line is a file
                        else if (line.trim().startsWith("<file>")) {
                            // Extract file path from line and add to stack
                            String files = line.substring(8, line.length() - 8);
                            tfile.getItems().addAll(files);
                        }
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                textField.setText(path);
                tequations.setText(equation);
                curserStack.push(path);
            } else {
                textField.setText("invalid file ");
                curserStack.push(path);
                count++;
            }
            if (count == 1)
                btback.setDisable(true);

        });
        //sets the scene for the stage to be a VBox layout with a specified size and background color. Finally, it displays the stage to the user.
        BackgroundFill backgroundFill1 = new BackgroundFill(Color.ORANGE, null, null);
        Background background1 = new Background(backgroundFill1);
        vBox3.setBackground(background1);
        Scene scene = new Scene(vBox3, 600, 500);
        //This code creates a new stage and sets its title to "project 2"
        Stage stage1 = new Stage();
        stage1.setTitle("project 2");
        //sets an icon for the stage
        Image icon = new Image("https://www.shareicon.net/data/512x512/2016/07/10/119930_google_512x512.png");
        stage1.getIcons().add(icon);
        stage1.setScene(scene);
        stage1.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static String postfix(String x) {

        CurserStack<String> a = new CurserStack<>();
        String y[] = x.split(" ");
        String result = "";
        for (int i = 0; i < y.length; i++) {
            if (y[i].equals("(")) { // If current token is an opening parenthesis, push it to stack
                a.push(y[i]);
            } else if (y[i].equals(")")) { // If current token is a closing parenthesis, pop and append all operators from the stack until an opening parenthesis is encountered
                while (!a.peek().equals("(")) {
                    result += " " + a.peek();
                    a.pop();
                }
                a.pop();
            } else if (y[i].equals("+") || y[i].equals("-")) { // If current token is an operator, pop and append all operators from the stack until an opening parenthesis or a lower precedence operator is encountered
                while (!a.isEmpty() && !a.peek().equals("(")) {
                    result += " " + a.peek();
                    a.pop();
                }
                a.push(y[i]);
            } else if (y[i].equals("*") || y[i].equals("/") || y[i].equals("%")) { // If current token is an operator, pop and append all operators from the stack until an opening parenthesis or a lower or equal precedence operator is encountered
                if (a.isEmpty() || a.peek().contains("+") || a.peek().contains("-") || a.peek().contains("(")) {
                    a.push(y[i]);

                } else {

                    result += " " + a.peek();
                    a.pop();
                    a.push(y[i]);
                }
            } else { // If current token is an operand, append it to result
                result += " " + y[i];
            }

        }
        // Append all remaining operators from the stack to result
        while (a.peek() != null) {
            result += " " + a.peek();
            a.pop();
        }
        return result;

    }

    public static Double evaluatePostfixExpression(String postfixExpr) {
            // Create a stack to hold the operands
            CurserStack<Double> stack = new CurserStack<Double>();
            // Split the postfix expression into individual items (operators and operands)
            String[] items = postfixExpr.split(" ");

            for (String item : items) {
                try {
                    // If the item is a number, push it onto the stack
                    stack.push(Double.valueOf(item));
                } catch (NumberFormatException e) {
                    // If the item is not a number, it must be an operator
                    double val1 = 0;
                    double val2 = 0;
                    double res = 0;

                    switch (item) {
                        case "+":
                            // Pop the top two operands from the stack, add them, and push the result back onto the stack
                            val1 = stack.pop();
                            val2 = stack.pop();
                            res = val1 + val2;
                            stack.push(res);
                            break;
                        case "-":
                            // Pop the top two operands from the stack, subtract the top operand from the second operand, and push the result back onto the stack
                            val1 = stack.pop();
                            val2 = stack.pop();
                            res = val2 - val1;
                            stack.push(res);
                            break;
                        case "/":
                            // Pop the top two operands from the stack, divide the second operand by the top operand, and push the result back onto the stack
                            try {
                                val1 = stack.pop();
                                val2 = stack.pop();
                                res = val2 / val1;
                                stack.push(res);
                                break;
                            } catch (Exception ex) {
                                // Handle division by zero
                                System.out.println("division by zero exception");
                            }
                        case "*":
                            // Pop the top two operands from the stack, multiply them, and push the result back onto the stack
                            val1 = stack.pop();
                            val2 = stack.pop();
                            res = val2 * val1;
                            stack.push(res);
                            break;

                        case "%":
                            // Pop the top two operands from the stack, find the remainder when the second operand is divided by the top operand, and push the result back onto the stack
                            val1 = stack.pop();
                            val2 = stack.pop();
                            res = val2 % val1;
                            stack.push(res);
                            break;
                    }
                }
            }

            // Return the final result, which should be the only remaining value on the stack
            return stack.pop();
    }

    public static boolean validFile(String path) {
        // Create a stack to store the open tags
        CurserStack<String> openTags = new CurserStack<>();

        // Open the file for reading
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            // Read the file line by line
            String line = reader.readLine();
            while (line != null) {
                // Check if the line contains a tag
                if (line.startsWith("<") && line.endsWith(">")) {
                    // Extract the tag from the line
                    String tag = line.substring(1, line.length() - 1).trim();
                    // Check if the tag is a start tag or an end tag
                    if (tag.startsWith("/")) {
                        // The tag is an end tag, so remove the corresponding start tag from the stack
                        String startTag = tag.substring(1);
                        if (openTags.isEmpty()) {
                            // There are no open tags, so the file is invalid
                            return false;
                        } else if (!openTags.peek().equals(startTag)) {
                            // The start tag does not match the end tag, so the file is invalid
                            return false;
                        } else {
                            // The start tag matches the end tag, so remove the start tag from the stack
                            openTags.pop();
                        }
                    } else {
                        // The tag is a start tag, so add it to the stack
                        openTags.push(tag);
                    }
                }
                line = reader.readLine();
            }

            // Check if the stack is empty
            if (openTags.isEmpty()) {
                // The stack is empty, so all start tags have a corresponding end tag
                return true;
            } else {
                // The stack is not empty, so there are unclosed start tags
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkParameters(String exp) {
        CurserStack<Character> s = new CurserStack<>();
        char temp;
        char[] c = exp.toCharArray();

// loop through each character in the expression
        for (int i = 0; i < c.length; i++) {

// if the character is an opening parenthesis, push it onto the stack
            switch (c[i]) {
                case '(':
                    s.push(c[i]);
                    break;

// if the character is a closing parenthesis, check if the stack is empty
// if it is not empty, pop an element from the stack and check if it is an opening parenthesis
// if it is not, return false
// if the stack is empty, return false
                case ')':
                    if (!s.isEmpty()) {
                        temp = (char) s.pop();
                        if (temp != '(') {
                            return false;
                        }
                    } else {
                        return false;
                    }

                    break;
            }

        }

// after the loop is complete, check if the stack is empty. If it is not empty, return false
// otherwise, return true
        if (s.peek() != null) {
            return false;
        } else
            return true;


    }

}
