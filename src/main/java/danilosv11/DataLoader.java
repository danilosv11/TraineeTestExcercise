package danilosv11;

import java.util.List;

interface DataLoader {
    List<String> searchData(String filter, String namePrefix);
}