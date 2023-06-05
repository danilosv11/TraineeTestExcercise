package danilosv11;

import java.util.List;
import java.util.Scanner;

public class TraineeTestExercise {
    private static final String FILE_PATH = "/airports.csv";

    public static void main(String[] args) {
        AirportDataLoader airportDataLoader = new AirportDataLoader(FILE_PATH);
        airportDataLoader.loadData();

        System.out.println("Для выхода напишите '!quit'");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Фильтр: ");
        String filter = scanner.nextLine();

        while (!filter.equals("!quit")) {
            System.out.print("Начало имени аэропорта: ");
            String startWith = scanner.nextLine();

            long startTime = System.currentTimeMillis();
            List<String> filteredData = airportDataLoader.searchAirports(filter, startWith);
            long endTime = System.currentTimeMillis();

            for (String data : filteredData) {
                System.out.println(data);
            }

            System.out.println("Найдено " + filteredData.size() + " аэропортов");
            System.out.println("Время: " + (endTime - startTime) + " мс");

            System.out.print("Новый фильтр: ");
            filter = scanner.nextLine();
        }

        scanner.close();
    }
}


