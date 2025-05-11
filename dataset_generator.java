import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class dataset_generator {
    static final int maxValue = 1000000000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of elements to generate: ");
        int n = scanner.nextInt();
        scanner.nextLine();

        String filename = "dataset_" + n + ".csv";

        generateDataset(filename, n);

        scanner.close();
    }

    public static void generateDataset(String filename, int n) {
        Random rand = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>(n);
        List<String> data = new ArrayList<>(n);

        while (uniqueNumbers.size() < n) {
            int num = rand.nextInt(maxValue) + 1;
            if (uniqueNumbers.add(num)) {
                String randomString = generateRandomString(rand);
                data.add(num + "," + randomString);
            }
        }

        Collections.shuffle(data);

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