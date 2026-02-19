package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view;

import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * The type Root layout controller.
 */
public class RootLayoutController {

    private MainApp mainApp;

    /**
     * Sets main app.
     *
     * @param mainApp the main app
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    // -------------------- MENU ACTIONS --------------------
    @FXML
    private void handleNew() {
        if (!confirmSaveIfDirty("New", "Create a new address book?")) {
            return; // cancelado
        }
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null); // “no guardado previamente”
        mainApp.setDirty(false);
    }
    @FXML
    private void handleOpen() {
        if (!confirmSaveIfDirty("Open", "Open another address book?")) {
            return; // cancelado
        }
        FileChooser fc = createJsonFileChooser("Open Address Book (JSON)");
        setInitialDirectory(fc);
        File file = fc.showOpenDialog(mainApp.getPrimaryStage());
        if (file == null) return;
        try {
            mainApp.loadPersonDataFromJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
        } catch (IOException e) {
            showError("Could not load data",
                    "Could not load data from file:\n" + file.getPath(), e);
        }
    }
    @FXML
    private void handleSave() {
        saveOrSaveAs(); // ✅ si no hay fichero -> Save As
    }
    @FXML
    private void handleSaveAs() {
        saveAs();
    }
    @FXML
    private void handleExit() {
        if (!confirmSaveIfDirty("Exit", "Exit application?")) {
            return; // cancelado
        }
        mainApp.getPrimaryStage().close();
    }
    @FXML
    private void handleAbout() {
        mainApp.showAbout();
    }

    @FXML
    private void handleHelpHtml() {
        mainApp.showHelpHtml();
    }

    @FXML
    private void handleHelpMarkdown() {
        mainApp.showHelpMarkdown();
    }

    @FXML
    private void handleHelpPdf() {
        mainApp.showHelpPdf();
    }
    // -------------------- SAVE LOGIC --------------------
    private boolean saveOrSaveAs() {
        File file = mainApp.getPersonFilePath();
        if (file == null) {
            return saveAs();
        }
        try {
            mainApp.savePersonDataToJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
            return true;
        } catch (IOException e) {
            showError("Could not save data",
                    "Could not save data to file:\n" + file.getPath(), e);
            return false;
        }
    }

    private boolean saveAs() {
        FileChooser fc = createJsonFileChooser("Save Address Book (JSON)");
        setInitialDirectory(fc);
        File file = fc.showSaveDialog(mainApp.getPrimaryStage());
        if (file == null) return false;
        // Asegurar extensión .json
        if (!file.getPath().toLowerCase().endsWith(".json")) {
            file = new File(file.getPath() + ".json");
        }
        try {
            mainApp.savePersonDataToJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
            return true;
        } catch (IOException e) {
            showError("Could not save data",
                    "Could not save data to file:\n" + file.getPath(), e);
            return false;
        }
    }

    // ---------------- CSV EXPORT / IMPORT ----------------

    @FXML
    private void handleShowDonutChart() {
        mainApp.showGenerationsDonut();
    }

    @FXML
    private void handleExportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().toLowerCase().endsWith(".csv")) {
                file = new File(file.getPath() + ".csv");
            }

            try {
                // Usamos el repositorio CSV para guardar
                es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.CsvPersonRepository csvRepo =
                        new es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.CsvPersonRepository();

                csvRepo.save(file, mainApp.getPersonData());

            } catch (IOException e) {
                showError("Export Error", "Could not save data to CSV file:\n" + file.getPath(), e);
            }
        }
    }

    @FXML
    private void handleImportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            try {
                es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.CsvPersonRepository csvRepo =
                        new es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.CsvPersonRepository();

                java.util.List<es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person> importedPersons = csvRepo.load(file);

                // Añadir a la lista existente
                mainApp.getPersonData().addAll(importedPersons);

            } catch (IOException e) {
                showError("Import Error", "Could not load data from CSV file:\n" + file.getPath(), e);
            }
        }
    }

    @FXML
    private void handleShowBirthdayStatistics() {
        mainApp.showBirthdayStatistics();
    }

    // -------------------- DIRTY CONFIRMATION --------------------
    private boolean confirmSaveIfDirty(String title, String header) {
        if (!mainApp.isDirty()) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("You have unsaved changes. What do you want to do?");
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType dontSave = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(save, dontSave, cancel);
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result == save) {
            return saveOrSaveAs();
        }
        if (result == dontSave) {
            return true;
        }
        return false;
    }
    // -------------------- HELPERS --------------------
    private FileChooser createJsonFileChooser(String title) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*")
        );
        return fc;
    }
    private void setInitialDirectory(FileChooser fc) {
        File current = mainApp.getPersonFilePath();
        if (current != null && current.getParentFile() != null && current.getParentFile().exists()) {
            fc.setInitialDirectory(current.getParentFile());
        } else {
            File home = new File(System.getProperty("user.home"));
            if (home.exists()) fc.setInitialDirectory(home);
        }
    }
    private void showError(String header, String content, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content + "\n\n" + e.getClass().getSimpleName() + ": " + e.getMessage());
        alert.showAndWait();
    }
}