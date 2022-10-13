import os
import time

structures = {
    "iit": "com.nuschele.structures.dropright.CustomTree",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
}

fractions = [10, 50, 100, 200, 500, 1000, 2000, 4000, 8000, 10000]
for fraction in fractions:
    os.mkdir("/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-operations/" + str(fraction))

runs = 1

for fraction in fractions:
    test_tree = "/Users/aleksandernuszel/Desktop/private/magisterka/interval-length/" + str(fraction) + "/tree.csv"
    for sc in structures:
        for run in range(1, runs + 1):
            print("Running " + str(fraction) + "/" + str(sc) + "/" + str(run))
            # time.sleep(5)
            output_name = "/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-operations/" + str(
                fraction) + "/" + sc + "_" + str(
                run) + "_" + str(fraction) + ".txt"
            java_command = "export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home;" \
                           "cd /Users/aleksandernuszel/Desktop/private/magisterka/SparkTests/target/scala-2.12; " \
                           "java -Xms6G -Xmx16G -cp SparkTest-assembly-0.1.jar com.nuschele.NoSparkTest {} {} {} > {}".format(
                test_tree, test_tree, structures[sc], output_name)
            os.system(java_command)
