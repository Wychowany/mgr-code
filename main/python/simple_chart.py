import matplotlib.pyplot as plt

fractions = [0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55]
structures = {
    "iit": "com.nuschele.structures.custom.CustomTree",
    "ailist": "org.biodatageeks.sequila.rangejoins.exp.ailist.AIList",
    "sequila": "org.biodatageeks.sequila.rangejoins.methods.IntervalTree.IntervalTreeRedBlack",
    "nclist": "org.biodatageeks.sequila.rangejoins.exp.nclist.NCList"
}

numberOfRuns = 1

broadcasts = {
    "iit": [],
    "ailist": [],
    "sequila": [],
    "nclist": []
}

totals = {
    "iit": [],
    "ailist": [],
    "sequila": [],
    "nclist": []
}

for fraction in fractions:
    for sc in structures:
        for run in range(1, numberOfRuns + 1):
            logFile = "/Users/aleksandernuszel/Desktop/private/magisterka/benchmark/final-uniform-long/{}/{}_{}_{}.txt".format(
                str(fraction), sc,
                str(run),
                str(fraction))
            with open(logFile) as f:
                lines = f.readlines()
                broadcast = int(lines[1].split(" ")[2]) / 1000
                total = int(lines[8].split(" ")[2]) / 1000
                broadcasts[sc].append(broadcast)
                totals[sc].append(total)


def generateFigure(title, xlabel, ylabel, fileName, data):
    plt.clf()
    plt.title(title)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    for sc in structures:
        if sc in data:
            plt.plot(fractions, data[sc], label=sc)
    plt.legend(loc='upper left')
    plt.savefig(fileName)


generateFigure('Rozgłaszanie struktury', 'część zbioru danych uni', 'sekundy [s]', 'broadcast_time_uni.png',
               broadcasts)
generateFigure('Całkowity czas złączenia', 'część zbioru danych uni', 'sekundy[s]', 'total_time_uni.png', totals)
