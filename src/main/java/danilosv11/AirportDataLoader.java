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
        AirportFilterEvaluator airportFilterEvaluator = new AirportFilterEvaluator();
        return airportData.stream()
                .map(data -> data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                .filter(values -> {
                    String name = values[1].replaceAll("\"", "");
                    return name.toLowerCase().startsWith(startWith.toLowerCase()) &&
                            (filter.isEmpty() || airportFilterEvaluator.assessFilter(values, filter));
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
}
