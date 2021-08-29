import IPAdj
import M

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

DM=3
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
ranges = [[3, 4], [1, 4], [1, 4], [1, 4], [1, 4], [1, 4], [1, 4], [1, 4], [1, 4]]

def calAA(ids):
    ka = ids[0]
    kb = ids[1]
    mina = [min(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    maxa = [max(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    minb = [min(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    maxb = [max(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    bound_B = M.expandFn(templates[kb], neighFn)
    list = []
    for i in range(minb[0] - maxa[0] - 1, maxb[0] - mina[0] + 1 + 1):
        for j in range(minb[1] - maxa[1] - 1, maxb[1] - mina[1] + 1 + 1):
            for k in range(minb[2] - maxa[2] - 1, maxb[2] - mina[2] + 1 + 1):
                if i + j + k == 0:
                    tup = (i, j, k)
                    patch = [M.add(tp, tup) for tp in templates[ka]]
                    if (not M.overlap(templates[kb], patch)) and M.overlap(bound_B, patch):
                        list.append(tup)
    return list

pairs = [(0, 4), (4, 0), (4, 4)]
aa_dic = {}
for ids in pairs:
    aa_dic[ids] = calAA(ids)


IPAdj.optimize(P, neighFn, templates, N=ranges, AAdict=aa_dic)
# the same results by Java: testAdjacent/TestHex.java
