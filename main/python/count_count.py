file = open("/Users/olek/Desktop/logs/iitii_cost.txt", "r")
total_count = 0
for line in file:
    if "COST:" in line:
        total_count += int(line.split(":")[1])

print(total_count)
file.close()