package domain.strategy;

import domain.Storage;

public class SingleUserSaveStrategy implements SaveStrategy {
    @Override
    public void save() {
        Storage.saveToFile();
    }
}
