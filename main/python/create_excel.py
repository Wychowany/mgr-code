import xlsxwriter
import os
import requests
import datetime

fractions = [0.018, 0.016, 0.014, 0.012, 0.010, 0.008, 0.006, 0.004, 0.002, 0.001]

structures = {
    "iitii_no_intepolation": "org.biodatageeks.sequila.rangejoins.exp.iitii.ImplicitIntervalTreeWithInterpolationIndex --domains-num=-1",
    "iitii": "org.biodatageeks.sequila.rangejoins.exp.iitii.ImplicitIntervalTreeWithInterpolationIndex",
    "iitree": "org.biodatageeks.sequila.rangejoins.exp.iit.IITree",
    "ailist": "org.biodatageeks.sequila.rangejoins.exp.ailist.AIList",
    "nclist": "org.biodatageeks.sequila.rangejoins.exp.nclist.NCList",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack"
}

excel_path = "/Users/olek/Desktop/magisterka/benchmark/small_tree/results.xlsx"
if os.path.exists(excel_path):
    os.remove(excel_path)

workbook = xlsxwriter.Workbook(excel_path)

bold_format = workbook.add_format()
bold_format.set_bold(True)

colors = ['#D6CDEA', '#F9D8D6']
color_formats = []
for color in colors:
    color_formats.append(workbook.add_format({'bg_color': color}))
worksheet = workbook.add_worksheet("tests")
buildCols = ['D', 'E', 'F', 'G', 'H']
avgBuildCol = 'I'
broadcastCols = ['J', 'K', 'L', 'M', 'N']
avgBroadcastCol = 'O'
intersectionCols = ['P', 'Q', 'R', 'S', 'T']
avgIntersectionCol = 'U'
joinDurationCols = ['V', 'W', 'X', 'Y', 'Z']
avgJoinDuration = 'AA'
recordsCol = 'A'
fractionCol = 'B'
structureCol = 'C'
worksheet.write(avgBuildCol + '1', "Avg Build", bold_format)
worksheet.write(avgIntersectionCol + '1', "Avg intersec", bold_format)
worksheet.write(avgJoinDuration + '1', "Avg Join dur", bold_format)
worksheet.write(avgBroadcastCol + '1', "Avg Broadcast", bold_format)
worksheet.write(fractionCol + '1', 'fraction')
worksheet.write(structureCol + '1', 'structure')
for idx, col in enumerate(buildCols):
    worksheet.write(col + '1', 'Build({})'.format(idx + 1))

for idx, col in enumerate(broadcastCols):
    worksheet.write(col + '1', 'Broadcast({})'.format(idx + 1))

for idx, col in enumerate(intersectionCols):
    worksheet.write(col + '1', 'Intersec({})'.format(idx + 1))

for idx, col in enumerate(joinDurationCols):
    worksheet.write(col + '1', 'Join dur({})'.format(idx + 1))

countCol = 'AB'
broadcastSizeCol = 'AC'
gcTimeCol = 'AD'
worksheet.write(countCol + '1', "count")
worksheet.write(broadcastSizeCol + '1', "Broadcast size")
worksheet.write(gcTimeCol + '1', "avgGC")


def buildIndex(structure):
    if structure == "iitii" or structure == "iitii_no_intepolation":
        return 3
    else:
        return 1


numberOfRuns = 5

row = 2
for fraction in fractions:
    for sc in structures:
        worksheet.set_row(row - 1, cell_format=color_formats[int((row - 2) / 6) % 2])
        worksheet.write(row - 1, 0, "")
        worksheet.write(fractionCol + str(row), fraction)
        worksheet.write(structureCol + str(row), sc)
        estimatedSize = None
        count = None
        buildSum = 0
        broadcastSum = 0
        interSecSum = 0
        joinDurSum = 0
        gcTimeSum = 0
        for run in range(1, numberOfRuns + 1):
            logFile = "/Users/olek/Desktop/magisterka/benchmark/small_tree/{}/{}_{}_{}.txt".format(str(fraction), sc,
                                                                                                   str(run),
                                                                                                   str(fraction))
            with open(logFile) as f:
                worksheet.write(recordsCol + str(row), str(int(fraction * 21000)) + "k")
                lines = f.readlines()
                appId = lines[0].strip().split('=')[1]
                buildTimeLineIndex = buildIndex(sc)
                structureBuildTime = int(lines[buildTimeLineIndex].strip().split(' ')[2])
                buildSum += structureBuildTime
                worksheet.write_number(buildCols[run - 1] + str(row), structureBuildTime)
                estimatedSize = lines[buildTimeLineIndex + 1].split('=')[1]
                broadcastTime = lines[buildTimeLineIndex + 2].split(' ')[2]
                worksheet.write_number(broadcastCols[run - 1] + str(row), int(broadcastTime))
                broadcastSum += int(broadcastTime)
                count = lines[buildTimeLineIndex + 6].split("|")[1]
                appResult = requests.get('http://localhost:18080/api/v1/applications/{}'.format(appId)).json()
                worksheet.write_number(joinDurationCols[run - 1] + str(row), int(appResult['attempts'][0]['duration']))
                joinDur = int(lines[buildTimeLineIndex + 9].split(' ')[2])
                joinDurSum += joinDur
                worksheet.write_number(joinDurationCols[run - 1] + str(row), joinDur)
                intersectionJobResult = \
                requests.get('http://localhost:18080/api/v1/applications/{}/jobs'.format(appId)).json()[0]
                subTime = datetime.datetime.strptime(intersectionJobResult['submissionTime'], '%Y-%m-%dT%H:%M:%S.%fGMT')
                compTime = datetime.datetime.strptime(intersectionJobResult['completionTime'],
                                                      '%Y-%m-%dT%H:%M:%S.%fGMT')
                delta = compTime - subTime
                mili = delta.seconds * 1000 + delta.microseconds * 0.001
                interSecSum += mili
                worksheet.write_number(intersectionCols[run - 1] + str(row), mili)
                gcTimeSum += int(requests.get('http://localhost:18080/api/v1/applications/{}/executors'.format(appId)).json()[0]['totalGCTime'])
        worksheet.write_number(broadcastSizeCol + str(row), int(estimatedSize))
        worksheet.write_number(countCol + str(row), int(count))
        worksheet.write_number(avgBroadcastCol + str(row), int(broadcastSum/numberOfRuns), bold_format)
        worksheet.write_number(avgJoinDuration + str(row), int(joinDurSum/numberOfRuns), bold_format)
        worksheet.write_number(avgBuildCol + str(row), int(buildSum/numberOfRuns), bold_format)
        worksheet.write_number(avgIntersectionCol + str(row), int(interSecSum/numberOfRuns), bold_format)
        worksheet.write_number(gcTimeCol + str(row), int(gcTimeSum/numberOfRuns),bold_format)
        row += 1
workbook.close()
