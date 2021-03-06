# the same results by categoryAdjacent/C_C_AmmBeen.java
import gurobipy as gp
from gurobipy import GRB, LinExpr
import M

DM = 4
P = [(-3, -3, -2, 0), (-3, -3, -2, 1), (-3, -3, -1, 1), (-3, -3, -1, 2), (-3, -3, 0, 2), (-3, -2, -1, 1),
     (-3, -2, -1, 2), (-3, -2, 0, 1), (-3, -2, 0, 2), (-3, -2, 0, 3), (-3, -2, 1, 2), (-3, -2, 1, 3), (-3, -1, 0, 2),
     (-3, -1, 1, 2), (-3, -1, 1, 3), (-3, -1, 2, 3), (-3, 0, 2, 3), (-2, -3, -3, -1), (-2, -3, -3, 0),
     (-2, -3, -2, -1), (-2, -3, -2, 0), (-2, -3, -2, 1), (-2, -3, -1, 0), (-2, -3, -1, 1), (-2, -2, -2, 0),
     (-2, -2, -1, 0), (-2, -2, -1, 1), (-2, -2, 0, 1), (-2, -2, 0, 2), (-2, -1, -1, 1), (-2, -1, 0, 1), (-2, -1, 0, 2),
     (-2, -1, 1, 1), (-2, -1, 1, 2), (-2, -1, 1, 3), (-2, -1, 2, 3), (-2, 0, 1, 2), (-2, 0, 1, 3), (-2, 0, 2, 2),
     (-2, 0, 2, 3), (-2, 0, 3, 3), (-2, 1, 2, 3), (-2, 1, 3, 3), (-1, -3, -3, -2), (-1, -3, -3, -1), (-1, -3, -2, -1),
     (-1, -3, -2, 0), (-1, -2, -3, -2), (-1, -2, -3, -1), (-1, -2, -2, -1), (-1, -2, -2, 0), (-1, -2, -1, -1),
     (-1, -2, -1, 0), (-1, -2, -1, 1), (-1, -1, -2, -1), (-1, -1, -1, -1), (-1, -1, -1, 0), (-1, -1, -1, 1),
     (-1, -1, 0, 0), (-1, -1, 0, 1), (-1, -1, 1, 1), (-1, -1, 1, 2), (-1, 0, 0, 0), (-1, 0, 0, 1), (-1, 0, 1, 1),
     (-1, 0, 1, 2), (-1, 0, 2, 2), (-1, 0, 2, 3), (-1, 1, 1, 1), (-1, 1, 1, 2), (-1, 1, 2, 1), (-1, 1, 2, 2),
     (-1, 1, 2, 3), (-1, 1, 3, 2), (-1, 1, 3, 3), (-1, 2, 3, 2), (-1, 2, 3, 3), (0, -3, -3, -2), (0, -2, -3, -3),
     (0, -2, -3, -2), (0, -2, -3, -1), (0, -2, -2, -2), (0, -2, -2, -1), (0, -1, -3, -2), (0, -1, -2, -2),
     (0, -1, -2, -1), (0, -1, -1, -1), (0, -1, -1, 0), (0, -1, 0, 0), (0, 0, -1, -1), (0, 0, -1, 0), (0, 0, 0, -1),
     (0, 0, 0, 0), (0, 0, 0, 1), (0, 0, 1, 0), (0, 0, 1, 1), (0, 1, 0, 0), (0, 1, 1, 0), (0, 1, 1, 1), (0, 1, 2, 1),
     (0, 1, 2, 2), (0, 1, 3, 2), (0, 2, 2, 1), (0, 2, 2, 2), (0, 2, 3, 1), (0, 2, 3, 2), (0, 2, 3, 3), (0, 3, 3, 2),
     (1, -2, -3, -3), (1, -2, -3, -2), (1, -1, -3, -3), (1, -1, -3, -2), (1, -1, -2, -3), (1, -1, -2, -2),
     (1, -1, -2, -1), (1, -1, -1, -2), (1, -1, -1, -1), (1, 0, -2, -3), (1, 0, -2, -2), (1, 0, -1, -2), (1, 0, -1, -1),
     (1, 0, 0, -1), (1, 0, 0, 0), (1, 1, -1, -2), (1, 1, -1, -1), (1, 1, 0, -1), (1, 1, 0, 0), (1, 1, 1, -1),
     (1, 1, 1, 0), (1, 1, 1, 1), (1, 1, 2, 1), (1, 2, 1, -1), (1, 2, 1, 0), (1, 2, 1, 1), (1, 2, 2, 0), (1, 2, 2, 1),
     (1, 2, 3, 1), (1, 2, 3, 2), (1, 3, 2, 0), (1, 3, 2, 1), (1, 3, 3, 1), (1, 3, 3, 2), (2, -1, -3, -3),
     (2, -1, -2, -3), (2, 0, -3, -3), (2, 0, -2, -3), (2, 0, -2, -2), (2, 0, -1, -3), (2, 0, -1, -2), (2, 1, -2, -3),
     (2, 1, -1, -3), (2, 1, -1, -2), (2, 1, -1, -1), (2, 1, 0, -2), (2, 1, 0, -1), (2, 1, 1, -1), (2, 2, 0, -2),
     (2, 2, 0, -1), (2, 2, 1, -1), (2, 2, 1, 0), (2, 2, 2, 0), (2, 3, 1, -1), (2, 3, 1, 0), (2, 3, 2, -1),
     (2, 3, 2, 0), (2, 3, 2, 1), (2, 3, 3, 0), (2, 3, 3, 1), (3, 0, -2, -3), (3, 1, -2, -3), (3, 1, -1, -3),
     (3, 1, -1, -2), (3, 1, 0, -2), (3, 2, -1, -3), (3, 2, -1, -2), (3, 2, 0, -3), (3, 2, 0, -2), (3, 2, 0, -1),
     (3, 2, 1, -2), (3, 2, 1, -1), (3, 3, 0, -2), (3, 3, 1, -2), (3, 3, 1, -1), (3, 3, 2, -1), (3, 3, 2, 0)]
assert (185 == len(P))  # en=7
# ************************************************************************************
K = DM + 3 * DM + 4
A = []
A.append([(-1, 0, 0, 0), (0, 0, 0, 0), (1, 0, 0, 0)])
A.append([(0, -1, 0, 0), (0, 0, 0, 0), (0, 1, 0, 0)])
A.append([(0, 0, -1, 0), (0, 0, 0, 0), (0, 0, 1, 0)])
A.append([(0, 0, 0, -1), (0, 0, 0, 0), (0, 0, 0, 1)])
for i in range(DM):
    for j in range(3):
        A.append([A[i][0], (0, 0, 0, 0), A[(i +j+ 1) % DM][2]])
A.append([(0, 0, 0, 0), (1, 0, 0, 0)])  # 2 - straight
A.append([(0, 0, 0, 0), (0, 1, 0, 0)])
A.append([(0, 0, 0, 0), (0, 0, 1, 0)])
A.append([(0, 0, 0, 0), (0, 0, 0, 1)])
assert (K == len(A))

_P_ = M.expand(P, DM)
print(len(P), len(_P_))

AAdict = {}
Ca = [x for x in range(4)]  # category A (0-3)
Cb = [x for x in range(4, 16)]  # category B (4-15)
for a in Ca:
    for b in Cb:
        pair = (a, b)
        AA, inv = M.calAA2(pair, A)
        AAdict[pair] = AA
        AAdict[(b, a)] = inv

def optimize():
    try:
        m = gp.Model("Category-Category Adjacent")
        X = [m.addVars(P, vtype=GRB.BINARY) for k in range(K)]
        oe = LinExpr()
        for k in range(K):
            oe.add(X[k].sum(), len(A[k]))
        m.setObjective(oe, GRB.MAXIMIZE)

        for v in P:
            le = LinExpr()
            for k in range(K):
                Akv = [M.sub(v, tp) for tp in A[k]]
                for u in Akv:
                    if u in P:
                        le.add(X[k][u])
            m.addConstr(le <= 1)

        for v in _P_:
            for k in range(K):
                le = LinExpr()
                Akv = [M.sub(v, tp) for tp in A[k]]
                for u in Akv:
                    if u in P:
                        le.add(X[k][u])
                m.addConstr(le == 0)

        le = LinExpr()
        for k in range(16, 20):
            le.add(X[k].sum())
        m.addConstr(le <= 20)  # sum of type 16-19 <= threshold

        # *********************************** adjacency ***********************************
        for tj in Ca:  # category A (0-3)
            # given a tj-type patch, at least one adjacent patch from category B
            for v in P:
                le = LinExpr()
                for ti in Cb:  # category B (4-15)
                    for pa in AAdict[(ti, tj)]:
                        u = M.add(v, pa)
                        if u in P:
                            le.add(X[ti][u])
                m.addConstr(le >= X[tj][v])

        for tj in Cb:  # category B (4-15)
            # given a tj-type patch, at least one adjacent patch from category A
            for v in P:
                le = LinExpr()
                for ti in Ca:  # category A (0-3)
                    for pa in AAdict[(ti, tj)]:
                        u = M.add(v, pa)
                        if u in P:
                            le.add(X[ti][u])
                m.addConstr(le >= X[tj][v])

        m.optimize()
        for k in range(K):
            for i in range(len(P)):
                if 0.5 < X[k][P[i]].x:
                    print(k, i)

    except gp.GurobiError as er:
        print(' ****** error ******')


optimize()
