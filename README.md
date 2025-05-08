# Jane Street Advent Of Code 2024 Solutions
When applying for a job at Jane Street I came across this challenge when researching the position, I thought they might be fun alternative to the LeetCode grind.
I started this in May 2025, long after the challenge was over, but I didn't look at any other peoples solutions beforehand. Enjoy!

## Problem 1
### Part 1
#### Problem:
Given two lists of integers, calculate the 'distance' between them. The distance is calculated by summing the difference between the first-smallest, second-smallest, etc., numbers in each list
#### Solution:
I read the file input into two ArrayLists in memory from disk using a Scanner class, regex, and Integer parsing. I could have not used regex to speed this up, but performace wasn't important here.

First I sorted each list from smallest to largest O(n*log(n)) time complexity (where n is the combined length of the two lists). I assume that both lists are the same length.
I then iterate over each pair of numbers, calculating their difference and summing it to a running total - O(n) complexity.

The 'best-case' solution to this problem is O(n). My solution is O(n) + O(n* logn) = O(n* logn).
I expect my solution isn't optimal, and the sorting of each list is unnececary, but I'm running this on an M2-Mac, so speed isn't a big concern.
### Part 2
#### Problem:
Given the same two lists, calculate instead their 'similarity' score. This is calculated by summing the multiple of the number of times each number in the left list occurs in the right list.
Ie: left list: 1,2,3, right list: 1,1,2 Similarity = (1 * 2)+(2 * 1)+(3 * 0) = 4
#### Solution:
I read the lists from disk in the same way as in part 1, I then sort both lists.

I use a pointer for each list: leftPtr and rightPtr, then for each number in the left list I count the number of occurrences in the right list (by incrementing rightPtr).
I then calculate the similarity score for the left number, and sum it with a running total. I keep track of the last number and it's similarity score so work for identical numbers in the left list dowsn't need to be repeated.

The complexity of traveral is O(n), where n is the sum of left and right list length. Total complexity is O(nlogn)

## Problem 2 – Red-Nosed Reports
### Part 1
#### Problem:
We are given multiple 'reports', each comprising a list of numbers 'levels'. We must determine if a report is 'safe'. For a report to be safe both the following conditions must be met:
- All levels are either strictly increasing or strictly decreasing.
- Each adjacent pair of levels differs by at least 1 and at most 3.
#### Solution:
I parsed the input file line-by-line using a Scanner and a regex to extract integers like in Problem 1. Each report (line in the file) is stored in memory as an ArrayList<Integer>

I wrote a function that determines if a report is safe, it has two stages:
Determine Direction:
I look at the first two values. If the second is greater than the first, I assume the report is ascending; otherwise, descending.
Validate Differences:
I iterate through each number in the list, compare the difference with the previous number, and use this to workout if this is safe, if not, return false (not safe).

This algorithm runs in O(m) time for a single report of length m, and O(n * m) overall for n reports (m is the average report length).
### Part 2
#### Problem:
A new constraint is introduced: the reactor has a "Problem Dampener" that can tolerate a single bad level. A report is considered safe if it becomes safe after removing any one of its levels.

#### Solution:
I'm not pound of this solution, but I wanted to complete this one quickly. I brute-forced it by first checking if the report is safe without a level removed. If not, I try by checking saftey after removing each level in-turn.

Best case complexity is O(n), but the worst and average case complexity is O(n^2), where the saftey check O(n) is run for each level in the list (n, or n/2 in the average case, levels to check saftey for). This assumes that the unnececary array copy I use runs in constant time, but in any case this could be trivially removed by keeping track of the last level removed and re-adding to an exisiting array instead of copying the entire array prior to level removal.

My solution leads to lots of wasted work, it ran easily on my mac, but I would optimise it by
  - removing unnececary array copying from brute-force solution
  - Calculating the validity of adjacent pairs of levels in each direction, and pairs to the 'next next' level (assuming the next level is removed by the dampener). If all pairs are valid in one direction (or more), allowing for 1 dampened level pair. I may implement this proposal later.

## Problem 3 - Mull It Over
### Part 1
#### Problem
We are given a string of 'corrupted memory' that contains the instruction 'mul(X,Y)'. We are tasked by finding the result of each valid 'mul' instruction is the memory, and summing all their results.
#### Solution
This one was very easy, I used a Regular Expression 'mul\([0-9]{1,3},[0-9]{1,3}\)' to find all the valid instructions, and another simple function to take the string of a valid instruction and return the result.
### Part 2
#### Problem
This is where it got a bit trickier, two new instructions 'do()' and 'don't()'. Instructions where either 'valid' or 'invalid', depending on if the most recent, previous, instruction is a 'do()' or 'don't()'.
We must calculate the sum of only the 'valid' mul instruction's results
#### Solution
I used a regex to get the indexes (in the corrupted memory string) of all the do and don't instructions. I stored the indexes of the do and don't instructions in two separate, sorted, lists.
I iterate through both lists using two pointers (one for each list) from smallest index to largest. E.g. for do list: {1,2,3,5}, don't list: {4,6} the index visit order would be {1,2,3,4,5,6}
As we iterate through the lists we keep track of what the last instruction is, and when the end of a 'valid' section is reached, I send the 'valid' substring to be processed.

# Problem 4 - Ceres Search
### Part 1
#### Problem
We are given a grid of letters, and need to find the number of occurrences of 'XMAS'. XMAS can be written top-down, left-right, right-left, diagonally, etc. It is essentially a wordsearch.
#### Solution
I read the input into a 2d character array, then iterate over all the rows and columns in the array. If the character is an 'X', I check each possible direction that an XMAS could occur. Ie: each of the three characters to the right, below, to the left, and diagonally, of the X, seeing if they are {M, A, S}.
I count up the number of valid occurrences, and add it to the running total. This runs is O(n) time, where n is the number of characters in the input string. This is because I compare each character in the grid with an 'X', and checking for 'XMAS' at each X is a constant time operation.
### Part 2
#### Problem
Instead of searching for 'XMAS' in the word search we need to search for:\
M.S\
.A.\
M.S\
(Or other orientations, sorry for the poor formatting)
#### Solution
I use a very similar technique to Part 1, I iterate over each character in the grid, excluding the edges, and compare it with 'A', I then check each character to the top-left, top-right, bottom-left, and bottom-right, to see if there is a valid X'd MAS.
It's a little messy with some nested if statements, but it runs in linear (O(N)) time as well.