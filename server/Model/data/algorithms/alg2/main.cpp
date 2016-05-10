#include <stdio.h>
#include <iostream>
#include <fstream>

int main(int argc, char* argv[]) {

	if (argc < 2) {
		printf("Error\n");
		return 1;
	}

	char* path = argv[1];

	std::ifstream input(path);
	std::string line;
	if (input.is_open())
	{
		while ( std::getline (input,line) )
		{
			std::cout << line << '\n';
		}
		input.close();
		return 0;
	}

	printf("Couldn't open file\n");
	return 1;
}