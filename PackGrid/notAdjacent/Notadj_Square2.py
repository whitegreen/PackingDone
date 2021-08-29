# the same results by notAdjacent/Notadj_Square2.java
import gurobipy as gp
from gurobipy import GRB, LinExpr
import M

DM=2
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
_P_ = M.expand(P, DM)
print(len(P), len(_P_))

K = 10
A = [[(0, 0), (1, 0), (0, 1)],
             [(0, 0), (-1, 0), (0, -1)],
             [(0, 0), (0, 1), (-1, 0)],
             [(0, 0), (0, -1), (1, 0)],
             [(0, 1), (0, 0), (0, -1)],
             [(1, 0), (0, 0), (-1, 0)],
             [(0, 1), (0, 0), (-1, 0), (-2, 0)],
             [(0, -1), (0, 0), (-1, 0), (-2, 0)],
             [(1, 0), (0, 0), (-1, 0), (1, 1), (0, 1), (-1, 1)],
             [(0, 1), (0, 0), (0, -1), (1, 1), (1, 0), (1, -1)]]
assert (K == len(A))


AAdict = {}
Ca = [x for x in range(4)]  # category A (0-3)
Cb = [x for x in range(8, 10)]  # category B (8,9)
for tj in Ca:
    for ti in Cb:
        pair = (ti, tj)
        AAdict[pair] = M.calAA2(pair, A)
        pair = (tj, ti)
        AAdict[pair] = M.calAA2(pair, A)


def notAdj(m, ti, tj, X):
    AA = M.calAA2((ti, tj), A)
    bigM = len(AA)
    for v in P:
        le = LinExpr()
        for pa in AA:
            u = M.add(pa, v)
            if u in P:
                le.add(X[ti][u])
        le.add( X[tj][v],bigM)
        m.addConstr(le <= bigM)

def adj (m, v, tis, tj, X):
    le = LinExpr()
    for ti in tis:
        AA = AAdict[(ti, tj)]
        for pa in AA:
            u = M.add(pa, v)
            if u in P:
                le.add(X[ti][u])
    m.addConstr(le >= X[tj][v])

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

        # ************************* constraint on number of k-th type patches  **************************
        le = LinExpr()
        for k in range(4, 8):
            le.add(X[k].sum())
        m.addConstr(le <= 4)

        le = LinExpr()
        for k in Cb:
            le.add(X[k].sum())
        m.addConstr(le >= 12)

        # ***********************************  category-category adjacent ************
        for tj in Ca:
            for v in P:
                adj(m, v, Cb, tj, X)
        for tj in Ca:
            for v in P:
                adj(m, v, Cb, tj, X)

        for tj in Cb:
            for v in P:
                adj(m, v, Ca, tj, X)

        # *********************************** not adjacent ***********************************
        notAdj(m, 8, 9, X)
        notAdj(m, 8, 8, X)
        notAdj(m, 9, 9, X)
        # patches from category A are apart

        m.optimize()
        for k in range(K):
            for i in range(len(P)):
                if 0.5 < X[k][P[i]].x:
                    print(k, i)

    except gp.GurobiError as er:
        print(' ****** error ******')


optimize()