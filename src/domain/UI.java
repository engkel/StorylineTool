package domain;

import domain.dao.NoteDao;
import domain.strategy.delete.DeleteStrategy;
import domain.strategy.delete.MultiUserDeleteStrategy;
import domain.strategy.delete.SingleUserDeleteStrategy;
import domain.strategy.export.ExportProjectStrategy;
import domain.strategy.export.ExportStrategy;
import domain.strategy.save.MultiUserSaveStrategy;
import domain.strategy.save.SaveStrategy;
import domain.strategy.save.SingleUserSaveStrategy;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.IOException;

public class UI {
    // Declare out timeline GridPane, so it can be accessed throughout the class.
    private static GridPane timelineGrid = new GridPane();

    // Declare our noteTextArea, orderField and rowField so they
    // can be accessed throughout the whole scope of the UI class.
    static private TextArea noteTextArea;
    static private TextField orderField;
    static private TextField rowField;

    // Declare our SaveStrategy and DeleteStrategy so it can be accessed throughout the class
    private static SaveStrategy saveStrategy;
    private static DeleteStrategy deleteStrategy;
    private static ExportStrategy exportStrategy = new ExportProjectStrategy();

    /**
     *  This method will generate a GridPane for the start menu.
     *  This includes the title and the two buttons. As well as
     *  adding MOUSE_CLICKED events to the buttons.
     * @return - Gridpane, returns the gridpane for the start menu.
     */
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

        // Declare and instantiate all UI elements
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

                // Update the userMode to store that we are using Single User Mode.
                // Set the userMode to false (Single User Mode)
                Main.userMode = false;
                // Set SingleUser as our Save Strategy
                saveStrategy = new SingleUserSaveStrategy();
                // Set SingleUser as our Delete Strategy
                deleteStrategy = new SingleUserDeleteStrategy();


                try {
                    // Add all notes from the project.dat file into the notes LinkedList
                    Storage.loadFromFile();
                } catch (IOException ioException) {
                    System.out.println("An error occurred when trying to load data from the project.dat file.");
                    ioException.printStackTrace();
                }
                // Generate the program UI (Without the notes)
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

                // Update the userMode to store that we are using Multi User Mode.
                // Set the userMode to true (Multi User Mode)
                Main.userMode = true;
                // Set MultiUser as our Save Strategy
                saveStrategy = new MultiUserSaveStrategy();
                // Set SingleUser as our Delete Strategy
                deleteStrategy = new MultiUserDeleteStrategy();

                // Get the dao used in the strategy. We first cast the saveStrategy as a MultiUserSaveStrategy,
                // since we instantiated it as such, then call the getNoteDao method.
                NoteDao noteDao = ((MultiUserSaveStrategy) saveStrategy).getNoteDao();
                // Use this dao to load notes from database into the Main.notes LinkedList
                noteDao.loadNotes(Main.projectID);

                generateProgram();
                // Show all notes from the notes LinkedList in the Timeline
                updateTimeline();
            }
        });

        // Return the generated gridpane for the menu
        return menuPane;
    }

    /**
     *  This method will generate the whole UI for the main program.
     *  It will not add any notes to the timeline, but it will
     *  make the program ready. Notes can be added with updateTimeline
     *  once this method has been run.
     */
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

        // Add Note TextArea
        VBox noteTextAreaBox = new VBox();
        noteTextAreaBox.setSpacing(5);
        Label noteTextAreaLabel = new Label("Note Text:");
        noteTextAreaLabel.setFont(new Font("Arial", 14));
        noteTextArea = new TextArea();
        noteTextArea.setPrefWidth(225);
        noteTextArea.setFont(new Font("Arial", 14));
        // Add to noteTextAreaBox (Vbox, label and textarea)
        noteTextAreaBox.getChildren().addAll(noteTextAreaLabel, noteTextArea);



        // Prepare for Note Order & Row
        VBox noteOtherPane = new VBox();
        // Row Gap of 10px
        noteOtherPane.setSpacing(10);

        // Add Note Order
        VBox orderBox = new VBox();
        orderBox.setSpacing(5);
        Label orderLabel = new Label("Order:");
        orderLabel.setFont(new Font("Arial", 14));
        orderField = new TextField();
        orderField.setPrefHeight(30);
        orderField.setFont(new Font("Arial", 14));
        orderBox.getChildren().addAll(orderLabel, orderField);

        // Add Note Row
        VBox rowBox = new VBox();
        rowBox.setSpacing(5);
        Label rowLabel = new Label("Row:");
        rowLabel.setFont(new Font("Arial", 14));
        rowField = new TextField();
        rowField.setPrefHeight(30);
        rowField.setFont(new Font("Arial", 14));
        rowBox.getChildren().addAll(rowLabel, rowField);



        // Add the "Add Note" Button
        HBox addNoteBox = new HBox();
        Button addNoteBtn = new Button("Add Note");
        addNoteBtn.setFont(new Font("Arial", 14));
        addNoteBtn.setPrefWidth(125);
        addNoteBtn.setPrefHeight(60);
        addNoteBox.getChildren().add(addNoteBtn);
        addNoteBox.setAlignment(Pos.BASELINE_CENTER);

        // Add Note Order and Row to the orderAndRowPane
        noteOtherPane.getChildren().addAll(orderBox, rowBox, addNoteBox);


        // Region testing
        Region hregion = new Region();

        // Declare and initialize the VBox used to contain the below three buttons.
        VBox saveOptionsBox = new VBox();
        saveOptionsBox.setAlignment(Pos.BOTTOM_CENTER);

        // Add the Export button
        Button exportProjectBtn = new Button("Export Project");
        exportProjectBtn.setFont(new Font("Arial", 14));
        exportProjectBtn.setPrefHeight(24);
        exportProjectBtn.setPrefWidth(130);
        exportProjectBtn.setBackground(new Background(new BackgroundFill(Color.rgb(250, 140, 60), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(exportProjectBtn, new Insets(10, 10, 5, 10));

        // Add Delete Project button
        Button deleteProjectBtn = new Button("Delete Project");
        deleteProjectBtn.setFont(new Font("Arial", 14));
        deleteProjectBtn.setPrefHeight(24);
        deleteProjectBtn.setPrefWidth(130);
        deleteProjectBtn.setBackground(new Background(new BackgroundFill(Color.rgb(250, 110, 110), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(deleteProjectBtn, new Insets(10, 10, 5, 10));

        //  Add the Save Project Button
        Button saveProjectBtn = new Button("Save to " + (Main.userMode ? "Database" : "File"));
        saveProjectBtn.setFont(new Font("Arial", 14));
        saveProjectBtn.setPrefHeight(70);
        saveProjectBtn.setPrefWidth(130);
        saveProjectBtn.setBackground(new Background(new BackgroundFill(Color.rgb(90, 200, 90), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(saveProjectBtn, new Insets(0, 10, 10, 10));

        // Add the three buttons to the VBox
        saveOptionsBox.getChildren().addAll(exportProjectBtn, deleteProjectBtn, saveProjectBtn);

        // Add the MOUSE_CLICKED event to the export project button
        exportProjectBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // Remove all the data in the project file
                exportStrategy.export();
            }
        });

        // Add the MOUSE_CLICKED event to the delete project button
        deleteProjectBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // Remove all the data in the project file
                deleteStrategy.delete();

                // Update the UI, which in this case
                // makes all the notes disappear.
                updateTimeline();
            }
        });

        // Add the MOUSE_CLICKED event to the save button
        saveProjectBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                saveStrategy.save();
            }
        });


        // Add note tools (Note Text, Order, Row and Add Button) and saving tools (Delete Project and Save Project) to the ControlPane
        controlsPane.getChildren().addAll(noteTextAreaBox, noteOtherPane, hregion, saveOptionsBox);
        // top, right, bottom, left
        HBox.setMargin(noteTextAreaBox, new Insets(10, 10, 10, 10));
        HBox.setMargin(noteOtherPane, new Insets(10, 0, 10, 0));
        HBox.setHgrow(hregion, Priority.ALWAYS);


        // Clicked event for the "Add Note" button
        addNoteBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                // Store the input fields in their own Strings
                String noteText = noteTextArea.getText();
                String noteOrder = orderField.getText();
                String noteRow = rowField.getText();

                // Make sure the data in the inputs are valid, do nothing if invalid
                if (!Verifier.verifyNoteData(noteText, noteOrder, noteRow)) {
                    System.out.println("The input data was not valid. " +
                            "An example can be inputting letters into the order or row input.");
                    return;
                }

                // Store the input data
                int noteOrderParsed = Integer.parseInt(noteOrder);
                int noteRowParsed = Integer.parseInt(noteRow);

                // Check if there are any duplicate notes (Same order and row)
                // Do nothing if there is a duplicate
                if (Note.checkDuplicate(noteOrderParsed, noteRowParsed)) {
                    System.out.println("The note was not added. " +
                            "There is already a note at the given order and row.");
                    return;
                }

                // Add the note to the timeline!
                Note.addNoteToLinkedList(noteText, noteOrderParsed, noteRowParsed);

                // Update the timeline to show the updated data.
                updateTimeline(); // Update the timeline to show the new note
            }
        });

        // Change the scene to show the recently made programPane
        System.out.println("Changing scene now!");
        Main.menuScene = new Scene(programPane, 1300, 750);
        Main.primaryStage.setScene(Main.menuScene);
    }

    /**
     *  This method will update the timeline, by removing all
     *  notes on the timeline, and adding all notes from the
     *  Main.notes LinkedList to the timeline GridPane.
     */
    public static void updateTimeline() {

        // Only remove all notes in the UI if there are any.
        if (timelineGrid.getChildren().size() > 0) {
            // Remove all the previous notes from the timeline gridpane.
            timelineGrid.getChildren().removeAll(timelineGrid.getChildren());
        }

        // Store the amount of rows, this will be updated as we add each note.
        // The purpose of this of this, is to be able to add the amount of rowconstraints
        // - needed, for the UI to always look like it is supposed to.
        int rowCount = 0;

        // Iterate over each note in the Main.notes LinkedList,
        // and add them one by one.
        for(int i = 0; i < Main.notes.size(); i++) {
            if (rowCount < Main.notes.get(i).row) {
                rowCount = Main.notes.get(i).row;
            }

            // Create the note element for JavaFX
            VBox noteBox = new VBox();
            noteBox.setMinSize(160, 100);
            noteBox.setPrefSize(160, 100);
            noteBox.setMaxSize(160, 100);
            Label noteText = new Label(Main.notes.get(i).text);
            noteText.setWrapText(true);
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

            // Add the Right-Click event, which will delete the note.
            int finalI = i;
            noteBox.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                        Note.removeNoteFromLinkedList(Main.notes.get(finalI).order, Main.notes.get(finalI).row);
                        updateTimeline();
                    }
                }
            });

            // Set the position of the note in the grid
            timelineGrid.add(noteBox, x, y);
        }

        // Now we want to add rowconstraints to every row of notes we have,
        // we do this in case we have an empty row between two other rows.
        // If that was the case before and we did not have any rowconstraints,
        // then the UI would not look like we want it to look like.
        // Store the current amount of rowconstraints we currently have
        int currentRowConstraints = timelineGrid.getRowConstraints().size();

        // We have more rows than we have rowconstraints.
        // We will add the needed rowconstraints.
        // The result will be that we have the same amount of rows and rowconstraints
        if (currentRowConstraints < rowCount) {
            for (int i = 0; i < rowCount-currentRowConstraints; i++) {
                RowConstraints rowConstraint = new RowConstraints();
                rowConstraint.setMinHeight(100);
                rowConstraint.setPrefHeight(100);
                rowConstraint.setMaxHeight(100);
                timelineGrid.getRowConstraints().add(rowConstraint);
            }
        }

        // We have fewer rows than we have rowconstraints.
        // We will remove the rowconstraints that we do not need
        // The result will be that we have the same amount of rows and rowconstraints
        if (currentRowConstraints > rowCount) {
            for (int i = 1; i <= currentRowConstraints-rowCount; i++) {
                timelineGrid.getRowConstraints().remove(currentRowConstraints-i);
            }
        }
    }
}