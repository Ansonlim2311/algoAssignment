import java.io.*;
import java.util.*;

public class merge_sort {

    public static class RowData {
        int number;
        String text;

        RowData(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        RowData[] numbers = readCSVRange(inputFile);
        if (numbers == null) {
            System.err.println("Error: Unable to read dataset.");
            scanner.close();
            return;
        }

        String outputFile = "merge_sort_" + numbers.length + ".csv";

        runAndTimeSort(numbers);

        writeStepsToFile(outputFile, numbers);
        System.out.println("Merge sort steps written to " + outputFile);

        scanner.close();
    }

    public static RowData[] readCSVRange(String filename) {
        List<RowData> list = new ArrayList<>();
        int number;
        String text;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                // if (parts.length == 2) {
                number = Integer.parseInt(parts[0].trim());
                text = parts[1];
                list.add(new RowData(number, text));
                // }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return list.toArray(new RowData[0]);
    }

    public static void mergeSort(RowData[] S) {
        mergeSort(S, 0, S.length - 1);
    }

    public static void mergeSort(RowData[] S, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(S, left, mid);
            mergeSort(S, mid + 1, right);
            merge(S, left, mid, right);
        }
    }

    public static void merge(RowData[] S, int left, int mid, int right) {
        LinkedList<RowData> L = new LinkedList<>();
        for (int i = 0; i < mid - left + 1; i++)
            L.add(S[left + i]);

        LinkedList<RowData> R = new LinkedList<>();
        for (int j = 0; j < right - mid; j++)
            R.add(S[mid + 1 + j]);

        int k = left;

        while (!L.isEmpty() && !R.isEmpty()) {
            if (L.getFirst().number <= R.getFirst().number) {
                S[k++] = L.removeFirst();
            } else {
                S[k++] = R.removeFirst();
            }
        }

        while (!L.isEmpty()) {
            S[k++] = L.removeFirst();
        }

        while (!R.isEmpty()) {
            S[k++] = R.removeFirst();
        }
    }

    public static void writeStepsToFile(String filename, RowData[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < data.length; i++) {
                RowData row = data[i];
                bw.write(row.number + "," + row.text + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static double runAndTimeSort(RowData[] data) {
        long start, end;
        double duration;
        start = System.currentTimeMillis();
        mergeSort(data);
        end = System.currentTimeMillis();
        duration = (end - start) / 1000;
        System.out.printf("Running time: %.3f second%n", duration);
        return duration;
    }
}