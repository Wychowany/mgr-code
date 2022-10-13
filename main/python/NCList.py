from ncls import NCLS
import time


class SingleRow:
    def __init__(self, chromosome, start, end):
        self.chromosome = chromosome
        self.start = start
        self.end = end


class DoubleArray:
    def __init__(self):
        self.starts = []
        self.ends = []


df1 = []
keys = set()
with open("/Users/olek/Desktop/AIListTestData/ex-rna.bed") as file_in:
    for line in file_in:
        split = line.split("\t")
        if len(split) == 3:
            row = SingleRow(split[0], int(split[1]), int(split[2]))
            keys.add(split[0])
            df1.append(row)

new_map = {}
for key in keys:
    new_map[key] = DoubleArray()

for row in df1:
    new_map[row.chromosome].starts.append(row.start)
    new_map[row.chromosome].ends.append(row.end)

trees = {}
# create trees
start_tree = time.time()
for k, v in new_map.items():
    trees[k] = NCLS(v.starts, v.ends, [0])
    trees[k].find_overlap(0, 100)
end_tree = time.time()
tree_execution = round(end_tree - start_tree, 2)
print("Building structure time=" + str(tree_execution))

df2 = []
with open("/Users/olek/Desktop/AIListTestData/ex-anno.bed") as file_in:
    for line in file_in:
        split = line.split("\t")
        if len(split) == 3:
            row = SingleRow(split[0], int(split[1]), int(split[2]))
            df2.append(row)

# find_overlaps
count = 0
chromosome_count = 0

start_overlap = time.time()
j = 0
max_print = 10
for row in df2:
    if row.chromosome in trees:
        tree = trees[row.chromosome]
        count += sum(1 for _ in tree.find_overlap(row.start, row.end))

end_overlap = time.time()
print("                  count=" + str(count))
overlapping_execution = round(end_overlap - start_overlap, 2)
print("       Overlapping time=" + str(overlapping_execution))
print("                    Sum=" + str(tree_execution + overlapping_execution))
