import gurobipy as gp
from gurobipy import GRB, LinExpr
import M


def optimize(P, Fn, A, N=None, AHdict=None, AAdict=None):
    DM = len(P[0])
    K = len(A)
    _P_ = M.expand(P, DM) if Fn is None else M.expandFn(P, Fn)
    print(len(P), len(_P_))
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

        ##################### adjacent constraints ###################
        if AHdict is not None:
            for i, AH in AHdict.items():
                for u in P:
                    if u not in AH:
                        m.addConstr(X[i][u] == 0)

        if AAdict is not None:
            for ids, AA in AAdict.items():
                ti = ids[0]
                tj = ids[1]
                for v in P:
                    le = LinExpr()
                    for pt in AA:
                        u = M.add(v, pt)
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
