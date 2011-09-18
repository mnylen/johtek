from math import sqrt
from Queue import PriorityQueue

MAXIMUM_SPEED = 1000

class TransportationStop:
    def __init__(self, transportation, stop, time_spent):
        self.transportation = transportation
        self.stop = stop
        self.time_spent = time_spent
        self.next = None
    
    def set_next(self, next):
        self.next = next
        next.time_spent_diff = next.time_spent - self.time_spent
        
    def __str__(self):
        if self.next:
            return "%s at %s, next: %s" % (self.transportation.name, self.stop.name, self.next.stop.name)
        else:
            return "%s at %s, final stop" % (self.transportation.name, self.stop.name)

class Transportation:
    def __init__(self, name):
        self.name = name
        self.first_stop = None
    
    def set_time_table_entries(self, entries):
        previous = None
        
        for entry in entries:
            stop, time_spent = entry
            tstop = TransportationStop(self, stop, time_spent)
            
            if previous is not None:
                previous.set_next(tstop)
            else:
                self.first_stop = tstop
            
            stop.transportations.add(tstop)
            previous = tstop
        


class Stop(object):
    def __init__(self, number, y_coord, x_coord, name):
        self.number = number
        self.y_coord = y_coord
        self.x_coord = x_coord
        self.name = name
        self.transportations = set()

    def distance(self, other):
        x_distance = abs(other.x_coord - self.x_coord)
        y_distance = abs(other.y_coord - self.y_coord)

        return sqrt(x_distance*x_distance + y_distance*y_distance)

class Node:
    def __init__(self, parent, stop, time, transportation = None):
        self.parent = parent
        self.stop = stop
        self.time = time
        self.transportation = transportation
    
    def __waiting_time(self, tstop):
        if self.parent is None or self.transportation == tstop.transportation:
            return 0
        else:
            departure  = self.time - tstop.time_spent
            departure += 10 - (departure % 10)
            arrival    = departure + tstop.time_spent
                
            if arrival < self.time:
                arrival += 10
                    
            return arrival - self.time
                
    def adjacent(self):
        for tstop in self.stop.transportations:
            next_tstop = tstop.next
            
            if next_tstop is None:
                continue
            
            waiting_time = self.__waiting_time(tstop)
            travel_time = next_tstop.time_spent_diff
            
            yield Node(self, next_tstop.stop, (self.time + waiting_time + travel_time), tstop.transportation)

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

        for adjacent in current.adjacent():
            node_list.put( (fcost(adjacent, to_stop), adjacent) )

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
        print "Found route. Travel time %d minutes.\n" % path[-1].time
        
        previous = None
        for node in path:
            if previous is not None:
                if previous.transportation != node.transportation:
                    print "At %s" % previous.stop.name
                    print "  Take: %s" % node.transportation.name
                    print "    (%02d min) %s" % (previous.time, previous.stop.name)
                
                print "    (%02d min) %s" % (node.time, node.stop.name)
            
            previous = node
        
    else:
        print "No path found :("