file = open("/Users/olek/Desktop/logs/iitii_cost.txt", "r")
line_count = 0
lines =[]
lines2 = []
for line in file:
    lines.append(line)
file.close()

file = open("/Users/olek/Desktop/logs/iitii_cost_10_dif.txt", "r")
for line in file:
    lines2.append(line)
file.close()

print(len(lines))
lines.sort()
print(len(lines2))
lines2.sort()


for i in range(len(lines)):
    print(lines[i])
    print(lines2[i])
print(line_count)
import pandas

df = pandas.read_csv('/Users/olek/Downloads/wiki_prices.csv')
print(df.count())