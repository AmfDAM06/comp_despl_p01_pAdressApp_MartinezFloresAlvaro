package es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view;

import es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.text.DateFormatSymbols;
import java.util.*;

public class BirthdayStatisticsController {

    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private CategoryAxis xAxis;
    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private void initialize() {
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));
        xAxis.setCategories(monthNames);
    }

    public void setPersonData(ObservableList<Person> persons) {
        updateCharts(persons);
        persons.addListener((ListChangeListener<Person>) c -> updateCharts(persons));
    }

    private void updateCharts(List<Person> persons) {
        updateBarChart(persons);
        updatePieChart(persons);
        updateLineChart(persons);
    }

    private void updateBarChart(List<Person> persons) {
        barChart.getData().clear();
        int[] monthCounter = new int[12];
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Birthdays");

        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
        }
        barChart.getData().add(series);
    }

    private void updatePieChart(List<Person> persons) {
        int genZ = 0;
        int millennials = 0;
        int genX = 0;
        int boomers = 0;
        int others = 0;

        for (Person p : persons) {
            int year = p.getBirthday().getYear();
            if (year >= 1997 && year <= 2012) genZ++;
            else if (year >= 1981 && year <= 1996) millennials++;
            else if (year >= 1965 && year <= 1980) genX++;
            else if (year >= 1946 && year <= 1964) boomers++;
            else others++;
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Gen Z", genZ),
                new PieChart.Data("Millennials", millennials),
                new PieChart.Data("Gen X", genX),
                new PieChart.Data("Baby Boomers", boomers),
                new PieChart.Data("Others", others)
        );

        pieChart.setData(pieData);
    }

    private void updateLineChart(List<Person> persons) {
        lineChart.getData().clear();
        Map<Integer, Integer> yearCounts = new TreeMap<>();

        for (Person p : persons) {
            int year = p.getBirthday().getYear();
            yearCounts.put(year, yearCounts.getOrDefault(year, 0) + 1);
        }

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Births Trend");

        for (Map.Entry<Integer, Integer> entry : yearCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);
    }
}