package domain.strategy.save;

import domain.Storage;

/**
 * This class implements a saving algorithm, which saves a project to
 * a file on the computer. However, the real implementation is located
 * inside the Storage class.
 */
public class SingleUserSaveStrategy implements SaveStrategy {
    @Override
    public void save() {
        Storage.saveToFile();
    }
}
