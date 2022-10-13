START=$(date +%s.%n)
/Users/olek/Desktop/cgranges/test/bedcov-cr /Users/olek/Desktop/AIListTestData/ex-rna.bed /Users/olek/Desktop/AIListTestData/ex-anno.bed -c | gawk -F'\t' '{ sum += $4 } END{ print sum }'
END=$(date +%s.%n)
DIFF=$(echo "$END - $START" | bc)

print "$DIFF"