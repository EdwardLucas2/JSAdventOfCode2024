import sys

from dataclasses import dataclass

@dataclass
class Game:
    ButtonA_X: int
    ButtonA_Y: int
    ButtonB_X: int
    ButtonB_Y: int
    Prize_X: int
    Prize_Y: int

    def __init__(self, button_ax, button_ay, button_bx, button_by, prize_x, prize_y):
        self.ButtonA_X = button_ax
        self.ButtonA_Y = button_ay
        self.ButtonB_X = button_bx
        self.ButtonB_Y = button_by
        self.Prize_X = prize_x
        self.Prize_Y = prize_y

    def __str__(self):
        s = ""
        s += "Button A: X:" + str(self.ButtonA_X) + " Y:" + str(self.ButtonA_Y) + "\n"
        s += "Button B: X:" + str(self.ButtonB_X) + " Y:" + str(self.ButtonB_Y) + "\n"
        s += "Prize: X:" + str(self.Prize_X) + " Y:" + str(self.Prize_Y)
        return s

    def getLowestCost(self, a_cost: int, b_cost: int) -> tuple[int, int, int]:
        lowestCost: int = sys.maxsize
        low_a_num: int = -1
        low_b_num: int = -1

        # Iterate through all possible combinations of button presses
        # Calculate the maximum number of presses of button A
        max_a_num = min(self.Prize_X // self.ButtonA_X + 1, self.Prize_Y // self.ButtonA_Y + 1)
        # Calculate the maximum number of presses of button B
        max_b_num = min(self.Prize_X // self.ButtonB_X + 1, self.Prize_Y // self.ButtonB_Y + 1)

        # Iterate through all possible combinations of button presses
        for a_num in range(max_a_num):
            for b_num in range(max_b_num):
                # Check if the current combination of button presses is valid
                if self.validate_presses(a_num, b_num):
                    # Calculate the cost of the current combination
                    cost = a_cost * a_num + b_cost * b_num
                    # Update the lowest cost if the current cost is lower
                    if cost < lowestCost:
                        lowestCost = cost
                        # Store the number of presses of each button
                        low_a_num = a_num
                        low_b_num = b_num

        # Return the lowest cost and the number of presses of each button
        return lowestCost, low_a_num, low_b_num

    def validate_presses(self, a_num, b_num) -> bool:
        # Validate num presses
        if a_num < 0 or b_num < 0:
            raise ValueError("Number of presses cannot be negative")

        # Check X axis
        if self.ButtonA_X * a_num + self.ButtonB_X * b_num != self.Prize_X:
            return False
        # Check Y axis
        if self.ButtonA_Y * a_num + self.ButtonB_Y * b_num != self.Prize_Y:
            return False
        return True