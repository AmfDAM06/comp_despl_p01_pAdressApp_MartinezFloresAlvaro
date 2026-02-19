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
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.scene.paint.Color;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.beans.Observable;
import javafx.scene.web.WebView;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.dansoftware.pdfdisplayer.PDFDisplayer;
import javafx.scene.control.Alert;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList(
            person -> new Observable[]{person.birthdayProperty()}
    );

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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.NONE);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

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

    public void showGenerationsDonut() {
        Stage donutStage = new Stage();
        donutStage.setTitle("Generations Donut Chart");
        donutStage.initModality(Modality.NONE);
        donutStage.initOwner(primaryStage);
        donutStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

        Tile donutTile = TileBuilder.create()
                .skinType(Tile.SkinType.DONUT_CHART)
                .title("Generations Donut")
                .backgroundColor(Color.TRANSPARENT)
                .build();

        donutTile.getStyleClass().add("tile");

        VBox legendBox = new VBox(10);
        legendBox.setAlignment(Pos.CENTER_LEFT);
        legendBox.setPadding(new Insets(20));

        updateDonutDataWithLegend(donutTile, legendBox);

        personData.addListener((ListChangeListener<Person>) c -> updateDonutDataWithLegend(donutTile, legendBox));

        HBox root = new HBox(20, donutTile, legendBox);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 450);
        root.getStyleClass().add("donut-tile-container");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(MainApp.class.getResource("view/DarkTheme.css").toExternalForm());

        donutStage.setScene(scene);
        donutStage.show();
    }

    private void updateDonutDataWithLegend(Tile donutTile, VBox legendBox) {
        int genZ = 0;
        int millennials = 0;
        int genX = 0;
        int boomers = 0;
        int others = 0;

        for (Person p : personData) {
            int year = p.getBirthday().getYear();
            if (year >= 1997 && year <= 2012) genZ++;
            else if (year >= 1981 && year <= 1996) millennials++;
            else if (year >= 1965 && year <= 1980) genX++;
            else if (year >= 1946 && year <= 1964) boomers++;
            else others++;
        }

        donutTile.clearChartData();
        donutTile.addChartData(new ChartData("Gen Z", genZ, Color.web("#4facfe")));
        donutTile.addChartData(new ChartData("Millennials", millennials, Color.web("#00f2fe")));
        donutTile.addChartData(new ChartData("Gen X", genX, Color.web("#43e97b")));
        donutTile.addChartData(new ChartData("Baby Boomers", boomers, Color.web("#38f9d7")));
        donutTile.addChartData(new ChartData("Others", others, Color.web("#fa709a")));

        legendBox.getChildren().clear();
        legendBox.getChildren().addAll(
                createLegendItem("Gen Z", Color.web("#4facfe")),
                createLegendItem("Millennials", Color.web("#00f2fe")),
                createLegendItem("Gen X", Color.web("#43e97b")),
                createLegendItem("Baby Boomers", Color.web("#38f9d7")),
                createLegendItem("Others", Color.web("#fa709a"))
        );
    }

    private HBox createLegendItem(String name, Color color) {
        Circle circle = new Circle(8, color);
        Label label = new Label(name);
        label.getStyleClass().add("label");
        HBox item = new HBox(10, circle, label);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private void updateDonutData(Tile donutTile) {
        int genZ = 0;
        int millennials = 0;
        int genX = 0;
        int boomers = 0;
        int others = 0;

        for (Person p : personData) {
            int year = p.getBirthday().getYear();
            if (year >= 1997 && year <= 2012) genZ++;
            else if (year >= 1981 && year <= 1996) millennials++;
            else if (year >= 1965 && year <= 1980) genX++;
            else if (year >= 1946 && year <= 1964) boomers++;
            else others++;
        }

        donutTile.clearChartData();
        donutTile.addChartData(new ChartData("Gen Z", genZ, Color.web("#4facfe")));
        donutTile.addChartData(new ChartData("Millennials", millennials, Color.web("#00f2fe")));
        donutTile.addChartData(new ChartData("Gen X", genX, Color.web("#43e97b")));
        donutTile.addChartData(new ChartData("Baby Boomers", boomers, Color.web("#38f9d7")));
        donutTile.addChartData(new ChartData("Others", others, Color.web("#fa709a")));
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("About");
        alert.setHeaderText("AddressApp");
        alert.setContentText("Desarrollado por Álvaro Martínez Flores.");
        alert.showAndWait();
    }

    public void showHelpHtml() {
        try {
            WebView webView = new WebView();
            URL url = getClass().getResource("/help/html/index.html");

            if (url != null) {
                webView.getEngine().load(url.toExternalForm());
            } else {
                webView.getEngine().loadContent("<html><body><h2>Error loading HTML</h2></body></html>");
            }

            Stage stage = new Stage();
            stage.setTitle("Help - HTML");
            stage.setScene(new Scene(webView, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHelpMarkdown() {
        try {
            URL resourceUrl = getClass().getResource("/help/markdown/README.md");
            String markdownContent = "";

            if (resourceUrl != null) {
                Path path = Paths.get(resourceUrl.toURI());
                markdownContent = Files.readString(path);
            }

            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            Node document = parser.parse(markdownContent);
            String htmlContent = renderer.render(document);

            WebView webView = new WebView();
            webView.getEngine().loadContent(htmlContent);

            Stage stage = new Stage();
            stage.setTitle("Help - Markdown");
            stage.setScene(new Scene(webView, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHelpPdf() {
        try {
            PDFDisplayer displayer = new PDFDisplayer();
            Scene scene = new Scene(displayer.toNode(), 800, 600);
            URL pdfUrl = getClass().getResource("/help/pdf/ayuda.pdf");

            if (pdfUrl != null) {
                File pdfFile = new File(pdfUrl.toURI());
                displayer.loadPDF(pdfFile);
            }

            Stage stage = new Stage();
            stage.setTitle("Help - PDF");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}