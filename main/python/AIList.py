from ailist import AIList
import time


class SingleRow:
    def __init__(self, chromosome, start, end):
        self.chromosome = chromosome
        self.start = start
        self.end = end


df1 = []
keys = set()
with open("/Users/olek/Desktop/AIListTestData/ex-rna.bed") as file_in:
    for line in file_in:
        split = line.split("\t")
        if len(split) == 3:
            row = SingleRow(split[0], int(split[1]), int(split[2]))
            keys.add(split[0])
            df1.append(row)

trees = {}
# create trees
start_tree = time.time()
structures = {}
for key in keys:
    structures[key] = AIList()


for row in df1:
    structures[row.chromosome].add(row.start, row.end)

for key in keys:
    structures[key].construct()


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
start_overlap = time.time()
for row in df2:
    if row.chromosome in structures:
        tree = structures[row.chromosome]
        count += sum(1 for _ in tree.intersect(row.start, row.end))
end_overlap = time.time()
print("                  count=" + str(count))
overlapping_execution = round(end_overlap - start_overlap, 2)
print("       Overlapping time=" + str(overlapping_execution))
print("                    Sum=" + str(tree_execution + overlapping_execution))

