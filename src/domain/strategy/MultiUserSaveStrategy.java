package domain.strategy;

import domain.Main;
import domain.Storage;

public class MultiUserSaveStrategy implements SaveStrategy {
    @Override
    public void save() {
        Storage.saveToDatabase(Main.projectID);
    }
}
