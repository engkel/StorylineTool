import java.io.*;
import java.util.Base64;

public class Storage {
    // A base64 decoder instance, used multiple times throughout the class.
    private static final Base64.Decoder decoder = Base64.getDecoder();

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
     *
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
        // Add every note we find to the Main.notes LinkedList.
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
     *
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
     *
     */
    public static void saveToFile() {
        // Make sure the project.dat file is created
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
        Base64.Encoder encoder = Base64.getEncoder();

        boolean addedFirstNote = false;
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

        // Write the project data to the project.dat file.
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
     *
     * @param projectID
     */
    public static void saveToDatabase(int projectID) {
        System.out.println("Saving to database...");

        //DB.deleteSQL("DELETE * FROM tbl_timeline where fld_project_id = " + projectID);

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT () "); // Work in progress here!


    }

    public static void tmpPrintAllSelectResults() {
        System.out.println("Showing the select results:");

        do {
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)) {
                break;
            } else {
                System.out.print(data);
            }
        } while (true);
    }
}
