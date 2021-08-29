import IPGrid

P = []  # available cells
for x in range(10):
    for y in range(8):
        P.append((x, y))
P.append((-1, 0))
P.append((-1, 1))
P.append((6, -1))
P.append((6, -2))

K = 4
templates = [[(0, 0), (1, 0), (0, 1)],
             [(0, 0), (-1, 0), (0, -1)],
             [(0, 1), (0, 0), (0, -1)],
             [(1, 0), (0, 0), (-1, 0), (-2, 0)]]
assert (K == len(templates))
ranges = [[1, 7] for k in range(K)]

IPGrid.optimize(P, None, templates, ranges)
# the same results by Java: testPack/PackSquare.java

