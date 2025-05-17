#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <list>
#include <string>
#include <stdexcept>


using namespace std;

struct RowData {
    int number;
    string text;

    RowData(int n, const string& t) : number(n), text(t) {}
};

vector<string> logSteps;

vector<RowData> readCSVRange(const string& filename) {
    vector<RowData> list;
    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Error reading file." << endl;
        return {};
    }

    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        string numStr, text;
        if (getline(ss, numStr, ',') && getline(ss, text)) {
            try {
                int number = stoi(numStr);
                list.emplace_back(number, text);
            } catch (...) {
                cerr << "Invalid line format: " << line << endl;
            }
        }
    }

    file.close();
    return list;
}

void writeStepsToFile(const string& filename) {
    ofstream out(filename);
    if (!out.is_open()) {
        cerr << "Error writing to file." << endl;
        return;
    }

    if (!logSteps.empty()) {
        out << logSteps.back(); // Write only the final sorted step
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

    // Log current state
    string log;
    for (const auto& row : S) {
        log += to_string(row.number) + "," + row.text + "\n";
    }
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

int main() {
    string inputFile;
    cout << "Enter dataset filename: ";
    getline(cin, inputFile);

    vector<RowData> data = readCSVRange(inputFile);
    if (data.empty()) {
        cerr << "Error: Unable to read dataset." << endl;
        return 1;
    }

    string outputFile = "merge_sort " + to_string(data.size()) + ".txt";

    mergeSort(data, 0, data.size() - 1);
    writeStepsToFile(outputFile);

    cout << "Merge sort steps written to " << outputFile << endl;

    return 0;
}
