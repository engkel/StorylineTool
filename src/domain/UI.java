package domain;

import domain.strategy.MultiUserSaveStrategy;
import domain.strategy.SaveStrategy;
import domain.strategy.SingleUserSaveStrategy;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.IOException;

public class UI {
    public static GridPane timelineGrid = new GridPane();

    static private TextArea noteTextArea;
    static private TextField orderField;
    static private TextField rowField;
    private static SaveStrategy saveStrategy;

    public static GridPane initStartMenu() {
        GridPane menuPane = new GridPane();

        // Set up a 2x2 grid
        // columns = 50% width
        // rows = 50% height
        ColumnConstraints columnWidth = new ColumnConstraints();
        columnWidth.setPercentWidth(50);
        RowConstraints rowHeight = new RowConstraints();
        rowHeight.setPercentHeight(50);
        menuPane.getColumnConstraints().addAll(columnWidth, columnWidth);
        menuPane.getRowConstraints().addAll(rowHeight, rowHeight);

        // Declare and instantiate all domain.UI elements
        Label title = new Label("Storyline Tool");
        Button singleUserBtn = new Button("Single User");
        Button multiUserBtn = new Button("Multi User");

        // Adjustments to the Title
        title.setFont(new Font("Arial", 65));
        // Node, columnIndex, rowIndex, colspan, rowspan
        menuPane.add(title, 0, 0, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.BOTTOM);
        // top, right, bottom, left
        GridPane.setMargin(title, new Insets(0, 0, 15, 0));

        // Adjustments to the "Single User" button
        singleUserBtn.setPrefSize(125,50);
        singleUserBtn.setFont(new Font("Arial", 16));
        menuPane.add(singleUserBtn, 0, 1);
        GridPane.setHalignment(singleUserBtn, HPos.RIGHT);
        GridPane.setValignment(singleUserBtn, VPos.TOP);
        // top, right, bottom, left
        GridPane.setMargin(singleUserBtn, new Insets(0, 10, 0, 0));

        // Adjustments to the "Multi User" button
        multiUserBtn.setPrefSize(125, 50);
        multiUserBtn.setFont(new Font("Arial", 16));
        menuPane.add(multiUserBtn, 1, 1);
        GridPane.setHalignment(multiUserBtn, HPos.LEFT);
        GridPane.setValignment(multiUserBtn, VPos.TOP);
        // top, right, bottom, left
        GridPane.setMargin(multiUserBtn, new Insets(0, 0, 0, 10));

        // Clicked event for the "Single User" button
        singleUserBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Single User Mode was clicked!");

                // Set the userMode to false (Single User Mode)
                Main.userMode = false;
                saveStrategy = new SingleUserSaveStrategy();

                try {
                    // Add all notes from the project.dat file into the notes LinkedList
                    Storage.loadFromFile();
                } catch (IOException ioException) {
                    System.out.println("An error occurred when trying to load data from the project.dat file.");
                    ioException.printStackTrace();
                }
                // Generate the program domain.UI (Without the notes)
                generateProgram();
                // Show all notes from the notes LinkedList in the Timeline
                updateTimeline();
            }
        });

        // Clicked event for the "Multi User" button
        multiUserBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                System.out.println("Multi User Mode was clicked!");

                Main.userMode = true;
                saveStrategy = new MultiUserSaveStrategy();

                // Adds all notes from the Database to the domain.Main.notes LinkedList
                Storage.loadFromDatabase(Main.projectID);
                generateProgram();
                // Show all notes from the notes LinkedList in the Timeline
                updateTimeline();
            }
        });

        return menuPane;
    }

    public static void generateProgram() {
        GridPane programPane = new GridPane();

        // Set the column width
        ColumnConstraints columnWidth = new ColumnConstraints();
        columnWidth.setPercentWidth(100);
        programPane.getColumnConstraints().add(columnWidth);

        // Set the row heights
        RowConstraints rowControlsHeight = new RowConstraints();
        RowConstraints rowToolsHeight = new RowConstraints();
        rowControlsHeight.prefHeightProperty().bind(programPane.heightProperty().subtract(175));
        rowToolsHeight.setPrefHeight(175);
        programPane.getRowConstraints().addAll(rowControlsHeight, rowToolsHeight);

        // Create the Timeline pane
        ScrollPane timelinePane = new ScrollPane();
        // Allows dragging the mouse to move around the ScrollPane
        timelinePane.setPannable(true);
        // Only shows the scroll bars when needed
        timelinePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        timelinePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        programPane.add(timelinePane, 0, 0);

        // Timeline Stack, All nodes are added to this Stackpane!
        timelineGrid = new GridPane();
        timelineGrid.setVgap(10);
        timelineGrid.setHgap(10);
        timelinePane.setContent(timelineGrid);

        // Create the Controls pane
        HBox controlsPane = new HBox();
        controlsPane.setBackground(new Background(new BackgroundFill(Color.rgb(220, 220, 220), CornerRadii.EMPTY, Insets.EMPTY)));
        programPane.add(controlsPane, 0, 1);

        // Add domain.Note TextArea
        VBox noteTextAreaBox = new VBox();
        noteTextAreaBox.setSpacing(5);
        Label noteTextAreaLabel = new Label("domain.Note Text:");
        noteTextAreaLabel.setFont(new Font("Arial", 14));
        noteTextArea = new TextArea();
        noteTextArea.setPrefWidth(225);
        noteTextArea.setFont(new Font("Arial", 14));
        // Add to noteTextAreaBox (Vbox, label and textarea)
        noteTextAreaBox.getChildren().addAll(noteTextAreaLabel, noteTextArea);



        // Prepare for domain.Note Order & Row
        VBox noteOtherPane = new VBox();
        // Row Gap of 10px
        noteOtherPane.setSpacing(10);

        // Add domain.Note Order
        VBox orderBox = new VBox();
        orderBox.setSpacing(5);
        Label orderLabel = new Label("Order:");
        orderLabel.setFont(new Font("Arial", 14));
        orderField = new TextField();
        orderField.setPrefHeight(30);
        orderField.setFont(new Font("Arial", 14));
        orderBox.getChildren().addAll(orderLabel, orderField);

        // Add domain.Note Row
        VBox rowBox = new VBox();
        rowBox.setSpacing(5);
        Label rowLabel = new Label("Row:");
        rowLabel.setFont(new Font("Arial", 14));
        rowField = new TextField();
        rowField.setPrefHeight(30);
        rowField.setFont(new Font("Arial", 14));
        rowBox.getChildren().addAll(rowLabel, rowField);



        // Add the "Add domain.Note" Button
        HBox addNoteBox = new HBox();
        Button addNoteBtn = new Button("Add domain.Note");
        addNoteBtn.setFont(new Font("Arial", 14));
        addNoteBtn.setPrefWidth(125);
        addNoteBtn.setPrefHeight(60);
        addNoteBox.getChildren().add(addNoteBtn);
        addNoteBox.setAlignment(Pos.BASELINE_CENTER);
        //addNoteBox.setBottom(addNoteBtn);

        // Add domain.Note Order and Row to the orderAndRowPane
        noteOtherPane.getChildren().addAll(orderBox, rowBox, addNoteBox);


        // Region testing
        Region hregion = new Region();


        // Add Delete Project and Save Project Buttons
        VBox saveOptionsBox = new VBox();
        saveOptionsBox.setAlignment(Pos.BOTTOM_CENTER);
        Button deleteProjectBtn = new Button("Delete Project");
        deleteProjectBtn.setFont(new Font("Arial", 14));
        deleteProjectBtn.setPrefHeight(24);
        deleteProjectBtn.setPrefWidth(130);
        deleteProjectBtn.setBackground(new Background(new BackgroundFill(Color.rgb(250, 110, 110), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(deleteProjectBtn, new Insets(10, 10, 5, 10));
        Button saveProjectBtn = new Button("Save to " + (Main.userMode ? "Database" : "File"));
        saveProjectBtn.setFont(new Font("Arial", 14));
        saveProjectBtn.setPrefHeight(70);
        saveProjectBtn.setPrefWidth(130);
        saveProjectBtn.setBackground(new Background(new BackgroundFill(Color.rgb(90, 200, 90), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(saveProjectBtn, new Insets(0, 10, 10, 10));
        saveOptionsBox.getChildren().addAll(deleteProjectBtn, saveProjectBtn);

        // Add the MOUSE_CLICKED event to the save button
        saveProjectBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                saveStrategy.save();
            }
        });


        // Add note tools (domain.Note Text, Order, Row and Add Button) and saving tools (Delete Project and Save Project) to the ControlPane
        controlsPane.getChildren().addAll(noteTextAreaBox, noteOtherPane, hregion, saveOptionsBox);
        // top, right, bottom, left
        HBox.setMargin(noteTextAreaBox, new Insets(10, 10, 10, 10));
        HBox.setMargin(noteOtherPane, new Insets(10, 0, 10, 0));
        HBox.setHgrow(hregion, Priority.ALWAYS);


        // Clicked event for the "Add domain.Note" button
        addNoteBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                //System.out.println("Add domain.Note Button was Clicked!");

                // Make sure the data in the inputs are valid, do nothing if invalid
                if (!Verifier.verifyNoteData(noteTextArea.getText(), orderField.getText(), rowField.getText())) {
                    System.out.println("The input data was not valid. An example can be inputting letters into the order or row input.");
                    return;
                }

                // Store the input data
                String noteText = noteTextArea.getText();
                int noteOrder = Integer.parseInt(orderField.getText());
                int noteRow = Integer.parseInt(rowField.getText());

                // Check if there are any duplicate notes (Same order and row)
                // Do nothing if there is a duplicate
                if (Note.checkDuplicate(noteOrder, noteRow)) {
                    System.out.println("The note was not added. There is already a note at the given order and row.");
                    return;
                }

                // Add the note to the timeline!
                Note.addNoteToLinkedList(noteText, noteOrder, noteRow);

                // Update the timeline to show the updated data.
                timelineGrid.getChildren().removeAll(timelineGrid.getChildren());
                UI.updateTimeline(); // Update the timeline to show the new note
            }
        });


        System.out.println("Changing scene now!");
        Main.menuScene = new Scene(programPane, 1300, 750);
        Main.primaryStage.setScene(Main.menuScene);
    }

    public static void updateTimeline() {

        for(int i = 0; i < Main.notes.size(); i++) {
            // Create the note element for JavaFX
            VBox noteBox = new VBox();
            noteBox.setMinSize(150, 75);
            noteBox.setPrefSize(150, 75);
            noteBox.setMaxSize(150, 75);
            Label noteText = new Label(Main.notes.get(i).text);
            Region spacer = new Region();
            Label noteInfo = new Label("Order: " + Main.notes.get(i).order + ", Row: " + Main.notes.get(i).row);

            // Add all the labels and the region
            noteBox.getChildren().addAll(noteText, spacer, noteInfo);
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Change the color of the notes to a yellow color
            noteBox.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 120), CornerRadii.EMPTY, Insets.EMPTY)));

            // Calculate the note's position in the grid
            int x = Main.notes.get(i).order / 2 - 1;
            int y = Main.notes.get(i).row - 1;

            System.out.println("domain.Note order: " + Main.notes.get(i).order);
            System.out.println("X: " + x);
            System.out.println("domain.Note row: " + Main.notes.get(i).row);
            System.out.println("Y: " + y);

            // Set the position of the note in the grid
            timelineGrid.add(noteBox, x, y);
        }
    }
}