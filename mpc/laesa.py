import asyncio
#docker-compose run --rm honeybadgermpc bash
#python apps/tutorial/laesa.py 2>&1 | tee out
from honeybadgermpc.mpc import TaskProgramRunner
from honeybadgermpc.preprocessing import (
    PreProcessedElements as FakePreProcessedElements,
)
from honeybadgermpc.utils.typecheck import TypeCheck

from honeybadgermpc.progs.mixins.dataflow import Share
from honeybadgermpc.progs.mixins.share_arithmetic import (
    BeaverMultiply,
    BeaverMultiplyArrays,
    MixinConstants,
)
from honeybadgermpc.progs.mixins.share_comparison import Equality, LessThan

config = {
    MixinConstants.MultiplyShareArray: BeaverMultiplyArrays(),
    MixinConstants.MultiplyShare: BeaverMultiply(),
    MixinConstants.ShareEquality: Equality(),
    MixinConstants.ShareLessThan: LessThan()
}

def levensteinDistance_normal(x, y):
    m = len(x)
    n = len(y)
    dynamicProgMatrix = [[0 for i in range(n+1)] for i in range(m+1)]
    for i in range(1, m+1):
        dynamicProgMatrix[i][0] = dynamicProgMatrix[i][0] + m
    for j in range(1, n+1):
        dynamicProgMatrix[0][j] = dynamicProgMatrix[0][j] + n
    for j in range(1, n+1):
        for i in range(1, m+1):
            cmp = 1 if (x[i-1] == y[j-1]) else 0
            if cmp == 0:
                substitution = 1
            else:
                substitution = 0
            op1 = dynamicProgMatrix[i-1][j]+1
            op2 = dynamicProgMatrix[i][j-1]+1
            op3 = dynamicProgMatrix[i-1][j-1]+substitution
            cmp12 = 1 if (op1 < op2) else 0
            cmp13 = 1 if (op1 < op3) else 0
            cmp23 = 1 if (op2 < op3) else 0
            #getting the min([op1,op2,op3])
            if  cmp12 == 1 and cmp13 == 1:
                dynamicProgMatrix[i][j] = op1
            elif cmp12 == 0 and cmp23 == 1:
                dynamicProgMatrix[i][j] = op2
            else:
                dynamicProgMatrix[i][j] = op3
    return dynamicProgMatrix[m][n]

async def levensteinDistance(ctx, x, y):
    m = len(x)
    n = len(y)
    dynamicProgMatrix = [[ctx.Share(0) + ctx.preproc.get_zero(ctx) for i in range(n+1)] for i in range(m+1)]
    for i in range(1, m+1):
        dynamicProgMatrix[i][0] = dynamicProgMatrix[i][0] + ctx.Share(m)
    for j in range(1, n+1):
        dynamicProgMatrix[0][j] = dynamicProgMatrix[0][j] + ctx.Share(n)
    for j in range(1, n+1):
        for i in range(1, m+1):
            cmp = await (x[i-1] == y[j-1]).open()
            if cmp == 0:
                substitution = ctx.Share(1) + ctx.preproc.get_zero(ctx)
            else:
                substitution = ctx.Share(0) + ctx.preproc.get_zero(ctx)
            op1 = dynamicProgMatrix[i-1][j]+ctx.Share(1)
            op2 = dynamicProgMatrix[i][j-1]+ctx.Share(1)
            op3 = dynamicProgMatrix[i-1][j-1]+substitution
            cmp12 = await(op1 < op2).open()
            cmp13 = await(op1 < op3).open()
            cmp23 = await(op2 < op3).open()
            #getting the min([op1,op2,op3])
            if  cmp12 == 1 and cmp13 == 1:
                dynamicProgMatrix[i][j] = op1
            elif cmp12 == 0 and cmp23 == 1:
                dynamicProgMatrix[i][j] = op2
            else:
                dynamicProgMatrix[i][j] = op3
    return dynamicProgMatrix[m][n]

#replacement for abs(x-y)
def share_sub_abs_normal(x, y):
    cmp = 1 if (y < x) else 0
    if cmp == 0:
        return y - x
    else:
        return x - y

#replacement for abs(x-y)
async def share_sub_abs(x, y):
    cmp = (y < x).open()
    if cmp == 0:
        return y - x
    else:
        return x - y

def laesa_normal(point, threshold, pivots, points):
    distMatrix = []
    for pt in points:
        ls = []
        for x in pivots:
            ld = levensteinDistance_normal(x, pt)
            ls.append(ld)
        distMatrix.append(ls)
    distancesFromPivots = []
    for x in pivots:
        ld = levensteinDistance_normal(point, x)
        distancesFromPivots.append(ld)
    pivotRanges = []
    for x in distancesFromPivots:
        dist = share_sub_abs_normal(x, threshold)
        pivotRanges.append((dist,x+threshold))
    contenders = [(i, p) for i, p in enumerate(points)]
    finalcontenders = []
    for j in range(0, len(pivots)):
        lower = pivotRanges[j][0]
        upper = pivotRanges[j][1]
        for (i, p) in contenders:
            cmp1 = 1 if (distMatrix[i][j] < lower) else 0
            cmp2 = 1 if (upper < distMatrix[i][j]) else 0
            if cmp1 == 0 or cmp2 == 0:
                finalcontenders.append(p)
    res = []
    for cont in finalcontenders:
        ld = levensteinDistance_normal(point, cont)
        cmpld = 1 if (threshold < ld) else 0
        if cmpld == 0:
            res.append(cont)
    return res

async def laesa(ctx, point, threshold, pivots, points):
    distMatrix = []
    for pt in points:
        ls = []
        for x in pivots:
            ld = await levensteinDistance(ctx, x, pt)
            ls.append(ld)
        distMatrix.append(ls)
    distancesFromPivots = []
    for x in pivots:
        ld = await levensteinDistance(ctx, point, x)
        distancesFromPivots.append(ld)
    pivotRanges = []
    for x in distancesFromPivots:
        dist = await share_sub_abs(x, threshold)
        pivotRanges.append((dist,x+threshold))
    contenders = [(i, p) for i, p in enumerate(points)]
    for j in range(0, len(pivots)):
        lower = pivotRanges[j][0]
        upper = pivotRanges[j][1]
        for (i, p) in contenders:
            cmp1 = await (distMatrix[i][j] < lower).open()
            cmp2 = await (upper < distMatrix[i][j]).open()
            if not(cmp1 == 0 or cmp2 == 0):
                contenders.remove((i,p))
    for _,cont in contenders:
        ld = await levensteinDistance(ctx, point, cont)
        cmpld = await (threshold < ld).open()#ld <= threshold ~ not threshold < ls
        if cmpld == 0:
            return ctx.ShareArray(cont)
    return ctx.ShareArray([])

async def laesa_test_1(ctx):
    points = ["bob","j"]
    norm_points = [[ord(x) for x in word] for word in points]
    share_points = [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in points]
    pivots = ["ab"]
    norm_pivots = [[ord(x) for x in word] for word in pivots]
    share_pivots =  [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in pivots]
    point = "bo"
    norm_point = [ord(x) for x in point]
    share_point = [ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in point]
    threshold = 1
    share_threshold = ctx.preproc.get_zero(ctx) + ctx.Share(threshold)

    res_normal = laesa_normal(norm_point, threshold, norm_pivots, norm_points)
    out_normal=list(set(["".join([chr(x) for x in match]) for match in res_normal]))
    print(f"[{ctx.myid}] expecting one of {out_normal}")

    res = await laesa(ctx, share_point, share_threshold, share_pivots, share_points)
    res_ = await res.open()
    assert (res_normal == []) or (res_ in res_normal)
    out="".join([chr(x) for x in res_])
    print(f"[{ctx.myid}] laesa OK {out} in {out_normal}")

async def laesa_test_2(ctx):
    points = ["alice", "bob"]
    norm_points = [[ord(x) for x in word] for word in points]
    share_points = [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in points]
    pivots = ["ab"]
    norm_pivots = [[ord(x) for x in word] for word in pivots]
    share_pivots =  [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in pivots]
    point = "bo"
    norm_point = [ord(x) for x in point]
    share_point = [ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in point]
    threshold = 1
    share_threshold = ctx.preproc.get_zero(ctx) + ctx.Share(threshold)

    res_normal = laesa_normal(norm_point, threshold, norm_pivots, norm_points)
    out_normal=list(set(["".join([chr(x) for x in match]) for match in res_normal]))
    print(f"[{ctx.myid}] expecting one of {out_normal}")

    res = await laesa(ctx, share_point, share_threshold, share_pivots, share_points)
    res_ = await res.open()
    assert (res_normal == []) or (res_ in res_normal)
    out="".join([chr(x) for x in res_])
    print(f"[{ctx.myid}] laesa OK {out} in {out_normal}")

async def laesa_test_3(ctx):
    points = ["jim","bob","alice"]
    norm_points = [[ord(x) for x in word] for word in points]
    share_points = [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in points]
    pivots = ["ab", "cd"]
    norm_pivots = [[ord(x) for x in word] for word in pivots]
    share_pivots =  [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in pivots]
    point = "bo"
    norm_point = [ord(x) for x in point]
    share_point = [ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in point]
    threshold = 1
    share_threshold = ctx.preproc.get_zero(ctx) + ctx.Share(threshold)

    res_normal = laesa_normal(norm_point, threshold, norm_pivots, norm_points)
    out_normal=list(set(["".join([chr(x) for x in match]) for match in res_normal]))
    print(f"[{ctx.myid}] expecting one of {out_normal}")

    res = await laesa(ctx, share_point, share_threshold, share_pivots, share_points)
    res_ = await res.open()
    assert (res_normal == []) or (res_ in res_normal)
    out="".join([chr(x) for x in res_])
    print(f"[{ctx.myid}] laesa OK {out} in {out_normal}")

async def laesa_test_4(ctx):
    points = ["jim","bob","alice", "bor"]
    norm_points = [[ord(x) for x in word] for word in points]
    share_points = [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in points]
    pivots = ["ab", "cd"]
    norm_pivots = [[ord(x) for x in word] for word in pivots]
    share_pivots =  [[ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in word] for word in pivots]
    point = "bo"
    norm_point = [ord(x) for x in point]
    share_point = [ctx.preproc.get_zero(ctx) + ctx.Share(ord(x)) for x in point]
    threshold = 1
    share_threshold = ctx.preproc.get_zero(ctx) + ctx.Share(threshold)

    res_normal = laesa_normal(norm_point, threshold, norm_pivots, norm_points)
    out_normal=list(set(["".join([chr(x) for x in match]) for match in res_normal]))
    print(f"[{ctx.myid}] expecting one of {out_normal}")

    res = await laesa(ctx, share_point, share_threshold, share_pivots, share_points)
    res_ = await res.open()
    assert (res_normal == []) or (res_ in res_normal)
    out="".join([chr(x) for x in res_])
    print(f"[{ctx.myid}] laesa OK {out} in {out_normal}")

async def prog():
    n, t = 4, 1
    pp = FakePreProcessedElements()
    pp.generate_zeros(1000, n, t)
    pp.generate_triples(120000, n, t)
    pp.generate_share_bits(1000, n, t)
    pp.generate_bits(3000, n, t)
    pp.generate_rands(10000, n, t)
    program_runner = TaskProgramRunner(n, t, config)
    program_runner.add(laesa_test_1)
    results = await program_runner.join()
    return results

def main():
    asyncio.set_event_loop(asyncio.new_event_loop())
    loop = asyncio.get_event_loop()
    loop.run_until_complete(prog())

if __name__ == "__main__":
    main()
