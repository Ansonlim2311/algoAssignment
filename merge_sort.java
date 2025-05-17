import java.io.*;
import java.util.*;

public class merge_sort {
    static List<String> logSteps = new ArrayList<>();

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

        String outputFile = "merge_sort " + numbers.length + ".csv";
        
        mergeSort(numbers);

        writeStepsToFile(outputFile);
        System.out.println("Merge sort steps written to " + outputFile);

        scanner.close();
    }

    public static RowData[] readCSVRange(String filename) {
        List<RowData> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0].trim());
                    String text = parts[1];
                    list.add(new RowData(number, text));
                }
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
        String log;
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

        log = "";
        for (int i = 0; i < S.length; i++) {
            log = log + S[i].number + "," + S[i].text + "\n";
        }
        logSteps.add(log);
    }

    public static void writeStepsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            if (!logSteps.isEmpty()) {
                bw.write(logSteps.get(logSteps.size() - 1));
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}