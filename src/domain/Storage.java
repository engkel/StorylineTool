package domain;

import persistence.DB;

import java.io.*;
import java.util.Base64;

public class Storage {
    // A base64 decoder instance, used multiple times throughout the class.
    private static final Base64.Decoder decoder = Base64.getDecoder();
    // A base64 encoder instance, used multiple times throughout the class.
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Checks whether the file at the given path exists.
     * @param path Path to the file
     * @return Boolean, true = file exists, false = file does not exist.
     */
    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    /**
     *  This method loads all the notes from the project.dat file,
     *  and adds them all to the Main.notes LinkedList of notes.
     */
    public static void loadFromFile() throws IOException {
        // Stop this function if there is no save file.
        if (!fileExists("project.dat")) {
            return;
        }

        FileReader saveFileReader = new FileReader("project.dat");
        BufferedReader br = new BufferedReader(saveFileReader);

        String fileLine;

        String noteText = ""; // Initializing to avoid "Might not have been initialized error"
        int noteOrder = 0;// Initializing to avoid "Might not have been initialized error"
        // noteRow has no need to be stored

        int lineNumber = 0;

        // Iterate over every line in the project.dat file.
        // Add every note we find to the domain.Main.notes LinkedList.
        while((fileLine=br.readLine()) != null) {
            switch (lineNumber % 3) {
                case 0:
                    noteText = new String(decoder.decode(fileLine));
                    break;
                case 1:
                    // This can break if the file becomes corrupt for any reason!
                    noteOrder = Integer.parseInt(fileLine);
                    break;
                case 2:
                    // This can break if the file becomes corrupt for any reason!
                    Main.notes.add(new Note(noteText, noteOrder, Integer.parseInt(fileLine)));
                    break;
            }
            lineNumber++;
        }
        saveFileReader.close();
    }

    /**
     * This method loads all the notes from the tbl_timeline table,
     * and adds them all to the Main.notes LinkedList of notes.
     * @param projectID - The project ID to get the notes from. Currently all notes are hardcoded to use project ID 0.
     */
    public static void loadFromDatabase(int projectID) {
        // Create the query string
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT fld_text, fld_order, fld_row FROM tbl_timeline WHERE fld_project_id = ");
        sb.append(projectID);
        sb.append(";");

        // Query the database for notes
        DB.selectSQL(sb.toString());

        int lineNumber = 0;
        String noteText = ""; // Initializing to avoid "Might not have been initialized error"
        int noteOrder = 0;// Initializing to avoid "Might not have been initialized error"

        do {
            String data = DB.getData();
            if (data.equals(DB.NOMOREDATA)) {
                break;
            }

            switch (lineNumber % 3) {
                case 0:
                    try {
                        noteText = new String(decoder.decode(data));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid note text was given from the Database, skipping to the next note!");
                    }
                    break;
                case 1:
                    noteOrder = Integer.parseInt(data);
                    break;
                case 2:
                    // Add the note to the LinkedList of notes
                    Main.notes.add(new Note(noteText, noteOrder, Integer.parseInt(data)));
                    break;
            }

            lineNumber++;
        } while (true);

    }

    /**
     *  This method will store all the notes from the Main.notes LinkedList
     *  into the project.dat file. The note text is saved as base64.
     *  One line is used for each piece of data. Text, order and row,
     *  this is repeated for each note in the LinkedList.
     */
    public static void saveToFile() {
        // Make sure the project.dat file exists.
        try {
            File saveFile = new File("project.dat");
            if (saveFile.createNewFile()) {
                System.out.println("The project.dat file was successfully created.");
            } else {
                System.out.println("Using the already existing project.dat file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving the project.");
            e.printStackTrace();
        }

        // Create a new StringBuilder to craft the save file.
        StringBuilder data = new StringBuilder();

        boolean addedFirstNote = false;
        // Add the note data to the StringBuilder, which will all be written to the file later.
        for (Note n : Main.notes) {
            // Add a newline after the row number from the previous note
            if (addedFirstNote) {
                data.append('\n');
            }
            data.append(encoder.encodeToString(n.text.getBytes()));
            data.append('\n');
            data.append(n.order);
            data.append('\n');
            data.append(n.row);
            addedFirstNote = true;
        }

        // Try to write the project data to the project.dat file.
        try {
            FileWriter saveFileWriter = new FileWriter("project.dat");
            saveFileWriter.write(data.toString());
            saveFileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the project.dat file.");
            e.printStackTrace();
        }
    }

    /**
     *  This method will save the notes from the Main.notes LinkedList,
     *  into the tbl_timeline table. Before inserting the data, the previous
     *  data for the given project ID is removed, to remove old data.
     * @param projectID - The project ID to get the notes from. Currently all notes are hardcoded to use project ID 0.
     */
    public static void saveToDatabase(int projectID) {
        System.out.println("Saving to database...");

        // Delete the old content from the database
        deleteProjectDBContent(projectID);

        // This boolean help keep track of if we already added any rows.
        // The reason for this is to add commas after each row to add.
        boolean addedFirstRow = false;

        // Stop the function here, as there is no notes to be inserted into the database.
        if (Main.notes.size() == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO storylineTool.dbo.tbl_timeline (fld_project_id, fld_text, fld_row, fld_order) VALUES "); // Work in progress here!

        for (Note n : Main.notes) {
            // Add a comma if this is NOT the first row to be inserted.
            if (addedFirstRow) {
                sb.append(", (");
            } else {
                sb.append('(');
                addedFirstRow = true;
            }
            sb.append(projectID);
            sb.append(", '");
            // We store the note text as base64 (To avoid problems with certain characters)
            sb.append(encoder.encodeToString(n.text.getBytes()));
            sb.append("', ");
            sb.append(n.row);
            sb.append(", ");
            sb.append(n.order);
            sb.append(')');
        }

        // Insert the data to the table!
        DB.insertSQL(sb.toString());
    }

    /**
     * This method will remove all the data in the project.dat file.
     * In other words, it will delete the project.
     */
    public static void deleteFileContent() {
        System.out.println("Deleting the project data in file!");
        // Make sure the project.dat file exists.
        try {
            File saveFile = new File("project.dat");
            if (saveFile.createNewFile()) {
                System.out.println("The project.dat did not exist, we will create it again.");
            } else {
                System.out.println("Using the already existing project.dat file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while delete the project data.");
            e.printStackTrace();
        }

        // Try to write the empty string to the project.dat file.
        // This will get rid of all the previous data, if any.
        try {
            FileWriter saveFileWriter = new FileWriter("project.dat");
            saveFileWriter.write("");
            saveFileWriter.close();
            System.out.println("Successfully deleted all data in the project.dat file.");
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the project.dat file.");
            e.printStackTrace();
        }

        // Clear the Main.notes LinkedList since
        // the user deleted the project.
        Main.notes.clear();

        // Update the UI, which in this case
        // makes all the notes disappear.
        UI.updateTimeline();
    }

    /**
     * This method will remove all the data, for the given project ID,
     * in the timeline table in the database. In other words, it will
     * delete the project currently being worked on.
     */
    public static void deleteProjectDBContent(int projectID) {
        DB.deleteSQL("DELETE FROM storylineTool.dbo.tbl_timeline where fld_project_id = " + projectID);
    }

    /**
     * This method will export all the data in a readable format to a file.
     * The difference between saveToFile() and exportToFile(), is that the
     * saveToFile() is meant to store the project data for the program to
     * read and write to, where the text is stored in base64. Whereas the
     * exportToFile() is storing the data in a readable format with
     * indentation based on the row.
     */
    public static void exportToFile() {
        // Make sure the project.dat file exists.
        try {
            File saveFile = new File("project_export.txt");
            if (saveFile.createNewFile()) {
                System.out.println("The project_export.txt file was successfully created.");
            } else {
                System.out.println("The project_export.txt already exists, we will overwrite the data.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while exporting the project.");
            e.printStackTrace();
        }

        // Create a new StringBuilder to craft the export data.
        StringBuilder data = new StringBuilder();

        boolean addedFirstLine = false;
        // Add the note data to the StringBuilder, which will all be written to the file later.
        for (Note n : Main.notes) {
            // Add a newline after the previous line
            if (addedFirstLine) {
                data.append('\n');
            }

            // Add the tabs before adding the line text
            for (int i = 1; i < n.row; i++) {
                data.append('\t');
            }

            // Add the note text to the current line
            data.append(n.text);

            // Set addedFirstLine to true so that the program will
            // add a new line if there are any another notes.
            addedFirstLine = true;
        }

        // Try to write the export data to the project_export.txt file.
        try {
            FileWriter saveFileWriter = new FileWriter("project_export.txt");
            saveFileWriter.write(data.toString());
            saveFileWriter.close();
            System.out.println("Successfully exported the project to project_export.txt.");
        } catch (IOException e) {
            System.out.println("An error occurred while exporting the project to the project_export.txt file.");
            e.printStackTrace();
        }
    }
}
