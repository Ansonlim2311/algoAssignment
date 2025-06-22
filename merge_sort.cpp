#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <string>
#include <stdexcept>
#include <chrono>

using namespace std;
using namespace std::chrono;

struct RowData {
    int number;
    string text;

    RowData(int number, string text) {
        this->number = number;
        this->text = text;
    }
};

vector<RowData> readCSVRange(string& filename) {
    vector<RowData> list;
    int number, comma;
    string text, line;
    ifstream file(filename);
    if (!file.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }

    while (getline(file, line)) {
        comma = line.find(',');
        if (comma != -1) {
            number = stoi(line.substr(0, comma));
            text = line.substr(comma + 1);
            list.push_back(RowData(number, text));
        }
    }
    file.close();
    return list;
}

void writeStepsToFile(string& filename, vector<RowData>& data) {
    ofstream out(filename);
    if (!out.is_open()) {
        throw runtime_error("Error writing to file: " + filename);
    }

    for (int i = 0; i < data.size(); ++i) {
        out << data[i].number << "," << data[i].text << "\n";
    }
    out.close();
}

void merge(vector<RowData>& S, int left, int mid, int right) {
    list<RowData> L, R;

    for (int i = 0; i < mid - left + 1; ++i)
        L.push_back(S[left + i]);
    for (int j = 0; j < right - mid; ++j)
        R.push_back(S[mid + 1 + j]);

    int k = left;
    while (!L.empty() && !R.empty()) {
        if (L.front().number <= R.front().number) {
            S[k++] = L.front();
            L.pop_front();
        } else {
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
}

void mergeSort(vector<RowData>& S, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(S, left, mid);
        mergeSort(S, mid + 1, right);
        merge(S, left, mid, right);
    }
}

int main() {
    string inputFile;
    cout << "Enter dataset filename: ";
    getline(cin, inputFile);

    vector<RowData> data = readCSVRange(inputFile);
    if (data.empty()) {
        throw runtime_error("Error: The dataset is empty or could not be read.");
    }

    string outputFile = "merge_sort_" + to_string(data.size()) + ".csv";

    auto start = high_resolution_clock::now();
    mergeSort(data, 0, data.size() - 1);
    auto end = high_resolution_clock::now();

    auto duration = duration_cast<seconds>(end - start);
    writeStepsToFile(outputFile, data);

    cout << "Merge sort steps written to " << outputFile << endl;
    cout << "Running time: " << duration.count() << " second" << endl;

    return 0;
}