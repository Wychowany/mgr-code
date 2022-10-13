import os

structures = {
    "iit": "com.nuschele.structures.speedup.CustomTree",
    "ailist": "org.biodatageeks.sequila.rangejoins.exp.ailist.AIList",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
    "nclist": "com.nuschele.structures.nclist.NCList"
}

fractions = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0]
fractions.reverse()
for fraction in fractions:
    os.mkdir("/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/serialization/" + str(fraction))
runs = 2
for fraction in fractions:
    for sc in structures:
        for run in range(1, runs + 1):
            output_name = str(fraction) + "/" + sc + "_" + str(run) + "_" + str(fraction) + ".txt"
            app_name = str(fraction) + "_" + sc + "_run_" + str(run)
            command = "cd /Users/olek/Desktop/spark-3.0.1-bin-hadoop2.7/bin;" \
                      "export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home;" \
                      " ./spark-submit  --driver-memory 10g" \
                      " --driver-class-path /Users/olek/Desktop/mgr-2022/TestMapInsteadOfSQL/target/scala-2.12/TestMapInsteadOfSQL-assembly-0.1.jar" \
                      " --class com.nuschele.SparkSingleClassSQLTest --conf spark.driver.maxResultSize=\"0\" --conf spark.app.name=\"{}\" /Users/olek/Desktop/mgr-2022/TestMapInsteadOfSQL/target/scala-2.12/TestMapInsteadOfSQL-assembly-0.1.jar " \
                      " --interval-holder-class={} " \
                      " --tree-table-path=/Users/olek/Desktop/magisterka/test-datasets/small_tree/{}/tree_dataset " \
                      " --dataset-table-path=/Users/olek/Desktop/magisterka/genomes.csv" \
                      " > /Users/olek/Desktop/magisterka/benchmark/small_tree-/{}".format(
                app_name, structures[sc], str(fraction), output_name)
            os.system(command)
