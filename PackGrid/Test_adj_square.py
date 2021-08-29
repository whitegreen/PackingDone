import IPAdj
import M

DM = 2
P = []  # available cells
for x in range(11):
    for y in range(13):
        if (5 == x or 6 == x) and 6 < y < 10:
            continue
        P.append((x, y))

for y in range(4):
    P.append((-1, y))
    P.append((-2, y))
    P.append((-3, y))

K = 10
templates = [[(0, 0), (1, 0), (0, 1)],
             [(0, 0), (-1, 0), (0, -1)],
             [(0, 0), (0, 1), (-1, 0)],
             [(0, 0), (0, -1), (1, 0)],
             [(0, 1), (0, 0), (0, -1)],
             [(1, 0), (0, 0), (-1, 0)],
             [(0, 1), (0, 0), (-1, 0), (-2, 0)],
             [(0, -1), (0, 0), (-1, 0), (-2, 0)],
             [(1, 0), (0, 0), (-1, 0), (1, 1), (0, 1), (-1, 1)],
             [(0, 1), (0, 0), (0, -1), (1, 1), (1, 0), (1, -1)]]
assert (K == len(templates))
ranges = [[1, 6], [1, 6], [2, 6], [1, 6], [2, 6], [1, 6], [1, 6], [1, 6], [2, 6], [1, 6]]

ah_dic = {4: [(10, 3), (10, 4), (10, 5), (10, 6), (10, 7)],
          2: [(5, 0), (6, 0), (7, 0), (8, 0)]}

pairs = [(0, 8), (1, 8), (2, 8), (3, 8)]
aa_dic = {}
for ids in pairs:
    aa_dic[ids] = M.calAA2(ids, templates)

IPAdj.optimize(P, None, templates, N=ranges, AHdict=ah_dic, AAdict=aa_dic)
# the same results by Java: testAdjacent/TestSquare.java
