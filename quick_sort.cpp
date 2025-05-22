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

// Partition function 
int partition(vector<RowData>& S, int left, int right) {
    vector<RowData> L,E,G;

    int pivot = S[right].number;  // last element as pivot

    for (int i = left; i <= right; i++) {
        if (S[i].number < pivot) {
            L.push_back(S[i]);
        } else if (S[i].number == pivot) {
            E.push_back(S[i]);
        } else {
            G.push_back(S[i]);
        }
    }

    // Rebuild the vector S from L, E, G
    int index = left;
    for (int i = 0; i < (int)L.size(); i++) {
        S[index++] = L[i];
    }
    int pivotIndex = index;  // pivot index 
    for (int i = 0; i < (int)E.size(); i++) {
        S[index++] = E[i];
    }
    for (int i = 0; i < (int)G.size(); i++) {
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

// Read CSV file into vector<RowData>
vector<RowData> readCSV(const string& filename) {
    vector<RowData> data;
    ifstream infile(filename);
    if (!infile.is_open()) {
        throw runtime_error("Error reading file: " + filename);
    }
    string line;
    while (getline(infile, line)) {
        string parts[] = {line.substr(0, line.find(',')), line.substr(line.find(',') + 1)};
        if (parts[0].empty() || parts[1].empty()) {
            throw runtime_error("Error parsing line: " + line);
        }
        else {
            int number = stoi(parts[0]);
            string text = parts[1];
            data.push_back(RowData(number, text));
        }
    }
    infile.close();
    return data;
}

// Write sorted data to CSV
void writeCSV(const string& filename, const vector<RowData>& data) {
    ofstream outFile(filename);
    if (!outFile.is_open()) {
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

    duration<double, milli> duration = end - start;
    cout << "Running time: " << duration.count() << " ms" << endl;

    string outputFile = "quick_sort_" + to_string(data.size()) + ".csv";
    writeCSV(outputFile, data);

    cout << "Sorted data saved to " << outputFile << endl;

    return 0;
}
