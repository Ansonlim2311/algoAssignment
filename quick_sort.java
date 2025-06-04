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

    private static int partition(RowData[] S, int left, int right) {
        List<RowData> L = new ArrayList<>();
        List<RowData> E = new ArrayList<>();
        List<RowData> G = new ArrayList<>();
        int pivot = S[right].number;

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

        int index = left;
        for (int i = 0; i < L.size(); i++) {
            S[index++] = L.get(i);
        }
        int pivotIndex = index;
        for (int i = 0; i < E.size(); i++) {
            S[index++] = E.get(i);
        }
        for (int i = 0; i < G.size(); i++) {
            S[index++] = G.get(i);
        }
        return pivotIndex;
    }

    private static void quicksort(RowData[] S, int left, int right) {
        if (left < right) {
            int pi = partition(S, left, right);
            quicksort(S, left, pi - 1);
            quicksort(S, pi + 1, right);
        }
    }

    private static RowData[] readCSV(String filename) {
        List<RowData> data = new ArrayList<>();
        int number;
        String text;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                // if (parts.length == 2) {
                number = Integer.parseInt(parts[0].trim());
                text = parts[1];
                data.add(new RowData(number, text));
                // }
            }
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
        return data.toArray(new RowData[0]);
    }

    private static void writeStepsToFile(String filename, RowData[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < data.length; i++) {
                RowData rd = data[i];
                writer.write(rd.number + "," + rd.text + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static double runAndTimeSort(RowData[] data) {
        long start, end;
        double duration;
        start = System.currentTimeMillis();
        quicksort(data, 0, data.length - 1);
        end = System.currentTimeMillis();
        duration = (end - start) / 1000;
        System.out.printf("Running time: %.3f second%n", duration);
        return duration;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        RowData[] data = readCSV(inputFile);
        String outputFile = "quick_sort_" + data.length + ".csv";

        runAndTimeSort(data);
        writeStepsToFile(outputFile, data);

        scanner.close();
    }
}