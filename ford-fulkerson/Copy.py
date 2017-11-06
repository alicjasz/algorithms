from Edge import Edge


def create_graph():
    data = open('graph.csv').read()
    lines = data.split("\n")
    network = []
    for i in range(len(lines)):
        s = int(lines[i].split("\t")[0])
        t = int(lines[i].split("\t")[1])
        c = float(lines[i].split("\t")[2])
        new_edge = Edge(s, t, c)
        return_edge = Edge(t, s, 0)
        new_edge.return_edge = return_edge
        return_edge.return_edge = new_edge
        network.append(new_edge)
    return network


def get_neighbours(network, source):
    temp = []
    for n in network:
        if n.source == source:
            temp.append(n)

    return temp


def find_by_target(network, target):
    for n in network:
        if n.source == target:
            return n


def find_path(network, source, target, path):

    if source == target:
        return path
    neighbours = get_neighbours(network, source)
    for n in neighbours:
        residual = n.capacity - n.flow
        if residual > 0 and (n, residual) not in path:
            result = find_path(network, n.target, target, path + [(n, residual)])
            if result is not None:
                return result


def calculate_max_flow(network, source, target):

    path = find_path(network, source, target, [])
    # for p in path:
    #    print(str(p[0].source) + " " + str(p[0].target) + " " + str(p[1]))
    flows = []
    while path is not None:
        for p in path:
            flows.append(p[1])
        flow = min(flows)
        for edge, res in path:
            edge.flow += flow
            edge.return_edge.flow -= flow
        path = find_path(network, source, target, [])
    return sum(edge.flow for edge in get_neighbours(network, source))


def find_max_flow_from_source(network, source):

    x = dict()
    for i in range(0, 7):
        if i == source:
            continue
        x[i] = calculate_max_flow(network, source, i)

    return x

if __name__ == '__main__':
    network = create_graph()
    path = find_path(network, 2, 4, [])
    for p in path:
        print(str(p[0].source) + " " + str(p[0].target) + " " + str(p[1]))
    print(calculate_max_flow(network, 2, 4))
    x = find_max_flow_from_source(network, 5)
    print(x)