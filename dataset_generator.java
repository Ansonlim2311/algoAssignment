import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class dataset_generator {
    static final int maxValue = 1_000_000_000;
    // static final int minStringLen = 5;
    // static final int maxStringLen = 10;
    // int randomLength = rand.nextInt(9) + 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements to generate: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        // System.out.print("Enter the filename to save the dataset (e.g. dataset_n.csv): ");
        // String filename = scanner.nextLine();
        String filename = "dataset_" + size + ".csv";

        generateDataset(filename, size);
    }

    public static void generateDataset(String filename, int size) {
        Random rand = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>(size);
        List<String> data = new ArrayList<>(size);

        System.out.println("Generating data...");
        while (uniqueNumbers.size() < size) {
            int num = rand.nextInt(maxValue) + 1;
            if (uniqueNumbers.add(num)) {
                String randomString = generateRandomString(rand);
                data.add(num + "," + randomString);
            }
        }

        System.out.println("Shuffling data...");
        Collections.shuffle(data);

        System.out.println("Writing to file...");
        try (FileWriter writer = new FileWriter(filename)) {
            for (String line : data) {
                writer.write(line + "\n");
            }
            System.out.println("Dataset written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String generateRandomString(Random rand) {
        int length = rand.nextInt(9) + 1;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }
}