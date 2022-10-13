import xlsxwriter
import os
import matplotlib.pyplot as plt

fractions = [10, 50, 100, 200, 500, 1000, 2000, 4000, 8000, 10000]
fractions.reverse()
structures = {
    "iit": "com.nuschele.structures.speedup.CustomTree",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
}

avgBuilds = {
    "iit": [],
    "sequila": [],
}
avgIntersections = {
    "iit": [],
    "sequila": [],
}

avgSums = {
    "iit": [],
    "sequila": []
}

excel_path = "/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-full/no_spark_results.xlsx"
if os.path.exists(excel_path):
    os.remove(excel_path)

workbook = xlsxwriter.Workbook(excel_path)
precision = workbook.add_format({'num_format': '0.00'})

bold_format = workbook.add_format()
bold_format.set_bold(True)

colors = ['#D6CDEA', '#F9D8D6']
color_formats = []
for color in colors:
    color_formats.append(workbook.add_format({'bg_color': color}))
worksheet = workbook.add_worksheet("tests")
buildCols = ['D', 'E', 'F', 'G', 'H']
avgBuildCol = 'I'
intersectionCols = ['J', 'K', 'L', 'M', 'N']
avgIntersectionCol = 'O'
sumCols = ['P', 'Q', 'R', 'S', 'T']
avgSumCol = 'U'
recordsCol = 'A'
fractionCol = 'B'
structureCol = 'C'
countCol = 'V'
worksheet.write(avgBuildCol + '1', "Avg Build", bold_format)
worksheet.write(avgIntersectionCol + '1', "Avg intersec", bold_format)
worksheet.write(avgSumCol + '1', "Avg sum", bold_format)
worksheet.write(fractionCol + '1', 'fraction')
worksheet.write(structureCol + '1', 'structure')
worksheet.write(recordsCol + '1', 'Tree size')
for idx, col in enumerate(buildCols):
    worksheet.write(col + '1', 'Build({})'.format(idx + 1))

for idx, col in enumerate(intersectionCols):
    worksheet.write(col + '1', 'Intersec({})'.format(idx + 1))

for idx, col in enumerate(sumCols):
    worksheet.write(col + '1', 'Sum({})'.format(idx + 1))

worksheet.write(countCol + '1', "count")


def buildIndex(structure):
    if structure == "iitii" or structure == "iitii_no_intepolation":
        return 2
    else:
        return 0
numberOfRuns = 1

row = 2
for fraction in fractions:
    for sc in structures:
        worksheet.set_row(row - 1, cell_format=color_formats[int((row - 2) / 3) % 2])
        worksheet.write(row - 1, 0, "")
        worksheet.write(fractionCol + str(row), fraction)
        worksheet.write(structureCol + str(row), sc)
        count = None
        buildSum = 0
        interSecSum = 0
        avgSumSum = 0
        for run in range(1, numberOfRuns + 1):
            logFile = "/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-full/{}/{}_{}_{}.txt".format(
                str(fraction), sc,
                str(run),
                str(fraction))
            with open(logFile) as f:
                worksheet.write(recordsCol + str(row), str(int(fraction * 21000)) + "k")
                lines = f.readlines()
                buildTimeLineIndex = buildIndex(sc)
                structureBuildTime = float(lines[buildTimeLineIndex].strip().split('=')[1])
                buildSum += structureBuildTime
                worksheet.write_number(buildCols[run - 1] + str(row), structureBuildTime)
                count = lines[buildTimeLineIndex + 1].split("=")[1]
                intersectionTime = float(lines[buildTimeLineIndex + 2].split('=')[1])
                interSecSum += intersectionTime
                avgSumSum += intersectionTime + structureBuildTime
                worksheet.write_number(sumCols[run - 1] + str(row), intersectionTime + structureBuildTime)
                worksheet.write_number(intersectionCols[run - 1] + str(row), intersectionTime)

        worksheet.write_number(countCol + str(row), int(count))
        avgBuild = float(buildSum / numberOfRuns)
        if sc in avgBuilds:
            avgBuilds[sc].append(avgBuild)
        worksheet.write_number(avgBuildCol + str(row), avgBuild, precision)
        avgIntersection = float(interSecSum / numberOfRuns)
        if sc in avgIntersections:
            avgIntersections[sc].append(avgIntersection)
        worksheet.write_number(avgIntersectionCol + str(row), avgIntersection, precision)
        avgSum = float(avgSumSum / numberOfRuns)
        if sc in avgSums:
            avgSums[sc].append(avgSum)
        worksheet.write(avgSumCol + str(row), avgSum, precision)
        row += 1
workbook.close()


def generateFigure(title, xlabel, ylabel, fileName, data):
    plt.clf()
    plt.title(title)
    plt.xlabel(xlabel)
    plt.xscale('log')
    plt.ylabel(ylabel)
    for sc in structures:
        if sc in data:
            plt.plot(fractions, data[sc], label=sc)
    plt.legend(loc='upper left')
    plt.savefig(fileName)

operations = {
    "iit": [5868566, 23489582, 40742916, 71759070, 161161064, 309346671,605523599, 1197716633, 2382042141, 2973910539],
    "sequila": [201545161, 224965924, 254228283, 312707329,488155506, 780658185, 1365623230,2535412564, 4875173124, 6044917430],
}
generateFigure('Wyszukiwanie przecieć', 'długość przedziału', 'sekundy [s]', 'intersection_time.png', avgIntersections)
# generateFigure('Tworzenie struktury', 'część zbioru danych uni', 'sekundy[s]', 'build_time.png', avgBuilds)
# generateFigure('Tworzenie struktury + wyszukiwanie przecięć','część zbioru danych uni', 'sekundy[s]', 'avg_time.png', avgSums)

