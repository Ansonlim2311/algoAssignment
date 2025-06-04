#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <stdexcept>
#include <list>

using namespace std;

vector<string> logSteps;

struct RowData {
    int number;
    string text;

    RowData(int number, string text) {
        this->number = number;
        this->text = text;
    }
};

vector<RowData> readCSVRange(string& filename, int start, int end) {
    vector<RowData> numbers;
    int number;
    string text;
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
        string parts[] = {line.substr(0, line.find(',')), line.substr(line.find(',') + 1)};
        // if (parts[0].empty() || parts[1].empty()) {
        //     throw runtime_error("Error parsing line: " + line);
        // }
        // else {
        number = stoi(parts[0]);
        text = parts[1];
        numbers.push_back(RowData(number, text));
        // }
    }
    file.close();
    return numbers;
}

void merge(vector<RowData>& S, int left, int mid, int right) {
    list<RowData> L, R;
    for (int i = left; i <= mid; i++) {
        L.push_back(S[i]);
    }
    for (int i = mid + 1; i <= right; i++) {
        R.push_back(S[i]);
    }

    int k = left;
    while (!L.empty() && !R.empty()) {
        if (L.front().number <= R.front().number) {
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

    string log = "[";
    for (int i = 0; i < S.size(); i++) {
        log = log + to_string(S[i].number) + "/" + S[i].text;
        if (i < S.size() - 1) {
            log += ", ";
        }
    }
    log = log + "]";
    logSteps.push_back(log);
}

void mergeSort(vector<RowData>& S, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(S, left, mid);
        mergeSort(S, mid + 1, right);
        merge(S, left, mid, right);
    }
}

void writeStepsToFile(string& filename) {
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

    vector<RowData> numbers = readCSVRange(inputFile, startRow, endRow);
    if (numbers.empty()) {
        throw runtime_error("Error: The dataset is empty or could not be read.");
    }

    log = "[";
    for (int i = 0; i < numbers.size(); i++) {
        log = log + to_string(numbers[i].number) + "/" + numbers[i].text;
        if (i < numbers.size() - 1) {
            log += ", ";
        }
    }
    log = log + "]";
    logSteps.push_back(log);

    mergeSort(numbers, 0, numbers.size() - 1);

    writeStepsToFile(outputFile);

    cout << "Merge sort steps written to " << outputFile << endl;
    return 0;
}