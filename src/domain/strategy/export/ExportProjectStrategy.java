package domain.strategy.export;

import domain.Storage;

public class ExportProjectStrategy implements ExportStrategy {

    @Override
    public void export() {
        Storage.exportToFile();
    }
}
