package domain.strategy.delete;

import domain.Main;
import domain.Storage;
import domain.UI;

public class MultiUserDeleteStrategy implements DeleteStrategy {
    @Override
    public void delete() {
        // This will remove all the data for the given project ID,
        // in the timeline table in the database.
        Storage.deleteProjectDBContent(Main.projectID);

        // Delete all the notes in the notes LinkedList
        Main.notes.clear();

        // Update the UI so show the updated notes LinkedList, with no notes.
        UI.updateTimeline();
    }
}
