import os
import xlsxwriter


class SparkTest:
    file_name = None
    left_table = None
    right_table = None
    structure = None
    run_time = None
    result_count = None
    failed = False

    def __init__(self, file_name, left_table, right_table, structure):
        self.file_name = file_name
        self.left_table = left_table
        self.right_table = right_table
        self.structure = structure


genetic_files = ["fBrain-DS14718.bed", "chainOrnAna1.bed",
                 "chainRn4.bed", "chainVicPac2.bed", "chainXenTro3Link.bed",
                 "ex-anno.bed", "ex-rna.bed", "exons.bed", "chainMonDom5Link.bed"]

structures = ["NCList", "IITree", "AIList", "IntervalTreeRedBlack"]
files = filter(lambda file_name: ".txt" in file_name, os.listdir("/Users/olek/Desktop/tests"))
tests = map(
    lambda file_name: SparkTest(file_name, file_name.split("_")[0], file_name.split("_")[1], file_name.split("_")[2]),
    files)
tests = list(tests)
for i in range(len(tests)):
    test = tests[i]
    full_path = "/Users/olek/Desktop/tests/" + test.file_name
    if os.path.getsize(full_path) == 0:
        test.failed = True
    else:
        with open(full_path) as f:
            for line in f:
                if "|" in line and "cnt" not in line:
                    test.result_count = int(line.split("|")[1])
                if "elapsedTimeMeasured" in line:
                    test.run_time = int(line.split(":")[1])
    tests[i] = test

excel_path = "/Users/olek/Desktop/excel/results.xlsx"
if os.path.exists(excel_path):
    os.remove(excel_path)

workbook = xlsxwriter.Workbook(excel_path)
cell_format = workbook.add_format()
cell_format.set_pattern(1)
cell_format.set_bg_color('green')

bold_format = workbook.add_format()
bold_format.set_pattern(2)
bold_format.set_bold(True)

worksheet = workbook.add_worksheet("spark-tests")
worksheet.write('B1', 'left_table')
worksheet.write('C1', 'right_table')
worksheet.write('D1', structures[0])
worksheet.write('E1', structures[1])
worksheet.write('F1', structures[2])
worksheet.write('G1', structures[3])
worksheet.write('H1', "result_count")
row = 2


def fill_run_time(row, column, run_time, worksheet, failed, fastest):
    if failed is True:
        worksheet.write(column + str(row), "FAILED")
    else:
        if fastest is True:
            worksheet.write(column + str(row), run_time, cell_format)
        else:
            worksheet.write(column + str(row), run_time)


for left_table in genetic_files:
    for right_table in genetic_files:
        results_for_pair_of_datasets = list(
            filter(lambda single_test: single_test.left_table == left_table and single_test.right_table == right_table,
                   tests))
        worksheet.write('B' + str(row), left_table)
        worksheet.write('C' + str(row), right_table)
        for x in results_for_pair_of_datasets:
            found_faster = False
            if x.failed is not True:
                for y in results_for_pair_of_datasets:
                    if x.structure != y.structure and y.run_time < x.run_time:
                        found_faster = True
            if structures[0] in x.structure:
                fill_run_time(row, "D", x.run_time, worksheet, x.failed, not found_faster)

            if structures[1] in x.structure:
                fill_run_time(row, "E", x.run_time, worksheet, x.failed, not found_faster)

            if structures[2] in x.structure:
                fill_run_time(row, "F", x.run_time, worksheet, x.failed, not found_faster)

            if structures[3] in x.structure:
                fill_run_time(row, "G", x.run_time, worksheet, x.failed, not found_faster)

        if len(results_for_pair_of_datasets) > 0 and not results_for_pair_of_datasets[0].failed:
            worksheet.write("H" + str(row), results_for_pair_of_datasets[0].result_count, bold_format)
        row = row + 1
workbook.close()
