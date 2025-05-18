import java.io.*;
import java.util.*;

public class quick_sort {
    static class RowData {
        int number;
        String text;

        RowData(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    // Partition function using ArrayLists
    private static int partition(List<RowData> S, int left, int right) {
        List<RowData> L = new ArrayList<>();
        List<RowData> E = new ArrayList<>();
        List<RowData> G = new ArrayList<>();
        // Choose last element as pivot
        int pivot = S.get(right).number;
        // Divide S[left..right] into L, E, G
        for (int i = left; i <= right; i++) {
            RowData e = S.get(i);
            if (e.number < pivot)
                L.add(e);
            else if (e.number == pivot)
                E.add(e);
            else
                G.add(e);
        }
        // Reconstruct S with L, E, G
        int k = left;
        for (int i = 0; i < L.size(); i++) {
            S.set(k++, L.get(i));
        }
        int pi = k;  // pivot index
        for (int i = 0; i < E.size(); i++) {
            S.set(k++, E.get(i));
        }
        for (int i = 0; i < G.size(); i++) {
            S.set(k++, G.get(i));
        }
        return pi;
    }

    // QuickSort function
    private static void quicksort(List<RowData> S, int left, int right) {
        if (left < right) {
            int pi = partition(S, left, right);
            quicksort(S, left, pi - 1);
            quicksort(S, pi + 1, right);
        }
    }

    // Read data from CSV
    private static List<RowData> readCSV(String filename) {
        List<RowData> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                int num = Integer.parseInt(parts[0].trim());
                String text = parts.length > 1 ? parts[1] : "";
                data.add(new RowData(num, text));
            }
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
        return data;
    }

    // Write sorted data to CSV
    private static void writeCSV(String filename, List<RowData> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (RowData rd : data) {
                writer.write(rd.number + "," + rd.text + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static double runAndTimeSort(List<RowData> data) {
        long start = System.nanoTime();
        quicksort(data, 0, data.size() - 1);
        long end = System.nanoTime();
        double duration = (end - start) / 1e6;  // convert to milliseconds
        System.out.printf("Running time: %.3f ms%n", duration);
        return duration;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        List<RowData> data = readCSV(inputFile);

        // Run and time the sorting
        double duration = runAndTimeSort(data);

        // Write sorted output
        String outputFile = "quick_sort_" + data.size() + ".csv";
        writeCSV(outputFile, data);

        scanner.close();
    }
}