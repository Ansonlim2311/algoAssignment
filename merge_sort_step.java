import java.io.*;
import java.util.*;

public class merge_sort_step {
    static List<String> logSteps = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter filename: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();

        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();
        
        String outputFile = "merge_sort_step_" + startRow + "_" + endRow + ".txt";

        int[] numbers = readCSVRange(inputFile, startRow, endRow);
        if (numbers == null) {
            return;
        }

        logSteps.add("Before MergeSort: " + Arrays.toString(numbers));
        mergeSort(numbers);
        logSteps.add("After MergeSort:  " + Arrays.toString(numbers));

        writeStepsToFile(outputFile);
        System.out.println("Merge sort steps written to " + outputFile);
    }

    // Read specified rows from the CSV file
    public static int[] readCSVRange(String filename, int start, int end) {
        List<Integer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                if (row < start) continue;
                if (row > end) break;
                String[] parts = line.split(",", 2);
                list.add(Integer.parseInt(parts[0])); // only use the integer field
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    public static void mergeSort(int[] S) {
        mergeSort(S, 0, S.length - 1);
    }

    public static void mergeSort(int[] S, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(S, left, mid);
            mergeSort(S, mid + 1, right);
            merge(S, left, mid, right);
        }
    }

    public static void merge(int S[], int left, int mid, int right) {
        LinkedList<Integer> L = new LinkedList<>();
        for (int i = 0; i < mid - left + 1; i++)
            L.add(S[left + i]);

        LinkedList<Integer> R = new LinkedList<>();
        for (int j = 0; j < right - mid; j++)
            R.add(S[mid + 1 + j]);

        int i = left;

        while (!L.isEmpty() && !R.isEmpty()) {
            if (L.peek() <= R.peek()) {
                S[i++] = L.poll();
            } else {
                S[i++] = R.poll();
            }
        }

        while (!L.isEmpty()) {
            S[i++] = L.poll();
        }

        while (!R.isEmpty()) {
            S[i++] = R.poll();
        }

        // Log this merge step
        logSteps.add("Merged [" + left + " to " + right + "]: " + Arrays.toString(Arrays.copyOfRange(S, left, right + 1)));
    }

    public static void writeStepsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : logSteps) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}