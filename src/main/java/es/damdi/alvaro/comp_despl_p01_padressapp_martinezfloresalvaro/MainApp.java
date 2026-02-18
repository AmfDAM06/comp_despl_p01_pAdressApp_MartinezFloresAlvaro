package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.JacksonPersonRepository;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence.PersonRepository;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.setting.AppPreferences;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view.PersonEditDialogController;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view.PersonOverviewController;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view.RootLayoutController;
import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view.BirthdayStatisticsController; // [Importante] Importar el nuevo controlador

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    private final PersonRepository repository = new JacksonPersonRepository();

    private boolean dirty;

    private File personFilePath;

    public MainApp() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp - Álvaro Martínez Flores");

        // Icono de la aplicación
        this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

        initRootLayout();
        showPersonOverview();

        // Dirty flag listener
        personData.addListener((javafx.collections.ListChangeListener<Person>) c -> setDirty(true));

        // Cargar datos al inicio
        loadOnStartup();
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

            // [Importante] Conectar el controlador del RootLayout con MainApp
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Give the controller access to the main app.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Opens a dialog to edit details for the specified person.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * [NUEVO] Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPersonFilePath(File file) {
        this.personFilePath = file;
        AppPreferences.setPersonFile(file == null ? null : file.getAbsolutePath());
        if (primaryStage != null) {
            String name = (file == null) ? "AddressApp - Álvaro Martínez Flores" : "AddressApp - " + file.getName();
            primaryStage.setTitle(name);
        }
    }

    public void loadPersonDataFromJson(File file) throws IOException {
        List<Person> loaded = repository.load(file);
        personData.setAll(loaded);
        setPersonFilePath(file);
        setDirty(false);
    }

    public void savePersonDataToJson(File file) throws IOException {
        repository.save(file, new ArrayList<>(personData));
        setPersonFilePath(file);
        setDirty(false);
    }

    private void loadOnStartup() {
        AppPreferences.getPersonFile().ifPresentOrElse(
                path -> {
                    File f = new File(path);
                    if (f.exists()) {
                        try {
                            loadPersonDataFromJson(f);
                        } catch (IOException e) {
                            loadDefaultIfExists();
                        }
                    } else {
                        loadDefaultIfExists();
                    }
                },
                this::loadDefaultIfExists
        );
    }

    private void loadDefaultIfExists() {
        File f = defaultJsonPath.toFile();
        if (f.exists()) {
            try {
                loadPersonDataFromJson(f);
            } catch (IOException ignored) { }
        } else {
            setPersonFilePath(f);
        }
    }

    private final Path defaultJsonPath =
            Paths.get(System.getProperty("user.home"), ".addressappv2", "persons.json");

    public File getPersonFilePath() {
        return personFilePath;
    }

    public static void main(String[] args) {
        launch(args);
    }
}