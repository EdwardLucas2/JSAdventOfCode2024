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
