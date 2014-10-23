#!/usr/bin/env python
import argparse
import os
from collections import Counter
import texttable

parser = argparse.ArgumentParser(description='mhm.')
parser.add_argument('files', nargs='+')
args = parser.parse_args()

sim = Counter()
keys = []
for _f1 in args.files:
    f1, _ = os.path.splitext(os.path.basename(_f1))
    keys.append(f1)
    for _f2 in args.files:
        f2, _ = os.path.splitext(os.path.basename(_f2))
        #print f1, f2
        
        f1c = open(_f1, 'r').readlines()
        f2c = open(_f2, 'r').readlines()
        
        if f1c == f2c:
            sim[(f1, f2)] = 1

keys = sorted(keys)

tt = texttable.Texttable()
tt.add_row(keys + [' '])
for k1 in keys:
    row = []
    for k2 in keys:
        row.append(sim[(k1, k2)])

    row.append(k1)
    tt.add_row(row)

print tt.draw()
