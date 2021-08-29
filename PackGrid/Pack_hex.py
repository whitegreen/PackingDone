import IPGrid


def neighFn(p):
    return [(p[0] - 1, p[1] + 1, p[2]), (p[0] + 1, p[1] - 1, p[2]), (p[0] - 1, p[1], p[2] + 1),
            (p[0] + 1, p[1], p[2] - 1), (p[0], p[1] - 1, p[2] + 1), (p[0], p[1] + 1, p[2] - 1)]

P = []  # available cells
nn = 11
for x in range(nn):
    for y in range(nn):
        for z in range(nn):
            id = (x - nn // 2, y - nn // 2, z - nn // 2)
            if id[0] < -2 and id[1] > 1:
                continue
            if id[0] == 2 and id[1] == -1:
                continue
            if 0 == id[0] + id[1] + id[2]:
                P.append(id)
print(len(P))
# ************************************************************************************
K = 9
templates = [[(0, -1, 1), (0, 0, 0), (0, 1, -1)],
             [(1, -1, 0), (0, 0, 0), (-1, 1, 0)],
             [(1, 0, -1), (0, 0, 0), (-1, 0, 1)],
             [(0, -1, 1), (0, 0, 0), (-1, 1, 0)],
             [(0, -1, 1), (0, 0, 0), (-1, 0, 1)],
             [(1, -1, 0), (0, 0, 0), (0, 1, -1)],
             [(1, -1, 0), (0, 0, 0), (-1, 0, 1)],
             [(1, 0, -1), (0, 0, 0), (0, 1, -1)],
             [(1, 0, -1), (0, 0, 0), (-1, 1, 0)]]
assert (K == len(templates))
rangeType = [[1, 3] for k in range(K)]

IPGrid.optimize(P, neighFn, templates,rangeType)
# # the same results by Java: testPack/PackHex.java
