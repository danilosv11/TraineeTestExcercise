package danilosv11;

import java.util.List;

interface DataLoader {
    List<String> searchAirports(String filter, String startWith);

    void loadData();
}