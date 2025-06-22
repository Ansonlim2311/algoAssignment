#include <iostream>
#include <fstream>
#include <vector>
#include <string>
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

void quicksort(vector<RowData>& S, int left, int right) {
    if (left < right) {
        int pi = partition(S, left, right);
        quicksort(S, left, pi - 1);
        quicksort(S, pi + 1, right);
    }
}

vector<RowData> readCSV(string& filename) {
    vector<RowData> data;
    int number, comma;
    string text, line;
    ifstream infile(filename);
    if (!infile.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }
    
    while (getline(infile, line)) {
        comma = line.find(',');
        if (comma != -1) {
            number = stoi(line.substr(0, comma));
            text = line.substr(comma + 1);
            data.push_back(RowData(number, text));
        }
    }
    infile.close();
    return data;
}

void writeCSV(string& filename, vector<RowData>& data) {
    ofstream outFile(filename);
    if (outFile.is_open() == false) {
        throw runtime_error("Error writing to file: " + filename);
    }
    for (int i = 0; i < data.size(); i++) {
        outFile << data[i].number << "," << data[i].text << "\n";
    }
    outFile.close();
}

int main() {
    cout << "Enter dataset filename: ";
    string inputFile;
    getline(cin, inputFile);

    vector<RowData> data = readCSV(inputFile);
    if (data.empty()) {
        throw runtime_error("Error: The dataset is empty or could not be read.");
    }

    auto start = high_resolution_clock::now();
    quicksort(data, 0, data.size() - 1);
    auto end = high_resolution_clock::now();

    auto duration = duration_cast<seconds>(end - start);

    string outputFile = "quick_sort_" + to_string(data.size()) + ".csv";
    writeCSV(outputFile, data);

    cout << "Sorted data saved to " << outputFile << endl;
    cout << "Running time: " << duration.count() << " second" << endl;

    return 0;
}