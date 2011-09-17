class GameState(object):
    def __init__(self, grid):
        self.grid = grid
    
    def copy(self):
        new_grid = []
        
        for i in range(3):
            new_grid.append([])
            
            for j in range(3):
                new_grid[i].append(self.grid[i][j])
        
        return new_grid
        
    def value(self):
        # 1. Check all horizontal rows
        # 2. Check all vertical columns
        # 3. Check both diagonal rows
        
        # Check for horizontal rows
        for i in range(3):
            if self.grid[i] == ["X", "X", "X"]:
                return 1
            elif self.grid[i] == ["O", "O", "O"]:
                return -1
            
        # Check for vertical columns
        for j in range(3):
            column = ""
            
            for i in range(3):
                column += self.grid[i][j]
                
            if column == "XXX":
                return 1
            elif column == "OOO":
                return -1
        
        # Check for diagonal winning
        diagonal_first  = self.grid[0][0] + self.grid[1][1] + self.grid[2][2]
        diagonal_second = self.grid[0][2] + self.grid[1][1] + self.grid[2][0]
        
        if diagonal_first == "XXX" or diagonal_second == "XXX":
            return 1
        elif diagonal_first == "OOO" or diagonal_first == "OOO":
            return -1
        
        # Check if a move can be made
        for i in range(3):
            for j in range(3):
                if self.grid[i][j] == " ":
                    return None
        
        # Otherwise, it's a tie
        return 0
        

def children(state, player):
        states = []
        
        for i in range(3):
            for j in range(3):
                if state.grid[i][j] == " ":
                    new_grid  = state.copy()
                    new_grid[i][j] = player
                    
                    states.append(GameState(new_grid))
                    
        return states
                    
        
def min_value(state, alpha, beta):
    val = state.value()
    if val is not None:
        return val
    
    val = 100000
    for child in children(state, 'O'):
        val = min(val, max_value(child, alpha, beta))
        
        if val <= alpha:
            return val
        
        beta = min(beta, val)
        
    return val
    
def max_value(state, alpha, beta):
    val = state.value()
    if val is not None:
        return val
    
    val = -100000
    for child in children(state, 'X'):
        val = max(val, min_value(child, alpha, beta))
        
        if val >= beta:
            return val
            
        alpha = max(alpha, val)
    
    return val

if __name__ == "__main__":
    root_state = GameState([
        ["O", "O", "X"],
        [" ", "X", " "],
        ["O", "X", " "]
    ])
    
    print max_value(root_state, -100000000, 100000000)
    