import re
from Game import Game

def main():
    #Parse the input file
    games = ParseInputFile("input.txt")

    cost: int = 0

    #Iterate through each game
    for game in games:
        #Print the game data
        print(str(game) + "\n")
        #Get the lowest cost
        lowest_cost, a_num, b_num = game.getLowestCost(3, 1)

        if a_num > -1:
            cost += lowest_cost
        #Print the lowest cost, a number, and b number
        print("Lowest Cost: " + str(lowest_cost) + "\n" + "Num A Presses: " + str(a_num) + ", Num B Presses: " + str(b_num) + "\n")

    # Print the total cost
    print("Total Cost: " + str(cost) + "\n")

def ParseInputFile(filename: str) -> list[Game]:
    """
    Parse the input file and return a list of tuples.
    Each tuple contains 6 integers representing the game data.
    """
    games = []
    with open(filename, "r") as f:
        #Split the file by empty lines
        data = f.read().split("\n\n")
        #Iterate through each game string
        for game in data:
            #Parse the game string into a tuple
            try:
                game_data = ParseGame(game)
                games.append(game_data)
            except ValueError as e:
                print(f"Error parsing game data: {e}")
    return games

def ParseGame(data: str) -> Game:
    """
    Parse the game data from a string into a tuple of integers.
    """
    nums = re.findall("\\d+", data)
    # Verify there are 6 numbers
    if len(nums) != 6:
        raise ValueError("Invalid game data")
    # Convert to integers
    nums = [int(num) for num in nums]
    #Return the numbers as a tuple
    return Game(nums[0], nums[1], nums[2], nums[3], nums[4], nums[5])

if __name__ == "__main__":
    main()