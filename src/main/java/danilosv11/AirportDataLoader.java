package danilosv11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class AirportDataLoader implements DataLoader {
    private String filePath;
    private static List<String> airportData;

    public AirportDataLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<String> searchAirports(String filter, String startWith) {
        return airportData.stream()
                .map(data -> data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                .filter(values -> {
                    String name = values[1].replaceAll("\"", "");
                    return name.toLowerCase().startsWith(startWith.toLowerCase()) &&
                            (filter.isEmpty() || assessFilter(values, filter));
                })
                .map(values -> values[1].replaceAll("\"", "") +
                        Arrays.toString(values))
                .collect(Collectors.toList());
    }

    @Override
    public void loadData() {
        airportData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            airportData = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }



    private static boolean assessFilter(String[] data, String filter) {
        return Arrays.stream(filter.split("&"))
                .allMatch(condition -> Arrays.stream(condition.split("\\|\\|"))
                        .anyMatch(splitCondition -> assessCondition(data, splitCondition.trim())));
    }

    private static boolean assessCondition(String[] data, String condition) {
        String[] parts = condition.split("[<>=]");
        int column = Integer.parseInt(parts[0].substring(parts[0].indexOf('[') + 1, parts[0].indexOf(']'))) - 1;
        String operator = condition.replaceAll("[^<>=]", "");
        String filterValue = parts[1].replaceAll("'", "").toLowerCase();

        String comporationValue = data[column].replaceAll("\"", "").toLowerCase();
        switch (operator) {
            case "=":
                return comporationValue.equals(filterValue);
            case "<>":
                return !comporationValue.equals(filterValue);
            case "<":
                return Double.parseDouble(filterValue) > Double.parseDouble(comporationValue);
            case ">":
                return Double.parseDouble(filterValue) < Double.parseDouble(comporationValue);
            default:
                return true;
        }
    }
}
