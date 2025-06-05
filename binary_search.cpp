#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <random>
#include <chrono>
#include <stdexcept>

using namespace std;
using namespace std::chrono;

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

vector<RowData> readCSV(string& filename) {
    vector<RowData> list;
    ifstream file(filename);
    
    if (!file.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }

    string line;
    for (int index = 1; getline(file, line); index++) {
        string parts[] = {line.substr(0, line.find(',')), line.substr(line.find(',') + 1)}; 
        // if (parts[0].empty() || parts[1].empty()) {
        //     throw runtime_error("Error parsing line: " + line);

        // }
        // else {
        int number = stoi(parts[0]);
        string text = parts[1];
        list.push_back(RowData(number, text, index));
        // }
    }
    file.close();
    return list;
}

void binarySearch(vector<RowData>& list, int targetValue) {
    int leftIndex = 0;
    int rightIndex = list.size() - 1;

    while (leftIndex <= rightIndex) {
        int midIndex = (leftIndex + rightIndex) / 2;
        RowData midValue = list[midIndex];
        if (midValue.number == targetValue) {
            return;
        }
        else if (midValue.number < targetValue) {
            leftIndex = midIndex + 1;
        }
        else {
            rightIndex = midIndex - 1;
        }
    }   
}

void writeStepsToFile(string& filename, double bestTime, double worstTime, double avgTime) {
    ofstream file(filename);
    if (!file.is_open()) {
        throw runtime_error("Error writing to file: " + filename);
    }

    file << "Best Time: " << bestTime << " ns" << endl;
    file << "Average Time: " << avgTime << " ns" << endl;
    file << "Worst Time: " << worstTime << " ns" << endl;

    file.close();
}

int main() {
    string filename;
    auto start = high_resolution_clock::now();
    auto end = high_resolution_clock::now();
    auto duration = duration_cast<nanoseconds>(end - start);
    srand(time(0));
    cout << "Enter dataset filename: ";
    cin >> filename;

    vector<RowData> list = readCSV(filename);
    if (list.empty()) {
        throw runtime_error("Error: No data found in the file.");
    }

    int n = list.size();
    int bestTarget = list[(n / 2) - 1].number;
    int worstTarget = list[0].number - 1;
    int target = list[rand() % n].number;

    double bestTime, worstTime, avgTime;

    string outputFile = "binary_search_step_" + to_string(n) + ".txt";

    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) {
        binarySearch(list, bestTarget);
    }
    end = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(end - start);
    bestTime = duration.count() / n;

    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) {
        binarySearch(list, worstTarget);
    }
    end = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(end - start);
    worstTime = duration.count() / n;

    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) {
        binarySearch(list, target);
        avgTime += duration_cast<nanoseconds>(end - start).count();
    }
    end = high_resolution_clock::now();
    duration = duration_cast<nanoseconds>(end - start);
    avgTime = duration.count() / n;

    writeStepsToFile(outputFile, bestTime, worstTime, avgTime);

    cout << "Binary search steps written to " << outputFile << endl;
    return 0;
}
