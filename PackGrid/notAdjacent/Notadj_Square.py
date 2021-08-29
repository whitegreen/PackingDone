# the same results by notAdjacent/Notadj_Square.java
import gurobipy as gp
from gurobipy import GRB, LinExpr
import M

DM = 2
P = []  # available cells
for x in range(11):
    for y in range(13):
        P.append((x, y))
for x in range(-4, 0):
    for y in range(5):
        P.append((x, y))
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
Cb = [x for x in range(4, 6)]  # category B (4,5)
for a in Ca:
    for b in Cb:
        pair = (b, a)
        AAdict[pair] = M.calAA2(pair, A)

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
        for k in range(K):
            m.addConstr(X[k].sum() >= 1)  # at least one copy of each type

        le = LinExpr()
        for k in Ca:
            le.add(X[k].sum())
        m.addConstr(le >= 12)  # category A >=12

        le = LinExpr()
        for k in Cb:
            le.add(X[k].sum())
        m.addConstr(le >= 14)  # category B >=14

        # *********************************** not adjacent ***********************************
        # patches from category A are apart
        for ti in range(len(Ca)):
            for tj in range(ti, len(Ca)):
                AA = M.calAA2((ti, tj), A)
                bigM = len(AA)
                for v in P:
                    le = LinExpr()
                    for pa in AA:
                        u = M.add(v, pa)
                        if u in P:
                            le.add(X[ti][u])
                    le.addTerms(bigM, X[tj][v])
                    m.addConstr(le <= bigM)

        # patches from category A apart from that from category B
        for tj in Ca:
            for v in P:
                bigM = 0
                le = LinExpr()
                for ti in Cb:
                    AA = AAdict[(ti, tj)]
                    bigM += len(AA)
                    for pa in AA:
                        u = M.add(v, pa)
                        if u in P:
                            le.add(X[ti][u])
                le.addTerms(bigM, X[tj][v])
                m.addConstr(le <= bigM)

        m.optimize()
        for k in range(K):
            for i in range(len(P)):
                if 0.5 < X[k][P[i]].x:
                    print(k, i)

    except gp.GurobiError as er:
        print(' ****** error ******')


optimize()