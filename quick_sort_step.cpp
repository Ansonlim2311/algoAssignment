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
    int number, comma;
    string text, line;
    ifstream file(filename);
    
    if (!file.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }

    for (int row = 1; getline(file, line); row++) {
        if (row < start) {
            continue;
        }
        if (row > end) { 
            break;
        }
        comma = line.find(',');
        if (comma != -1) {
            number = stoi(line.substr(0, comma));
            text = line.substr(comma + 1);
            numbers.push_back(RowData(number, text));
        }
    }
    file.close();
    return numbers;
}

int partition(vector<RowData>& S, int left, int right) {
    vector<RowData> L,E,G;

    int pivot = S[right].number;

    for (int i = left; i <= right; i++) {
        int e = S[i].number ;
        if (e < pivot) {
            L.push_back(S[i]);
        } else if (e == pivot) {
            E.push_back(S[i]);
        } else {
            G.push_back(S[i]);
        }
    }

    int index = left;
    for (int i = 0; i < L.size(); i++) {
        S[index++] = L[i];
    }
    int pivotIndex = index;
    for (int i = 0; i < E.size(); i++) {
        S[index++] = E[i];
    }
    for (int i = 0; i < G.size(); i++) {
        S[index++] = G[i];
    }
    return pivotIndex;
}

void quickSort(vector<RowData>& S, int left, int right) {
    if (left < right) {
        int pi = partition(S, left, right);
        string log = "pi=" + to_string(pi) + " [";
        for (int i = 0; i < S.size(); i++) {
            log = log  + to_string(S[i].number) + "/" + S[i].text;
            if (i != S.size() - 1) {
                log = log + ", ";
            }
        }
        log = log + "]";
        logSteps.push_back(log);
        quickSort(S, left, pi - 1);  
        quickSort(S, pi + 1, right);       
    }
}

void writeStepsToFile(string& filename) {
    ofstream file(filename);
    if (!file.is_open()) {
        throw runtime_error("Error writing to file: " + filename);
    }

    for (int i = 0; i < logSteps.size(); i++) {
        file << logSteps[i] << endl;
    }
    file.close();
}

int main() {
    string inputFile, log;
    int startRow, endRow, left,right;

    cout << "Enter dataset filename: ";
    cin >> inputFile;

    cout << "Enter start row: ";
    cin >> startRow;

    cout << "Enter end row: ";
    cin >> endRow;

    string outputFile = "quick_sort_step_" + to_string(startRow) + "_" + to_string(endRow) + ".txt";

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

    left = 0;
    right = numbers.size() - 1;
    quickSort(numbers,left,right);

    writeStepsToFile(outputFile);

    cout << "Quick sort steps written to " << outputFile << endl;
    return 0;
}