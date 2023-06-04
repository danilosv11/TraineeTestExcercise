package danilosv11;

import java.util.List;
import java.util.Scanner;

public class TraineeTestExercise {
    private static final String FILE_PATH = "/airports.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Для выхода из программы введите '!quit'");
        System.out.print("Введите фильтр: ");
        String filter = scanner.nextLine();

        while (!filter.equals("!quit")) {
            System.out.print("Введите начало имени: ");
            String namePrefix = scanner.nextLine();

            long startTime = System.currentTimeMillis();
            AirportDataLoader dataLoader = new AirportDataLoader(FILE_PATH);
            List<String> filteredData = dataLoader.searchData(filter, namePrefix);
            long finishTime = System.currentTimeMillis();

            for (String data : filteredData) {
                System.out.println(data);
            }

            System.out.println("Найдено " + filteredData.size() + " аэропортов.");
            System.out.println("Затраченное время: " + (finishTime - startTime) + " мс");

            System.out.print("Введите фильтр: ");
            filter = scanner.nextLine();
        }

        scanner.close();
        System.out.println("Программа завершена.");
    }

}


