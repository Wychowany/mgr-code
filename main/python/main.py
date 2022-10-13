import os
import sys

left_tables = ["fBrain-DS14718.bed", "chainOrnAna1.bed",
               "chainRn4.bed", "chainVicPac2.bed", "chainXenTro3Link.bed",
               "ex-anno.bed", "ex-rna.bed", "exons.bed", "chainMonDom5Link.bed"]
right_tables = ["chainOrnAna1.bed", "chainRn4.bed", "ex-anno.bed", "exons.bed", "fBrain-DS14718.bed"]
classes = ["org.biodatageeks.tbd.NCList",
           "org.biodatageeks.tbd.IITree",
           "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
           "org.biodatageeks.tbd.AIList"]
count = (len(left_tables) * len(right_tables) - len(list(set(left_tables) & set(right_tables)))) * len(classes)
i = 1
for x in left_tables:
    for y in right_tables:
        for clazz in classes:
            if i == 4:
                sys.exit()
            if x != y:
                print("Running " + str(i) + "/" + str(count))
                i = i + 1
                output_name = x + "_" + y + "_" + clazz + ".txt"

                java_command = "cd /Users/olek/Desktop/spark-3.0.1-bin-hadoop2.7/bin; ./spark-submit --driver-memory 6g --class org.biodatageeks.tbd.Benchmark /Users/olek/Desktop/deploy_mgr/tbd-example-pub/target/scala-2.12/tbd-example-project-assembly-0.0.1.jar --left-table-path=/Users/olek/Desktop/AIListTestData/{} --left-table-name=left_table --right-table-path=/Users/olek/Desktop/AIListTestData/{} --right-table-name=right_table --interval-holder-class={} > /Users/olek/Desktop/tests/{}".format(
                    x, y, clazz, output_name
                )
                os.system(java_command)
