#include <iostream>
#include <fstream>
#include <sstream>
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
    int pi = index;  // pivot index 
    for (int i = 0; i < (int)E.size(); i++) {
        S[index++] = E[i];
    }
    for (int i = 0; i < (int)G.size(); i++) {
        S[index++] = G[i];
    }
    return pi;
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
    if (!infile) {
        cerr << "Error opening file: " << filename << endl;
        return data;
    }
    string line;
    while (getline(infile, line)) {
        stringstream ss(line);
        string numStr, text;
        if (!getline(ss, numStr, ',')) continue;
        if (!getline(ss, text)) text = "";
        int number = stoi(numStr);
        data.push_back(RowData(number, text));
    }
    infile.close();
    return data;
}

// Write sorted data to CSV
void writeCSV(const string& filename, const vector<RowData>& data) {
    ofstream outfile(filename);
    if (!outfile) {
        cerr << "Error opening file for writing: " << filename << endl;
        return;
    }
    for (int i = 0; i < (int)data.size(); i++) {
        outfile << data[i].number << "," << data[i].text << "\n";
    }
    outfile.close();
}

int main() {
    cout << "Enter dataset filename: ";
    string inputFile;
    getline(cin, inputFile);

    vector<RowData> data = readCSV(inputFile);
    if (data.empty()) {
        cerr << "No data to sort or file error." << endl;
        return 1;
    }

    auto start = high_resolution_clock::now();
    quicksort(data, 0, (int)data.size() - 1);
    auto end = high_resolution_clock::now();

    duration<double, milli> duration = end - start;
    cout << "Running time: " << duration.count() << " ms" << endl;

    string outputFile = "quick_sort_" + to_string(data.size()) + ".csv";
    writeCSV(outputFile, data);

    cout << "Sorted data saved to " << outputFile << endl;

    return 0;
}
