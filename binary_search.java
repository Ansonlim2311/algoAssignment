import java.io.*;
import java.util.*;

public class binary_search {

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
        long start, end, bestTime, worstTime, time;
        long avgTime, avgBestTime, avgWorstTime;
        Random rand = new Random();
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
        int bestTarget = list.get((n / 2) - 1).number; // -1 because array is 0 based index so if row1 is index 0 so n now is 500 so its row 499
        int worstTarget = list.get(0).number - 1;
        int randNum = rand.nextInt(n);
        int target = list.get(randNum).number;

        String outputFile = "binary_search_step_" + list.size() + ".txt";

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {  
            binarySearch(list, bestTarget);
        }
        end = System.nanoTime();
        bestTime = end - start;
        avgBestTime = bestTime / n;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            binarySearch(list, target);
        }
        end = System.nanoTime();
        time = end - start;
        avgTime = time / n;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            binarySearch(list, worstTarget);
        }
        end = System.nanoTime();
        worstTime = end - start;
        avgWorstTime = worstTime / n;

        writeStepsToFile(outputFile, avgBestTime, avgWorstTime, avgTime);
        System.out.println("Binary search steps written to " + outputFile);

        scanner.close();
    }

    public static List<RowData> readCSV(String fileName) {
        List<RowData> list = new ArrayList<>();
        int number;
        String text;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            for (int index = 1; ((line = br.readLine()) != null); index++) {
                String[] parts = line.split(",", 2);
                // if (parts.length == 2) {
                number = Integer.parseInt(parts[0].trim());
                text = parts[1];
                list.add(new RowData(number, text, index));
                // }
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

    public static void writeStepsToFile(String outputFile, long bestTime, long worstTime, long avgTime) {
        try (PrintWriter bw = new PrintWriter(new FileWriter(outputFile))) {
            bw.println("Best Time: " + bestTime + " ns");
            bw.println("Average Time: " + avgTime + " ns");
            bw.println("Worst Time: " + worstTime + " ns");
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}
