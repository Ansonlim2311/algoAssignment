#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <stdexcept>

using namespace std;

vector<string> logSteps;

struct RowData {
    int number;
    string text;
    int index;

    RowData(int number, string text, int index) {
        this->number = number;
        this->text = text;
        this->index = index;
    }
};

vector<RowData> readCSV(const string& filename) {
    vector<RowData> list;
    ifstream file(filename);
    
    if (!file.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }

    string line;
    for (int index = 1; getline(file, line); index++) {
        string parts[] = {line.substr(0, line.find(',')), line.substr(line.find(',') + 1)}; 
        if (parts[0].empty() || parts[1].empty()) {
            throw runtime_error("Error parsing line: " + line);

        }
        else {
            int number = stoi(parts[0]);
            string text = parts[1];
            list.push_back(RowData(number, text, index));
        }
    }
    file.close();
    return list;
}

bool binarySearch(vector<RowData> list, int targetValue) {
    int leftIndex = 0;
    int rightIndex = list.size() - 1;
    while (leftIndex <= rightIndex) {
        int midIndex = (leftIndex + rightIndex) / 2;
        RowData midValue = list[midIndex];

        logSteps.push_back(to_string(midValue.index) + ":" + to_string(midValue.number) + "/" + midValue.text);

        if (midValue.number == targetValue) {
            return true;
        }
        else if (midValue.number < targetValue) {
            leftIndex = midIndex + 1;
        }
        else if (midValue.number > targetValue) {
            rightIndex = midIndex - 1;
        }
    }
    return false;
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
    string filename;
    int targetVaule;

    cout << "Enter dataset filename: ";
    cin >> filename;

    cout << "Enter Target Value: ";
    cin >> targetVaule;

    string outputFile = "binary_search_step_" + to_string(targetVaule) + ".txt";

    vector<RowData> list = readCSV(filename);
    if (list.empty()) {
        throw runtime_error("Error: No data found in the file.");
    }

    bool found = binarySearch(list, targetVaule);
    if (found == false) {
        logSteps.push_back("-1");
    }

    writeStepsToFile(outputFile);

    cout << "Binary search steps written to " << outputFile << endl;
    return 0;
}