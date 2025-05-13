#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <stdexcept>
#include <list>

using namespace std;

vector<string> logSteps;

vector<int> readCSVRange(const string& filename, int start, int end) {
    vector<int> numbers;
    ifstream file(filename);
    
    if (!file.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }

    string line;
    for (int row = 1; getline(file, line); row++) {
        if (row < start) {
            continue;
        }
        if (row > end) { 
            break;
        }

        numbers.push_back(stoi(line.substr(0, line.find(','))));
    }
    file.close();
    return numbers;
}

void merge(vector<int>& S, int left, int mid, int right) {
    list<int> L, R;
    for (int i = left; i <= mid; i++) {
        L.push_back(S[i]);
    }
    for (int i = mid + 1; i <= right; i++) {
        R.push_back(S[i]);
    }

    int k = left;
    while (!L.empty() && !R.empty()) {
        if (L.front() <= R.front()) {
            S[k++] = L.front();
            L.pop_front();
        }
        else {
            S[k++] = R.front();
            R.pop_front();
        }
    }

    while (!L.empty()) {
        S[k++] = L.front();
        L.pop_front();
    }
    while (!R.empty()) {
        S[k++] = R.front();
        R.pop_front();
    }

    string log = "Merged [" + to_string(left) + " to " + to_string(right) + "]: ";
    for (int i = left; i <= right; i++) {
        log = log + to_string(S[i]) + " ";
    }
    logSteps.push_back(log);
}

void mergeSort(vector<int>& S, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(S, left, mid);
        mergeSort(S, mid + 1, right);
        merge(S, left, mid, right);
    }
}

void writeStepsToFile(const string& filename) {
    ofstream file(filename);
    if (!file.is_open()) {
        throw runtime_error("Error writing to file: " + filename);
    }

    for (int i = 0; i < logSteps.size(); i++) {
        file << logSteps[i] << + " " << endl;
    }
    file.close();
}

int main() {
    string inputFile, log;
    int startRow, endRow;

    cout << "Enter dataset filename: ";
    cin >> inputFile;

    cout << "Enter start row: ";
    cin >> startRow;

    cout << "Enter end row: ";
    cin >> endRow;

    string outputFile = "merge_sort_step_" + to_string(startRow) + "_" + to_string(endRow) + ".txt";

    vector<int> numbers = readCSVRange(inputFile, startRow, endRow);
    if (numbers.empty()) {
        throw runtime_error("Error: The dataset is empty or could not be read.");
    }

    log = "Before MergeSort: ";
    for (int i = 0; i < numbers.size(); i++) {
        log = log + to_string(numbers[i]) + " ";
    }
    logSteps.push_back(log);

    mergeSort(numbers, 0, numbers.size() - 1);

    log = "After MergeSort: ";
    for (int i = 0; i < numbers.size(); i++) {
        log = log + to_string(numbers[i]) + " ";
    }
    logSteps.push_back(log);

    writeStepsToFile(outputFile);

    cout << "Merge sort steps written to " << outputFile << endl;
    return 0;
}