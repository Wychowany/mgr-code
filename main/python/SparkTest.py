import os
import time

structures = {
    "iit": "com.nuschele.structures.speedup.CustomTree",
    "ailist": "org.biodatageeks.sequila.rangejoins.exp.ailist.AIList",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
    "nclist": "com.nuschele.structures.nclist.NCList"
}

fractions = [0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6]
for fraction in fractions:
    os.mkdir("/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-spark-uni/" + str(fraction))

runs = 1
streamedDataset = "/Users/aleksandernuszel/Desktop/private/magisterka/uniform/1.0/tree.csv"
for fraction in fractions:
    test_tree = "/Users/aleksandernuszel/Desktop/private/magisterka/uniform/" + str(fraction) + "/tree.csv"
    for sc in structures:
        for run in range(1, runs + 1):
            print("Running " + str(fraction) + "/" + str(sc) + "/" + str(run))
            time.sleep(5)
            output_name = "/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-spark-uni/" + str(
                fraction) + "/" + sc + "_" + str(
                run) + "_" + str(fraction) + ".txt"
            java_command = "export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home;" \
                           "cd /Users/aleksandernuszel/Desktop/private/magisterka/SparkTests/target/scala-2.12; " \
                           "java -Xms10G -Xmx16G -cp SparkTest-assembly-0.1.jar com.nuschele.SparkSingleClassSQLTest" \
                           " --interval-holder-class={} " \
                           " --tree-table-path={} " \
                           " --dataset-table-path={}" \
                           " > {}".format(
                structures[sc], test_tree, streamedDataset , output_name)
            os.system(java_command)
