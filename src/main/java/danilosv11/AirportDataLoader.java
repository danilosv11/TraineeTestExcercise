package danilosv11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AirportDataLoader implements DataLoader {
    private String filePath;

    public AirportDataLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<String> searchData(String filter, String namePrefix) {
        List<String> results = new ArrayList<>();
        int filterColumnIndex = getFilterColumnIndex(filter);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\"", "");
                String[] values = line.split(",");
                String name = values[1];

                if (name.toLowerCase().startsWith(namePrefix.toLowerCase())) {
                    if (filter.isEmpty() || assessFilter(values, filter, filterColumnIndex)) {
                        results.add(name + Arrays.toString(values));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return results;
    }

    private static int getFilterColumnIndex(String filter) {
        if (filter.isEmpty()) {
            return -1;
        }

        String column = filter.split("[<>=]")[0];
        String index = column.substring(column.indexOf('[') + 1, column.indexOf(']'));
        return Integer.parseInt(index) - 1;
    }

    private static boolean assessFilter(String[] data, String filter, int filterColumnIndex) {
        String[] conditions = filter.split("\\|\\|");
        for (String condition : conditions) {
            String[] splitconditions = condition.split("&");
            boolean flag = true;
            for (String splitcondition : splitconditions) {
                if (!assessCondition(data, splitcondition, getFilterColumnIndex(splitcondition))) {
                    flag = false;
                }
            }
            if (flag) {
                return true;
            }
        }
        return false;
    }

    private static boolean assessCondition(String[] data, String condition, int filterColumnIndex) {
        condition=condition.trim();
        String[] parts = condition.split("[<>=]");
        if (parts.length > 1) {
            String column = parts[0];
            String operator = condition.substring(column.length(), condition.indexOf(parts[1]));

            if (filterColumnIndex != -1 && filterColumnIndex < data.length) {
                String value = data[filterColumnIndex].trim().replaceAll("\"", "");
                String comparisonValue = parts[1].trim().replaceAll("\'", "");
                switch (operator) {
                    case "=":
                        return value.equals(comparisonValue);
                    case "<>":
                        return !value.equals(comparisonValue);
                    case ">":
                        return Double.parseDouble(value) > Double.parseDouble(comparisonValue);
                    case "<":
                        return Double.parseDouble(value) < Double.parseDouble(comparisonValue);
                }
            }
        }
        return false;
    }
}
