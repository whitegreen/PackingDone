# python -m pip install gurobipy
# https://github.com/whitegreen/PackingDone
import gurobipy as gp
from gurobipy import GRB, LinExpr
import M


def optimize(P, Fn, A, N):
    DM = len(P[0])
    K = len(A)
    _P_ = M.expand(P, DM) if Fn is None else M.expandFn(P, Fn)
    try:
        m = gp.Model("Packing Program")
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

        if N is not None:
            for k in range(K):
                le = LinExpr()
                for u in P:
                    le.add(X[k][u])
                m.addConstr(le == N[k])

        m.optimize()
        for k in range(K):
            for i in range(len(P)):
                if 0.5 < X[k][P[i]].x:
                    print(k, i)

    except gp.GurobiError as er:
        print(' ****** error ******')
