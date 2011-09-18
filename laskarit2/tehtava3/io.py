from reittiopas2 import Transportation, Stop

def read_data():
    stops_by_name   = dict()
    stops_by_number = dict()
    
    f = open("data/pysakit.txt", "r")
    for stop_data in f.readlines():
        stop_data = stop_data.strip()
        
        if stop_data == "":
            continue
        
        number, y_coord, x_coord, name = stop_data.split(" ")
        number  = int(number)
        y_coord = int(y_coord)
        x_coord = int(x_coord)
        stop    = Stop(number, y_coord, x_coord, name)
        
        stops_by_name[name]     = stop
        stops_by_number[number] = stop
        
    f.close()
    
    lines = set()
    
    for line_name in ["11", "11b", "1a", "1ab", "3b", "3t", "55k", "55kb", "7a", "7b", "M", "Mb"]:
        line = Transportation(line_name)
        tt_entries = list()
        
        f = open("data/linja%s.txt" % line_name, "r")
        for time_table_entry in f.readlines():
            time_table_entry = time_table_entry.strip()
            
            if time_table_entry == "":
                continue
            
            stop_number, time_spent_str = time_table_entry.split(" ")
            stop_number = int(stop_number)
            time_spent  = int(time_spent_str)
            stop        = stops_by_number[stop_number]
            
            tt_entries.append( (stop, time_spent) )
        
        line.set_time_table_entries(tt_entries)
        
        f.close()
        lines.add(line)
                
    return (stops_by_name, stops_by_number, lines)
