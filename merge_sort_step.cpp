#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <string>
#include <algorithm>

using namespace std;

// Function to log the sorting steps
vector<string> logSteps;

// Function to read the CSV file and get the specified rows
vector<int> readCSVRange(const string& filename, int start, int end) {
    vector<int> numbers;
    ifstream file(filename);
    
    if (!file.is_open()) {
        cerr << "Error reading file: " << filename << endl;
        return numbers;
    }

    string line;
    int row = 0;
    while (getline(file, line)) {
        row++;
        if (row < start) continue;
        if (row > end) break;

        stringstream ss(line);
        string numberStr;
        getline(ss, numberStr, ',');
        int number = stoi(numberStr);
        numbers.push_back(number);
    }
    file.close();
    return numbers;
}

// Function to merge two sorted halves
void merge(vector<int>& arr, int left, int mid, int right) {
    vector<int> L(arr.begin() + left, arr.begin() + mid + 1);
    vector<int> R(arr.begin() + mid + 1, arr.begin() + right + 1);

    int i = left, j = 0, k = 0;
    while (j < L.size() && k < R.size()) {
        if (L[j] <= R[k]) {
            arr[i++] = L[j++];
        } else {
            arr[i++] = R[k++];
        }
    }

    while (j < L.size()) {
        arr[i++] = L[j++];
    }

    while (k < R.size()) {
        arr[i++] = R[k++];
    }

    // Log this merge step
    stringstream ss;
    ss << "Merged [" << left << " to " << right << "]: ";
    for (int i = left; i <= right; i++) {
        ss << arr[i] << " ";
    }
    logSteps.push_back(ss.str());
}

// Merge sort function
void mergeSort(vector<int>& arr, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }
}

// Function to write the steps to a file
void writeStepsToFile(const string& filename) {
    ofstream file(filename);
    if (!file.is_open()) {
        cerr << "Error writing to file: " << filename << endl;
        return;
    }

    for (const string& line : logSteps) {
        file << line << endl;
    }
    file.close();
}

int main() {
    // Taking inputs from the user
    string inputFile;
    cout << "Enter filename: ";
    cin >> inputFile;

    int startRow, endRow;
    cout << "Enter start row (e.g., 1): ";
    cin >> startRow;

    cout << "Enter end row (e.g., 10): ";
    cin >> endRow;

    // Construct output file name
    string outputFile = "merge_sort_step_" + to_string(startRow) + "_" + to_string(endRow) + ".txt";

    // Read the data from the CSV file
    vector<int> numbers = readCSVRange(inputFile, startRow, endRow);
    if (numbers.empty()) {
        return 1;
    }

    logSteps.push_back("Before MergeSort: ");
    for (int num : numbers) {
        logSteps.back() += to_string(num) + " ";
    }

    // Perform merge sort
    mergeSort(numbers, 0, numbers.size() - 1);

    logSteps.push_back("After MergeSort: ");
    for (int num : numbers) {
        logSteps.back() += to_string(num) + " ";
    }

    // Write the steps to a file
    writeStepsToFile(outputFile);

    cout << "Merge sort steps written to " << outputFile << endl;
    return 0;
}