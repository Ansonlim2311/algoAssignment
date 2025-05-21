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
    private static int partition(RowData[] S, int left, int right) {
        List<RowData> L = new ArrayList<>();
        List<RowData> E = new ArrayList<>();
        List<RowData> G = new ArrayList<>();
        // Choose last element as pivot
        int pivot = S[right].number;  // Last element as pivot

        for (int i = left; i <= right; i++) {
            int e = S[i].number ;
            if (e < pivot) {
                L.add(S[i]);
            } else if (e == pivot) {
                E.add(S[i]);
            } else {
                G.add(S[i]);
            }
        }
        // Reconstruct S with L, E, G
        int index = left;
        for (int i = 0; i < L.size(); i++) {
            S[index++] = L.get(i);
        }
        int pivotIndex = index;  // Start of pivot equals
        for (int i = 0; i < E.size(); i++) {
            S[index++] = E.get(i);
        }
        for (int i = 0; i < G.size(); i++) {
            S[index++] = G.get(i);
        }
        return pivotIndex;
    }

    // QuickSort function
    private static void quicksort(RowData[] S, int left, int right) {
        if (left < right) {
            int pi = partition(S, left, right);
            quicksort(S, left, pi - 1);
            quicksort(S, pi + 1, right);
        }
    }

    // Read data from CSV
    private static RowData[] readCSV(String filename) {
        List<RowData> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String text = parts[1];
                    data.add(new RowData(number, text));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
        return data.toArray(new RowData[0]);
    }

    // Write sorted data to CSV
    private static void writeStepsToFile(String filename, RowData[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (RowData rd : data) {
                writer.write(rd.number + "," + rd.text + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static double runAndTimeSort(RowData[] data) {
        long start = System.nanoTime();
        quicksort(data, 0, data.length - 1);
        long end = System.nanoTime();
        double duration = (end - start) / 1e6;  // convert to milliseconds
        System.out.printf("Running time: %.3f ms%n", duration);
        return duration;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        RowData[] data = readCSV(inputFile);

        // Run and time the sorting
        runAndTimeSort(data);

        // Write sorted output
        String outputFile = "quick_sort_" + data.length + ".csv";
        writeStepsToFile(outputFile, data);

        scanner.close();
    }
}