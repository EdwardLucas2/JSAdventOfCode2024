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

The complexity of traveral is O(n), where n is the sum of left and right list length. Total complexity O(nlogn)
