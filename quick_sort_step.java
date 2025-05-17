import java.io.*; //reading&writing java files
import java.util.*; //provides arrayList, Scanner....

public class quick_sort_step{
    //logSteps = list, it was needed to store strings
    static List<String> logSteps = new ArrayList<>(); 

    public static class RowData{
        int number;
        String text;

        RowData(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        String log;
        int left, right;

        //Scanner to read input from users
        Scanner scanner = new Scanner(System.in);  

        //User input
        System.out.print("Enter dataset filename: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter start row: ");
        int startRow = scanner.nextInt();

        System.out.print("Enter end row: ");
        int endRow = scanner.nextInt();
        
        //output filename
        String outputFile = "quick_sort_step_" + startRow + "_" + endRow + ".txt";

        //calls a helper method to read number range from file 
        RowData[] numbers = readCSVRange(inputFile, startRow, endRow);
        if (numbers == null) { //if reading fails, return 
            scanner.close();
            return;
        }

        log = "[";
        for (int i = 0; i < numbers.length; i++) {
            log = log + numbers[i].number + "/" + numbers[i].text;
            if (i != numbers.length - 1) {
                log = log + ", ";
            }
        }
        log = log + "]";
        logSteps.add(log);

        left = 0;
        right = numbers.length - 1;

        quickSort(numbers,left,right);

        writeStepsToFile(outputFile);
        System.out.println("Quick sort steps written to " + outputFile);

        scanner.close();
    }

    //read lines from CSV and collect integers
    public static RowData[] readCSVRange(String filename, int start, int end) { //read range 
        List<RowData> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            for (int index = 1; (line = br.readLine()) != null; index++) {
                if (index < start) {
                    continue;
                }
                if (index > end) { 
                    break;
                }
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

  public static void quickSort(RowData[] S, int left, int right) {
        if (left < right) {
            int pi = partition(S, left, right); // Get pivot index after partitioning
            String log = "pi=" + pi + " [";
            for (int i = 0; i < S.length; i++) {
                log = log + S[i].number + "/" + S[i].text;
                if (i != S.length - 1) {
                    log = log + ", ";
                }
            }
            log = log + "]";
            logSteps.add(log);
            quickSort(S, left, pi - 1);         // Sort left part
            quickSort(S, pi + 1, right);        // Sort right part
        }
    }

    // Partition method with 3-way partitioning
    public static int partition(RowData[] S, int left, int right) {
        List<RowData> L = new ArrayList<>();
        List<RowData> E = new ArrayList<>();
        List<RowData> G = new ArrayList<>();

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

        // Reconstruct the list from L, E, G back into S
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
        return pivotIndex;  // Return the index of the pivot
    }

    public static void writeStepsToFile(String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < logSteps.size(); i++) {
                bw.write(logSteps.get(i));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }
}