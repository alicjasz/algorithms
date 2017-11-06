class Edge:

    def __init__(self, s, t, c):
        self.source = s
        self.target = t
        self.capacity = c
        self.flow = 0
        self.return_edge = None
