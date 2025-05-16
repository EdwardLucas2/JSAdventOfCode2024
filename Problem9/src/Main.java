import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SolvePart1V1("input.txt", true);
        SolvePart1V2("input.txt", true);
        //SolvePart2("input.txt");
    }

    public static void SolvePart1V1(String filename, boolean measureTime) {
        if(!measureTime) {
            int[] diskMap = SimpleDiskMapFileParser(filename);
            SimpleCompressDiskMap(diskMap);
            long checksum = SimpleCalcChecksum(diskMap);
            System.out.println("Simple checksum: " + checksum);
        } else {
            double totalTime = 0;
            int[] diskMap = SimpleDiskMapFileParser(filename);

            for(int i = 0; i < 100; i++) {
                diskMap = SimpleDiskMapFileParser(filename);
                assert diskMap != null;

                long startTime = System.currentTimeMillis();
                SimpleCompressDiskMap(diskMap);
                SimpleCalcChecksum(diskMap);
                long endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
            }

            double avgTime = totalTime / 100.0;

            System.out.println("Avg V1 time: " + avgTime);
        }
    }

    public static void SolvePart1V2(String fn, boolean measureTime) {
        DiskMap diskMap = ParseFileToMap(fn);

        if (diskMap == null) {
            System.err.println("Disk map could not be parsed");
            return;
        }

        if(!measureTime) {
            diskMap.CompressWithFrag();
            System.out.println("Checksum: " + diskMap.Checksum());
        } else {
            double totalTime = 0;
            for(int i = 0; i < 100; i++) {
                diskMap = ParseFileToMap(fn);
                assert diskMap != null;

                long startTime = System.currentTimeMillis();
                diskMap.CompressWithFrag();
                diskMap.Checksum();
                long endTime = System.currentTimeMillis();
                totalTime += (endTime - startTime);
            }

            double avgTime = totalTime / 100.0;
            System.out.println("Avg V2 time: " + avgTime);
        }
    }


    public static void SolvePart2(String fn) {
        DiskMap diskMap = ParseFileToMap(fn);

        if (diskMap == null) {
            System.err.println("Disk map could not be parsed");
            return;
        }

        diskMap.CompressNoFrag();

        System.out.println("Checksum: " + diskMap.Checksum());
    }

    public static int[] SimpleDiskMapFileParser(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            ArrayList<Integer> list = new ArrayList<>();

            String line = scanner.nextLine();

            boolean file = true;
            int fileIdx = 0;
            for(char c : line.toCharArray()) {
                int digit = Character.getNumericValue(c);

                if(file) {
                    for (int i = 0; i < digit; i++) {
                        list.add(fileIdx);
                    }
                    fileIdx++;
                    file = false;
                } else {
                    for (int i = 0; i < digit; i++) {
                        list.add(-1);
                    }
                    file = true;
                }
            }

            return list.stream().mapToInt(i -> i).toArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String SimpleDiskMapToString(int[] diskMap) {
        StringBuilder sb = new StringBuilder();
        for (int j : diskMap) {
            if (j < 0) {
                sb.append(".");
            } else {
                sb.append(j);
            }
        }

        return sb.toString();
    }
    public static long SimpleCalcChecksum(int[] diskMap) {
        long sum = 0;

        for(int i = 0; i < diskMap.length; i++) {
            if(diskMap[i] > 0)
                sum += (long) diskMap[i] * (long)i;
        }

        return sum;
    }
    public static void SimpleCompressDiskMap(int[] diskMap) {
        int lPtr = 0, rPtr = diskMap.length - 1;

        while(lPtr < rPtr) {
            //Is the current block at rightPtr a space or a file block
            if(diskMap[rPtr] < 0) {
                rPtr--;
            } else if(diskMap[lPtr] < 0) {
                //It's a space, move rPtr block here
                diskMap[lPtr] = diskMap[rPtr];
                diskMap[rPtr] = -1;
                lPtr++;
                rPtr--;
            } else {
                //It's a block
                lPtr++;
            }
        }
    }


    public static DiskMap ParseFileToMap(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            if(!scanner.hasNextLine())
                return null;

            ArrayList<Block> blocks = new ArrayList<>();

            String line = scanner.nextLine();

            boolean file = true;
            int fileIndex = 0;
            for(int i = 0; i < line.length(); i++) {
                int c = line.charAt(i) - '0';

                //Is this a file block
                if(file) {
                    //Yes, it's a file
                    blocks.add(new Block(fileIndex, c));
                    fileIndex++;
                    file = false;
                } else {
                    //It's a space
                    blocks.add(new Block(-1, c));
                    file = true;
                }
            }

            return new DiskMap(blocks);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public record Block(int index, int size) { }

    public record DiskMap(ArrayList<Block> blocks) {
        public long Checksum() {
            long sum = 0, pos = 0;

            for (Block b : blocks) {
                //Check if block is file
                if (b.index >= 0) {
                    // It's a file block, calculate the blocks checksum
                    double averagePos = ((long)(2 * pos + (long)b.size - 1) / (double)2.0);
                    long check = (long)((long)b.index * (long)b.size * averagePos);
                    sum += check;
                    //System.out.println("For block index " + b.index + ", checksum " + check + ", sum: " + sum);
                }
                //Increment pos
                pos += b.size;
            }

            return sum;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (Block b : blocks) {
                //Is this block a file
                if (b.index >= 0) {
                    //It is
                    sb.append(String.valueOf(b.index).repeat(b.size));
                    //sb.append("|");
                } else {
                    //It's an empty block
                    sb.append(String.valueOf('.').repeat(b.size));
                    //sb.append("|");
                }
            }

            return sb.toString();
        }

        //Compression for part 1
        public void CompressWithFrag() {
            int lPtr = 0, rPtr = blocks().size() - 1;

            while(lPtr <= rPtr) {
                //System.out.println("lPtr: " + lPtr + ", left block index: " + blocks.get(lPtr).index + ", size: " + blocks.get(lPtr).size + "\nrPtr: " + rPtr + ", right block index: " + blocks.get(rPtr).index + ", size: " + blocks.get(rPtr).size);
                //System.out.println(this.toString());

                Block lb = blocks().get(lPtr);
                Block rb = blocks().get(rPtr);

                if(rb.index < 0) {
                    rPtr--;
                } else if(lb.index >= 0) {
                    lPtr++;
                } else {
                    //Left pointer is a space, and right pointer is a block, move as many elements in right block to left
                    //Is the right block larger or equal to left block (will left block be removed)
                    if(rb.size >= lb.size) {
                        //Right block is bigger than, or equal to, left
                        //Instantiate a new block to replace the left block, it should be full of files from the right block
                        int index = rb.index;
                        int size = lb.size;
                        Block newFilledLeftBlock = new Block(index, size);

                        //Now replace the right block with two blocks: one empty, one full of the remaining elements that couldn't be moved
                        Block newEmptyRightBlock = new Block(-1, size);
                        Block remainingRightBlock = new Block(index, rb.size - size);

                        //Place the remaining elements where the old right block is
                        blocks.set(rPtr, remainingRightBlock);
                        //Place the empty block to the right of it
                        blocks.add(rPtr + 1, newEmptyRightBlock);

                        //Replace the left block with one that is full
                        blocks.set(lPtr, newFilledLeftBlock);
                        lPtr++;
                    } else {
                        //Left is bigger than right, we need to insert a new block to the left of left pointer with the moved blocks, and modify the existing block
                        Block newFilledLeftBlock = new Block(rb.index, rb.size);
                        Block newEmptyLeftBlock = new Block(-1, lb.size - rb.size);
                        Block newEmptyRightBlock = new Block(-1, rb.size);

                        //Change the current left block to the filled block (shorter than the original)
                        blocks.set(lPtr, newFilledLeftBlock);

                        //Replace the right block with an empty one
                        blocks.set(rPtr, newEmptyRightBlock);

                        //Add the empty end block to the right of the new left block
                        blocks.add(lPtr+1, newEmptyLeftBlock);

                        lPtr++;
                        rPtr--;
                    }
                }
            }
        }

        //Compression for part 2
        public void CompressNoFrag() {
            HashSet<Integer> blocksMoved = new HashSet<>();
            //Iterate over every block, from right to left, trying to move them to the left
            int rPtr = blocks().size() - 1;

            while(rPtr >= 0) {
                //Check if right pointer is a file block, and hasn't already been moved
                if(blocks.get(rPtr).index >= 0 && !blocksMoved.contains(blocks.get(rPtr).index)) {
                    //Add this block to the moved set
                    blocksMoved.add(blocks.get(rPtr).index);

                    //Get the size and index of the file to move
                    int sizeToMove = blocks.get(rPtr).size;
                    int indexToMove = blocks.get(rPtr).index;


                    //Iterate over all the blocks to the left of this block, looking for a free space large enough for this one
                    for(int l = 0; l < rPtr; l++) {
                        //Is this block free space, and big enough
                        if(blocks.get(l).index == -1 && blocks.get(l).size >= sizeToMove) {
                            //It is, initialise the block for the remaining empty spaces
                            Block remainingSpaceLeft = new Block(-1, blocks.get(l).size - sizeToMove);

                            //Set the left block to the right block
                            blocks.set(l, blocks.get(rPtr));

                            //Initialise the new right empty block
                            Block rightEmpty = new Block(-1, sizeToMove);
                            //Set the right block
                            blocks.set(rPtr, rightEmpty);

                            //If there are remaining spaces, add the new empty block
                            if(remainingSpaceLeft.size > 0)
                                blocks.add(l+1, remainingSpaceLeft);
                            break;
                        }
                    }
                }

                rPtr--;
            }
        }
    }
}
