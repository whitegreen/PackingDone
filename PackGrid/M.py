import operator


def overlap(lista, listb):
    for a in lista:
        if a in listb:
            return True
    return False


def expandFn(vs, custumFn):
    lst = []
    for p in vs:
        bs = custumFn(p)
        for b in bs:
            if b in vs or b in lst:
                continue
            lst.append(b)
    return lst


def expand(vs, DM):
    lst = []
    for p in vs:
        bs = neighFn(p, DM)
        for b in bs:
            if b in vs or b in lst:
                continue
            lst.append(b)
    return lst


def add(a, b):
    return tuple(map(operator.add, a, b))


def sub(a, b):
    return tuple(map(operator.sub, a, b))



def harr(arr, i, v):
    a = [x for x in arr]
    a[i] += v
    return tuple(a)


def neighFn(a, DM):
    list = []
    for i in range(DM):
        list.append(harr(a, i, 1))
    for i in range(DM):
        list.append(harr(a, i, -1))
    return list


def calAA2(ids, templates):
    DM = 2
    ka = ids[0]
    kb = ids[1]
    mina = [min(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    maxa = [max(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    minb = [min(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    maxb = [max(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    bound_B = expand(templates[kb], DM)
    list = []
    for i in range(minb[0] - maxa[0] - 1, maxb[0] - mina[0] + 1 + 1):
        for j in range(minb[1] - maxa[1] - 1, maxb[1] - mina[1] + 1 + 1):
            patch = [add(tp, (i, j)) for tp in templates[ka]]
            if (not overlap(templates[kb], patch)) and overlap(bound_B, patch):
                list.append((i, j))

    return list


def calAA3(ids, templates):
    DM = 3
    ka = ids[0]
    kb = ids[1]
    mina = [min(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    maxa = [max(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    minb = [min(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    maxb = [max(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    bound_B = expand(templates[kb], DM)
    list = []
    for i in range(minb[0] - maxa[0] - 1, maxb[0] - mina[0] + 1 + 1):
        for j in range(minb[1] - maxa[1] - 1, maxb[1] - mina[1] + 1 + 1):
            for k in range(minb[2] - maxa[2] - 1, maxb[2] - mina[2] + 1 + 1):
                tup = (i, j, k)
                patch = [add(tp, tup) for tp in templates[ka]]
                if (not overlap(templates[kb], patch)) and overlap(bound_B, patch):
                    list.append(tup)
    return list


def calAA4(ids, templates):
    DM = 4
    ka = ids[0]
    kb = ids[1]
    mina = [min(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    maxa = [max(templates[ka], key=lambda t: t[i])[i] for i in range(DM)]
    minb = [min(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    maxb = [max(templates[kb], key=lambda t: t[i])[i] for i in range(DM)]
    bound_B = expand(templates[kb], DM)
    list = []
    inv = []
    for i in range(minb[0] - maxa[0] - 1, maxb[0] - mina[0] + 1 + 1):
        for j in range(minb[1] - maxa[1] - 1, maxb[1] - mina[1] + 1 + 1):
            for k in range(minb[2] - maxa[2] - 1, maxb[2] - mina[2] + 1 + 1):
                for l in range(minb[3] - maxa[3] - 1, maxb[3] - mina[3] + 1 + 1):
                    tup = (i, j, k, l)
                    patch = [add(tp, tup) for tp in templates[ka]]
                    if (not overlap(templates[kb], patch)) and overlap(bound_B, patch):
                        list.append(tup)
                        inv.append( (-i, -j, -k, -l) )
    return list, inv
