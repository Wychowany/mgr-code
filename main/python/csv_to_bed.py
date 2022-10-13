from csv import reader

# open file in read mode
with open('/Users/olek/Desktop/magisterka/bed_streamed.bed', 'w') as f:
    with open(
            '/Users/olek/Desktop/magisterka/test-datasets/big_tree/0.2/streamed_dataset/part-00001-cb726b45-65ca-4c29-9e7e-f1ee871d4889-c000.csv',
            'r') as read_obj:
        csv_reader = reader(read_obj)
        for row in csv_reader:
            f.write("\t".join(row))
            f.write('\n')
