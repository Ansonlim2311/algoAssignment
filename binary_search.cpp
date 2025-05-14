#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <stdexcept>
#include <chrono>

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

void bubbleSort(vector<RowData>& list) {
    int n = list.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n- i - 1; j++) {
            if (list.at(j).number > list.at(j + 1).number) {
                RowData temp = list.at(j);
                list.at(j) = list.at(j + 1);
                list.at(j + 1) = temp;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            list.at(i).index = i + 1;
        }
    }
}

void binarySearch(vector<RowData> list, int targetValue) {
    int leftIndex = 0;
    int rightIndex = list.size() - 1;
    while (leftIndex <= rightIndex) {
        int midIndex = (leftIndex + rightIndex) / 2;
        RowData midValue = list[midIndex];

        logSteps.push_back(to_string(midValue.index) + ":" + to_string(midValue.number) + "/" + midValue.text);

        if (midValue.number == targetValue) {
            return;
        }
        else if (midValue.number < targetValue) {
            leftIndex = midIndex + 1;
        }
        else if (midValue.number > targetValue) {
            rightIndex = midIndex - 1;
        }
    }
}

void writeStepsToFile(const string& filename, chrono::nanoseconds bestTime, chrono::nanoseconds worstTime) {
    ofstream file(filename);
    if (!file.is_open()) {
        throw runtime_error("Error writing to file: " + filename);
    }

    file << "Best Time: " << bestTime.count() << " ns" << endl;
    file << "Worst Time: " << worstTime.count() << " ns" << endl;

    file.close();
}

int main() {
    string filename;
    int target, n, midIndex;

    cout << "Enter dataset filename: ";
    cin >> filename;

    vector<RowData> list = readCSV(filename);
    if (list.empty()) {
        throw runtime_error("Error: No data found in the file.");
    }

    string outputFile = "binary_search_step_" + to_string(list.size()) + ".txt";

    n = list.size();
    bubbleSort(list);

    
    target = list[n/2].number;
    auto start = chrono::high_resolution_clock::now();
    binarySearch(list, target);
    auto end = chrono::high_resolution_clock::now();
    auto bestTime = chrono::duration_cast<chrono::nanoseconds>(end - start);    

    target = list[n - 1].number + 1;
    start = chrono::high_resolution_clock::now();
    binarySearch(list, target);
    end = chrono::high_resolution_clock::now();
    auto worstTime = chrono::duration_cast<chrono::nanoseconds>(end - start);


    writeStepsToFile(outputFile, bestTime, worstTime);

    cout << "Binary search written to " << outputFile << endl;
    return 0;
}