package danilosv11;

public interface FilterEvaluator {
    boolean assessCondition(String[] data, String condition);

    boolean assessFilter(String[] data, String filter);
}
