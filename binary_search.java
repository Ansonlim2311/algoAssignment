import java.io.*;
import java.util.*;

public class binary_search {
    static List<String> logSteps = new ArrayList<>();

    static class RowData {
        int number;
        String text;
        int index;

        RowData(int number, String text, int index) {
            this.number = number;
            this.text = text;
            this.index = index;
        }
    }

    public static void main(String[] args) {
        long start, end, bestTime, worstTime;
        Scanner scanner = new Scanner (System.in);

        System.out.print("Enter DataSet Filename: ");
        String fileName = scanner.nextLine();

        List<RowData> list = readCSV(fileName);
        if (list == null) {
            System.err.println("Error: Unable to read dataset.");
            scanner.close();
            return;
        }

        int n = list.size();
        System.out.println(n);
        int bestTarget = list.get((n / 2) - 1).number;
        System.out.println(list.get(n/2).index);
        System.out.println(bestTarget);
        int worstTarget = list.get(0).number - 1;
        System.out.println(worstTarget);

        String outputFile = "binary_search_step_" + list.size() + ".txt";
        
        for (int i = 0; i < 20; i++) {
            binarySearch(list, bestTarget);
        }

        // Best case
        logSteps.clear();
        start = System.nanoTime();
        binarySearch(list, bestTarget);
        end = System.nanoTime();
        bestTime = end - start;

        List<String> bestCaseSteps = new ArrayList<>(logSteps);

        // Worst case
        logSteps.clear();
        start = System.nanoTime();
        binarySearch(list, worstTarget);
        end = System.nanoTime();
        worstTime = end - start;

        writeStepsToFile(outputFile, bestTime, worstTime, bestCaseSteps);
        System.out.println("Binary search steps written to " + outputFile);

        scanner.close();
    }

    public static List<RowData> readCSV(String fileName) {
        List<RowData> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            for (int index = 1; ((line = br.readLine()) != null); index++) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String text = parts[1];
                    list.add(new RowData(number, text, index));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return list;
    }

    public static void binarySearch(List<RowData> list, int targetValue) {
        int leftIndex = 0;
        int rightIndex = list.size() - 1;

        while (leftIndex <= rightIndex) {
            int midIndex = (leftIndex + rightIndex) / 2;
            RowData midData = list.get(midIndex);

            // logSteps.add(midData.index + ":" + midData.number + "/" + midData.text);

            if (midData.number == targetValue) {
                return;
            }
            else if (midData.number < targetValue) {
                leftIndex = midIndex + 1;
            }
            else if (midData.number > targetValue) {
                rightIndex = midIndex - 1;
            }
        }
    }

    public static void writeStepsToFile(String outputFile, long bestTime, long worstTime, List<String> bestCaseSteps) {
        try (PrintWriter bw = new PrintWriter(new FileWriter(outputFile))) {
            bw.println("Best Time: " + bestTime + " ns");
            bw.println("Worst Time: " + worstTime + " ns");
            // bw.println();
            // bw.println("Steps (worst case):");
            // for (String step : logSteps) {
            //     bw.println(step);
            // }
            // bw.println();
            // bw.println("Best Case Steps:");
            // for (String step : bestCaseSteps) {
            //     bw.println(step);
            // }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}
