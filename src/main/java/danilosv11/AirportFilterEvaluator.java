package danilosv11;

import java.util.Arrays;

public class AirportFilterEvaluator implements FilterEvaluator{
    @Override
    public boolean assessFilter(String[] data, String filter) {
        return Arrays.stream(filter.split("&"))
                .allMatch(condition -> Arrays.stream(condition.split("\\|\\|"))
                        .anyMatch(splitCondition -> assessCondition(data, splitCondition.trim())));
    }

    @Override
    public boolean assessCondition(String[] data, String condition) {
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
