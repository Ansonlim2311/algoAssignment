#include <iostream>
#include <fstream>
#include <unordered_set>
#include <vector>
#include <string>
#include <random>
#include <algorithm>
#include <ctime>

using namespace std;

const int maxValue = 1000000000;

string generateRandomString(mt19937& rng) {
    uniform_int_distribution<int> lengthDist(4, 12);
    int length = lengthDist(rng);

    string str;
    for (int i = 0; i < length; ++i) {
        char c = 'a' + rng() % 26;
        str += c;
    }
    return str;
}

void generateDataset(const string& filename, int n) {
    mt19937 rng(static_cast<unsigned int>(time(nullptr)));
    unordered_set<int> uniqueNumbers;
    vector<string> data;

    uniform_int_distribution<int> numDist(1, maxValue);

    while (uniqueNumbers.size() < static_cast<size_t>(n)) {
        int num = numDist(rng);
        if (uniqueNumbers.insert(num).second) {
            string randomString = generateRandomString(rng);
            data.push_back(to_string(num) + "," + randomString);
        }
    }

    // Shuffle the data
    shuffle(data.begin(), data.end(), rng);

    // Write to CSV
    ofstream out(filename);
    if (!out.is_open()) {
        cerr << "Error writing to file: " << filename << endl;
        return;
    }

    for (const string& line : data) {
        out << line << "\n";
    }

    out.close();
    cout << "Dataset written to " << filename << endl;
}

int main() {
    int n;
    cout << "Enter the number of elements to generate: ";
    cin >> n;

    string filename = "dataset_" + to_string(n) + ".csv";
    generateDataset(filename, n);

    return 0;
}

// How To Run
// g++ dataset_generator.cpp -o dataset_generator
// ./dataset_generator