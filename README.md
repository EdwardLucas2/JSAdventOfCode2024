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

## Problem 4 - Ceres Search
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

## Problem 5 - Print Queue
### Part 1
#### Problem
We are given a list of 'updates', each update is a list of pages, identified by page number, to print in order.
We are also given a list of rules governing the ordering of valid page printing, in this format 'X|Y', where page X must be printed before page Y.
Given this list of updates, determine which ones are 'valid' i.e. obey all the ordering rules, then calculate the sum middle page number of the valid updates (all the updates have an odd length, so this is trivial)
#### Solution
I used a Scanner and a Regex to parse the file into memory. I stored updates as a list of ArrayList<Integer>, and the rules as a Hashmap<Integer, Hashset<Integer>> (Map< Page to be printed before, Set < Pages to be printed after >>).

How to determine if an update is valid:
Initialise a set<int>: Pages already printed, add the first element in the update to it
Iterate through pages in the update (skipping the 0th index, it doesn't need to be checked as it's before every page), for each page, get the rule for this page, i.e. the get set of pages that this page must be printed before. If this set overlaps (shares an element) with the previously printed page set, this update is invalid.
If we reach the end of the update without discovering a fault, the update is valid. Getting the middle number is trivial: (middle index = size - 1/2)

This runs in time complexity: O(n*r), where n is the number of pages in the update, and r is the average number of rule pages in the update. I think this is the best case time complexity.

Foreach update I determine if it's valid, if so, sum it's middle value to a running total.
Overall time complexity: $$O(n * r * u), r=mean pages/rule in update, u=numupdates, n=meanpagesperupdate$$
### Part 2
#### Problem
In this part we need to re-order the invalid updates to a valid order, then sum the middle numbers of all the re-ordered updates.
#### Solution
I first attempted a method that iterated through the list as in part 1, keeping track of previously printed pages and their indexes, and upon finding an invalid rule, moving the offending page(s) to the position(s) directly after the current page (that has a rule invalidated), then restarting iteration from the beginning.
It was a pain to debug, as it involved modifying the same list I was iterating through, and wasn't efficient, the average time complexity for re-ordering a single update would be O(n * r * i), where n is the number of pages in the update, r is the mean number of rules, and i is the number of broken rules in the list. The worst-case time complexity would be much worse as a page moved to fix one rule could violate another.

In my second attempt I approached it 'rules first', instead of 'page first'. I initialised an empty list to store the ordered update. I then iterated through the pages in the update, then for each page I got its rules and iterated through each page in my re-ordered update list, check if the re-ordered page is contained in the current page's rules. If it was I inserted it just before, otherwise at the end of the list.
This allowed me to build a valid list, rather than trying to re-order elements in an invalid one.

Time Complexity: O(n^2), where n is the number of pages in the update. I think this is the best-case time complexity, (I only have medium confidence in this), as each page needs to be compared with the rules of every other page. Unless calculating the join of two sets can be done in constant time, I think this is as good as it gets.

## Problem 6
### Part 1
#### Problem
We are given a 2d map that represents an area a 'guard' patrols. The map is represented as a 2d matrix, each cell (element in the matrix) either is an obstacle, or is clear. The guard patrols the map deterministically:
The guard moves forward if the cell in front of them is clear (they can face, left, right, up, or down, *not diagonally*), if it's not clear (the cell in front of the guard is an obstacle), they turn 90 degrees right, ie, up, to left, to down, then forwards a space (if it's also not clear, they will keep rotating).
We are given a map showing the location of obstacles, clear floor, and the guards starting position and direction. We need to determine how many different cells the guard will visit i.e. until the guard loops, or moves off the map
#### Solution
I chose the most straight-forward way to solve this: simulation.
I stored the games state using a class (my first use of object-oriented programming in this challenge) with a 2d int array for the map, which stores the tiles (un)visited and obstacles. It stores the guards position and orientation using three ints, and a step counter. I wrote some helper functions to read in a game state from the file, and run an iteration, then ran many iterations until I reached max_steps, a property I defined to avoid having to implement loop detection, or the guard moved off the map.
One neat thing about this implementation was the use of an 2d int array to store visited and unvisited state, 1 represented an unvisited floor cell, 2 a visited one, and 3 an obstacle. This precluded me from needing a separate 'visisted' array, saving space. I didn't implement loop detection as I only needed to simulate one 'map' which didn't have the guard move in a loop, meaning that loop detection wasn't needed at this stage to save unnecessary looping iterations.
### Part 2
#### Problem
This is where the problem became more interesting, and I was glad I stored state in using an object, rather than simply using inline variables in a function.
I needed to determine in how many different locations a new (single) obstacle could be added, where the guard would move in a loop.
#### Solution
While I could try to design a smart strategy to detect loops in a map, maybe by looking at pairs of objects in adjacent columns or rows, I decided I'd rather make my computer sweat a bit, and stick with my previous simulation strategy.
##### Loop detection
I implemented loop detection by storing every previous guard position and location, then doing a look-up at each step to determine if the simulation had looped. Since the 'lookup check' would be done at every iteration it was important that it was quick.
I stored previous positions using a HashMap < Integer, HashMap < Integer, HashSet < Integer >>>, where you would check if a state had been repeated using Map < Guard X Position, Map < Guard Y Position, Set < Orientation >>>. (You would run a 'contains' on the set fetched from the maps).
This would check for a loop in constant time, which I was very happy with (adding a position is also constant (amortised) complexity)
##### Obstacle Placement Calculation
Since only one obstacle can be placed, and the guard in the input map doesn't loop *as is*, we can narrow down the number of possible obstacle placements to those visited by the guard in the initial, unmodified, map. If an obstacle was placed elsewhere the guards path would be unchanged, making a loop impossible.

I iterate over each 'visited' cell, add an obstacle, and run the simulation until either a loop is found, or the simulation ends (guard leaves the map).\
*N.B. the max step counter is no longer necessary due to loop detection - the simulation cannot go on indefinitely anymore, I, however, retained it to make debugging easier*

I then sum the number of simulations where a loop was formed, and return that value. The time complexity is O(n * s), where n is the number of visited nodes in the given map, and s is the mean number of steps in each underlying simulation (with an added obstacle), i.e. how many simulations steps until either a loop is found, or the guard leaves the map

## Problem 7 - Bridge Repair
### Part 1
#### Problem
We are given a list of 'equations', which comprise of a list of elements, and a target value. We need to find the operators that go in-between the elements that make the result equal the target value. Operators ignore precedence rules (BIDMAS), and are evaluated left-to-right.
For example, for an input equation with target value 16, and operators {1,3,4}, the correct two operators would be {+, *} ((1+3)*4=16).\
*N.B. only the operators {+, \*} can be used, and all elements are positive (>0)*

We are given a list of equations, we need to find which ones are 'valid' - where there exists a set of operators where the sum of the equation is equal to it's target value - and sum their target values.
#### Solution
I solved this using a recursive algorithm that brute-forced every possible combination of operators. I created a class that represented an equation using a final integer array for the elements (since they could never change), and an array list of characters to store the operators.
I also stored the current value of the equation (only using the elements there were operators for) as this saved re-computing the entire value each time an operator was added, I could instead work off the existing value. Finally I stored the target value, this wasn't entirely necessary, but it made things easier.

*N.B. I also decided to use an OO approach to maintain flexibility and maximise possible code-reuse in Part 2 - i thought an equation class with appropriate helper functions could definitely be useful*

My recursive algorithm would check for base-cases (all operators already added, current value equal to, or exceeding, target value), and return the equation if valid, or null if invalid. If a base-case wasn't reached, I would try adding a '+' operator and recurse, if this failed to result in a valid result I would try adding a '*' operator and recurse.

Bug-fixing was a real pain on this one - I had some integer overflows, and resorted to using long's and the Math library to prevent and detect overflows. Finally, I had a really irritating bug in my base-case detection logic that took me ages to figure out: if an equation's current value matched the target value it would return the equation (signalling validity) **without checking if all the operators were added** therefore, an incomplete equation could be marked valid

This solution worked, but it was pretty ugly. It had three main flaws:
- It was recursive, Java doesn't support tail call recursion, so a long solution with lots of backtracking could result in a stack overflow. In addition, returning the result takes forever as it needs to work it's way up the call stack. An iterative approach would be faster and more resilient
- Unnecessary object copying - at each recursive call I copy the Equation object. This is space inefficient, creates lots of work for the garbage collector, and copying the object is fairly compute intensive (especially the operator array copy)
- Most importantly, **evaluating from left to right is a terrible approach**, it results in loads of unnecessary branches!

If I evaluate from right to left (starting with the target value, and dividing by, or taking the difference of, the next element) I can detect invalid branches earlier when dividing by an element. If a division results in a decimal, that branch is invalid. When multiplying the result is always an integer, making this impossible to detect when evaluation from left to right.

##### Example of left to right evaluation. Equation: target value 16, elements {1,3,4}.
Step 1:\
Equation: 1 + 3 _ 4. Current value: 1 + 3 = 4, target: 16\
Step 2:\
Equation: 1 + 3 + 4. Current value: 1 + 3 + 4 = 8, target: 16\
Step 3 (backtrack):\
Equation: 1 + 3 * 4. Current value: 1 + 3 * 4 = 16, target: 16. IS VALID!

##### Example of right to left evaluation. Equation: target value 16, elements {1,3,4}.
Step 1:\
Equation: 1 _ 3 * 4. Current value (target is 1 - the value of the first element): 16 / 4 = 4\
Step 2:\
Equation: 1 * 3 * 4. Current value is 16 / 4 / 3 = 1.3 -> (while this also doesn't equal the first element, the division is invalid as it results in a remainder)\
Step 3: (backtrack)\
Equation: 1 + 3 * 4. Current value is 16 / 4 - 3 = 1. IS VALID!

While the example isn't perfect, if it was an element longer it would perfectly illustrate my point (but it would make the examples longer), any division resulting in a remainder can identify an invalid multiplication instantly, pruning invalid branches.

I implemented right to left evaluation (with the inefficient, copy heavy recursive function used in my previous, LTR implementation) and measured the average time it took to validate all the equations (and sum their totals).
I tried to re-use as much code as possible, and had a mix of flags and specific functions to differentiate between LTR and RTL modes, which wasn't beautiful, but it shows the difference between the different implementations well.

The average execution time for LTR evaluation was: 36.74ms and the RTL evaluation was: 2.66ms, using RTL was a ~13X speedup\
*N.B. I took the mean time of 50 runs to calculate these results - there was a good bit of noise even using the means, (I used System.currentTimeMillis to measure the time before and after each run)*

### Part 2
#### Problem
Another operator is introduced, the '||'. This operator concatenates the digits of the elements either side of it. For example: 100||23 = 10023. As in Part 1, calculate the sum of all possibly valid equations, but this time using the new concatenation operator in addition to sum and product.
#### Solution
When trying to add this extra operator my messy code I wrote to solve part 1 collapsed under the weight of my ineptitude. I decided to write a new, much shorter, cleaner solution to Part 1 (only supporting RTL evaluation). 
Instead of trying to preserve the original problem, and the operations I had done so far (with the element list and operation array), I only kept track of unprocessed element in a stack (I only ever care about the next element), and a simple current value integer (with a target of 0). This removed the need for my unnecessary Equation class that had grown into a monster.
Starting from scratch made my code way shorter and more readable, going from over 300 lines to 91, it was definitely a good idea.

I first tried to add the concatenation operator without really considering it's intricacies. I (incorrectly) implemented it by popping the next two elements from the stack, concatenating them, and pushing it back onto the stack, then recursing. This worked for simple equations, like 198: 1, 98, but said the equation 7290: 6 8 6 15 (6 * 8 || 6 * 15), where the left element is changed before it's concatenated (when going from right to left).
I initially thought that I may have to go back to left to right evaluation, as I couldn't see a way to evaluate the possible values of the left element prior to concatenation. However, after a bit of head scratching, and solving an equation or two by hand, I realised that I could remove the last digits from the current value when doing a concatenation, as I always new what the last digits of the result would be.

For example: Target value: 566, elements: 7, 8, 6. Let's say the algorithm wants to try adding a concat operation (between the 8 and the 6 - we are going from right to left), I can verify it could possibly be a valid operation by comparing the current element, 6, with the last digit(s) of the current value. Since they match I can recurse and call with target value: 56, and elements 78.
While this seems obvious in retrospect, I was really happy I made RTL evaluation work with this new operator, and with how clean my final solution is.

