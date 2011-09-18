MAXIMUM_SPEED = 1000

class Node(object):
    def __init__(self, parent, stop, time, line = None):
        self.parent = parent
        self.stop   = stop
        self.time   = time
        self.line   = line
        
    def adjacent_nodes(self):
        nodes = set()
        
        for line in self.stop.transportations:
            next_stop = line.next_stop(self.stop)
            
            if next_stop != None:
                if self.line and line == self.line:
                    waiting_time = 0
                else:
                    waiting_time = line.calculate_waiting_time(self.time, self.stop)
                
                travel_time       = line.calculate_travel_time(self.stop, next_stop)
                time_at_next_stop = self.time + waiting_time + travel_time
                
                nodes.add(Node(self, next_stop, time_at_next_stop, line))
        
        return nodes
            
    def __eq__(self, other):
        return self.stop == other.stop and self.time == other.time
    
    def __hash__(self):
        return hash(self.stop) * 37 + hash(self.time) * 37

class Stop(object):
    def __init__(self, number, y_coord, x_coord, name):
        self.number          = number
        self.y_coord         = y_coord
        self.x_coord         = x_coord
        self.name            = name
        self.transportations = set()
    
    def add_transportation(self, line):
        self.transportations.add(line)
    
    def distance(self, other):
        x_distance = abs(other.x_coord - self.x_coord)
        y_distance = abs(other.y_coord - self.y_coord)
        
        from math import sqrt
        total_distance = sqrt(x_distance*x_distance + y_distance*y_distance)
        
        return total_distance

class Transportation(object):
    def __init__(self, name):
        self.name       = name
        self.time_table = []
            
    def get_time_spent_on_arrival_at(self, stop):
        for entry in self.time_table:
            if entry[0] == stop:
                return entry[1]
        
        return None
        
    def first_stop(self):
        return self.time_table[0][0]
    
    def add_time_table_entry(self, stop, time_spent_from_departure):
        self.time_table.append( (stop, time_spent_from_departure) )
        stop.add_transportation(self)
    
    def calculate_travel_time(self, from_stop, to_stop):
        """
        Calculates the time in minutes it takes for this transportation
        to travel from the given stop to the given stop.
        
        """
        
        from_entry = None
        to_entry   = None
        
        for i in range(len(self.time_table)):
            if self.time_table[i][0] == from_stop:
                from_entry = self.time_table[i]
                break
        
        to_entry = self.time_table[i+1]
        
        if from_entry and to_entry:
            return to_entry[1] - from_entry[1]
        else:
            return None
    
    def calculate_waiting_time(self, current_time, stop):
        """
        Calculates the minutes we need to wait at the stop for
        this transportation to arrive there.
        
        """
        
        travel_time  = self.calculate_travel_time(self.first_stop(), stop)
        waiting_time = 0
        
        # a) Look backward from current_time to see if there's
        #    a departure that arrives at the stop after current_time.
        
        remainder      = (current_time % 10)
        departure_time = current_time - remainder
        
        while departure_time + travel_time >= current_time:
            departure_time -= 10
        
        if departure_time + travel_time <= current_time:
            # we just missed that departure
            departure_time += 10
        
        waiting_time = (departure_time + travel_time) - current_time
        
        
        # b) Look forward from current_time to find a departure
        #    that arrives at the stop after current_time
        
        departure_time = current_time - remainder + 10
        while departure_time + travel_time < current_time:
            departure_time += 10
                
        # Pick the departure that results in the lowest waiting time
        waiting_time = min(waiting_time, (departure_time + travel_time) - current_time)
        
        return waiting_time
        
    def next_stop(self, current_stop):
        for i in range(1, len(self.time_table)):
            if self.time_table[i-1][0] == current_stop:
                return self.time_table[i][0]
        
        return None


from Queue import PriorityQueue

def astar(from_stop, to_stop):
    def fcost(node, target):
        g_cost = node.time
        h_cost = int(node.stop.distance(target) / MAXIMUM_SPEED)
        
        return g_cost + h_cost
    
    start_node  = Node(None, from_stop, 0)

    node_list = PriorityQueue()
    node_list.put( (fcost(start_node, to_stop), start_node) )
    
    target_node = None
    
    while not node_list.empty():
        f_cost, current = node_list.get()
        
        if current.stop == to_stop:
            target_node = current
            break
        
        for adjacent_node in current.adjacent_nodes():
            node_list.put( (fcost(adjacent_node, to_stop), adjacent_node) )
        
    if target_node:
        path = []
        current = target_node
        
        while current is not None:
            path.append(current)
            current = current.parent
            
        path.reverse()
        return path
    else:
        return None
            
if __name__ == "__main__":
    import sys
    from io import read_data
    
    if len(sys.argv) < 3:
        print "Usage: python %s FROM_STOP TO_STOP" % sys.argv[0]
        exit(1)
    
    from_stop_name = sys.argv[1]
    to_stop_name   = sys.argv[2]
    
    # Load data from files
    stops_by_name, stops_by_number, lines = read_data()
    
    if not from_stop_name in stops_by_name:
        print "Unknnown FROM_STOP"
        exit(1)
    elif not to_stop_name in stops_by_name:
        print "Unknown TO_STOP"
        exit(1)
    
    from_stop = stops_by_name[from_stop_name]
    to_stop = stops_by_name[to_stop_name]
    
    path = astar(from_stop, to_stop)
    if path:
        print "Total time: %d minutes" % path[-1].time
        
        previous = None
        for node in path:
            if previous and node.line == previous.line:
                print "\t%s -- %d min" % (node.stop.name, node.time)
            else:
                if node.line is not None:
                    print "Change to %s" % (node.line.name)
                    previous = node
                
                    print "\t%s -- %d min" % (node.stop.name, node.time)
                
                else:
                    print "At %s" % (node.stop.name)
    else:
        print "No path found :("