import IPGrid
import M

DM=5
P = [(-2, -2, 0, 1, 0), (-2, -2, 0, 1, 1), (-2, -2, 0, 2, 0), (-2, -2, 0, 2, 1), (-2, -2, 1, 1, 0), (-2, -2, 1, 2, 0),
     (-2, -2, 1, 2, 1), (-2, -1, 0, 1, 0), (-2, -1, 1, 1, -1), (-2, -1, 1, 1, 0), (-2, -1, 1, 2, -1), (-2, -1, 1, 2, 0),
     (-2, -1, 2, 1, -1), (-2, -1, 2, 2, -1), (-2, 0, 1, 0, -2), (-2, 0, 1, 0, -1), (-2, 0, 1, 1, -2), (-2, 0, 1, 1, -1),
     (-2, 0, 2, 0, -2), (-2, 0, 2, 1, -2), (-2, 0, 2, 1, -1), (-2, 1, 1, 0, -2), (-2, 1, 2, 0, -2), (-2, 1, 2, 1, -2),
     (-1, -2, -1, 1, 1), (-1, -2, -1, 1, 2), (-1, -2, -1, 2, 1), (-1, -2, -1, 2, 2), (-1, -2, 0, 1, 0),
     (-1, -2, 0, 1, 1), (-1, -2, 0, 2, 1), (-1, -1, -1, 0, 0), (-1, -1, -1, 0, 1), (-1, -1, -1, 1, 0),
     (-1, -1, -1, 1, 1), (-1, -1, 0, 0, -1), (-1, -1, 0, 0, 0), (-1, -1, 0, 1, -1), (-1, -1, 0, 1, 0),
     (-1, -1, 0, 1, 1), (-1, -1, 1, 0, -1), (-1, -1, 1, 1, -1), (-1, -1, 1, 1, 0), (-1, 0, 0, -1, -1),
     (-1, 0, 0, 0, -1), (-1, 0, 0, 0, 0), (-1, 0, 1, -1, -1), (-1, 0, 1, 0, -2), (-1, 0, 1, 0, -1), (-1, 0, 1, 1, -1),
     (-1, 1, 0, -1, -1), (-1, 1, 1, -1, -2), (-1, 1, 1, -1, -1), (-1, 1, 1, 0, -2), (-1, 1, 1, 0, -1),
     (-1, 1, 2, -1, -2), (-1, 1, 2, 0, -2), (-1, 2, 1, -1, -2), (-1, 2, 2, -1, -2), (0, -2, -2, 0, 1),
     (0, -2, -2, 0, 2), (0, -2, -2, 1, 1), (0, -2, -2, 1, 2), (0, -2, -1, 0, 1), (0, -2, -1, 1, 1), (0, -2, -1, 1, 2),
     (0, -1, -2, 0, 1), (0, -1, -1, -1, 0), (0, -1, -1, -1, 1), (0, -1, -1, 0, 0), (0, -1, -1, 0, 1), (0, -1, -1, 1, 1),
     (0, -1, 0, 0, 0), (0, 0, -1, -1, -1), (0, 0, -1, -1, 0), (0, 0, -1, 0, 0), (0, 0, 0, -1, -1), (0, 0, 0, -1, 0),
     (0, 0, 0, 0, -1), (0, 0, 0, 0, 0), (0, 1, -1, -1, -1), (0, 1, 0, -2, -2), (0, 1, 0, -2, -1), (0, 1, 0, -1, -2),
     (0, 1, 0, -1, -1), (0, 1, 1, -2, -2), (0, 1, 1, -1, -2), (0, 1, 1, -1, -1), (0, 2, 0, -2, -2), (0, 2, 1, -2, -2),
     (0, 2, 1, -1, -2), (1, -2, -2, 0, 1), (1, -2, -2, 0, 2), (1, -2, -2, 1, 2), (1, -1, -2, -1, 1), (1, -1, -2, -1, 2),
     (1, -1, -2, 0, 1), (1, -1, -2, 0, 2), (1, -1, -1, -1, 0), (1, -1, -1, -1, 1), (1, -1, -1, 0, 1), (1, 0, -2, -2, 0),
     (1, 0, -2, -2, 1), (1, 0, -2, -1, 0), (1, 0, -2, -1, 1), (1, 0, -1, -2, 0), (1, 0, -1, -1, -1), (1, 0, -1, -1, 0),
     (1, 0, -1, -1, 1), (1, 1, -2, -2, 0), (1, 1, -1, -2, -1), (1, 1, -1, -2, 0), (1, 1, -1, -1, -1), (1, 1, -1, -1, 0),
     (1, 1, 0, -2, -2), (1, 1, 0, -2, -1), (1, 1, 0, -1, -1), (1, 2, -1, -2, -1), (1, 2, 0, -2, -2), (1, 2, 0, -2, -1),
     (1, 2, 1, -2, -2), (2, -1, -2, -1, 1), (2, -1, -2, -1, 2), (2, 0, -2, -2, 0), (2, 0, -2, -2, 1), (2, 0, -2, -1, 1),
     (2, 1, -2, -2, 0), (2, 1, -2, -2, 1), (2, 1, -1, -2, -1), (2, 1, -1, -2, 0), (2, 2, -1, -2, -1)]
assert (131 == len(P))  # en=5
# ************************************************************************************
K = 5 + 20
templates = []
tzo = [0]*DM
for i in range(DM):
    templates.append([M.harr(tzo, i, -1), tzo, M.harr(tzo, i, 1)])
for i in range(DM):
    for j in range(DM - 1):
        templates.append([templates[i][0], tzo, templates[(i + j+1) % DM][2]])
assert (K == len(templates))
rangeType = [[1, 40] for k in range(K)]

IPGrid.optimize(P, None, templates, rangeType)
# the same results by Java: testPack/PackPenrose.java