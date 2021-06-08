package domain.strategy;

import domain.Main;
import domain.Storage;

/**
 * This class implements a saving algorithm, which saves a project to
 * a database. However, the real implementation is located inside the
 * Storage class.
 */
public class MultiUserSaveStrategy implements SaveStrategy {
    @Override
    public void save() {
        Storage.saveToDatabase(Main.projectID);
    }
}
