//package com.nuschele.structures.custom;
//
//import org.biodatageeks.sequila.rangejoins.methods.base.BaseIntervalHolder;
//import org.biodatageeks.sequila.rangejoins.methods.base.BaseNode;
//import org.jetbrains.annotations.NotNull;
//import scala.Option;
//
//import java.util.*;
//
//public class CustomTree<V> implements BaseIntervalHolder<V> {
//
//	public final ArrayList<CustomNode<V>> intervals = new ArrayList<>();
//	private int K;
//	private int rootIndex;
//
//	@Override
//	public V put(int start, int end, V value) {
//		intervals.add(new CustomNode<>(start, end, value));
//		return value;
//	}
//
//	@Override
//	public V remove(int start, int end) {
//		return null;
//	}
//
//	@Override
//	public BaseNode<V> find(int start, int end) {
//		return null;
//	}
//
//	@Override
//	public Iterator<BaseNode<V>> overlappers(int start, int end) {
//		return new CustomIterator(start, end);
//	}
//
//	void augmentTree() {
//		recurAugmentMax(rootIndex, K);
//	}
//
//	int recurAugmentMax(int nodeIndex, int level) {
//		if (level == 0 && nodeIndex <= intervals.size() - 1) {
//			// regular leaf
//			intervals.get(nodeIndex).max = intervals.get(nodeIndex).getEnd();
//			return intervals.get(nodeIndex).max;
//		} else if (level == 0 && nodeIndex > intervals.size() - 1) {
//			// imaginary leaf
//			return -1;
//		} else if (level > 0 && nodeIndex <= intervals.size() - 1) {
//			// only right can be imaginary here but can return some value
//			int leftSubtreeMax = recurAugmentMax(nodeIndex - (int) Math.pow(2, level - 1), level - 1);
//			int rightSubtreeMax = recurAugmentMax(nodeIndex + (int) Math.pow(2, level - 1), level - 1);
//			intervals.get(nodeIndex).max = Math.max(Math.max(leftSubtreeMax, rightSubtreeMax), intervals.get(nodeIndex).getEnd());
//			return intervals.get(nodeIndex).max;
//		} else if (level > 0 && nodeIndex > intervals.size() - 1) {
//			// imaginary node, it makes sens to analyze only left subtree cos right is always imaginary
//			return recurAugmentMax(nodeIndex - (int) Math.pow(2, level - 1), level - 1);
//		}
//		throw new RuntimeException("Fix implementation");
//	}
//
//	@Override
//	public void postConstruct(Option<Object> domains) {
//		intervals.sort(new CustomNodeComparator());
//		TreeMetadata metadata = MetadataResolver.resolveMetadata(intervals.size());
//		K = metadata.K;
//		rootIndex = metadata.rootIndex;
//		augmentTree();
//	}
//
//	@NotNull
//	@Override
//	public Iterator<BaseNode<V>> iterator() {
//		return null;
//	}
//
//	public class CustomIterator implements Iterator<BaseNode<V>> {
//
//		final private int qStart;
//		final private int qEnd;
//		private int qNext;
//		int globalLevel = K;
//
//		public CustomIterator(int qStart, int qEnd) {
//			this.qStart = qStart;
//			this.qEnd = qEnd;
//			this.qNext = leastOverlap(rootIndex, globalLevel);
//		}
//
//		@Override
//		public CustomNode<V> next() {
//			int previous = qNext;
//			qNext = nextOverlapper();
//			return intervals.get(previous);
//		}
//
//
//		private boolean isImaginary(int nodeIndex) {
//			return nodeIndex > intervals.size() - 1;
//		}
//
//		@Override
//		public boolean hasNext() {
//			return qNext != -1;
//		}
//
//		private int nextOverlapper() {
//			int nodeIndex = qNext;
//			if (nodeIndex == -1) {
//				throw new RuntimeException("Something wrong");
//			}
//			// w ogole jest sens sprawdzania prawego dziecka
//			if (intervals.get(nodeIndex).max >= qStart) {
//				int rightIndex = TreeOperations.right(nodeIndex, globalLevel);
//				globalLevel--;
//				if (globalLevel == -1) {
//					return -1;
//				} else if (isImaginary(rightIndex)) {
//					nodeIndex = rightIndex;
//					while (isImaginary(nodeIndex)) {
//						nodeIndex = TreeOperations.left(nodeIndex, globalLevel);
//						globalLevel--;
//					}
//					// that function updates globalLevel inside
//					return leastOverlap(nodeIndex, globalLevel);
//				} else {
//					return -1;
////					return leastOverlap(nodeIndex, globalLevel);
//				}
//				// nie ma sensu, idziemy w gore
//			} else {
//				// Jesli to prawe dziecko to znaczy ze rodzic juz byl odwiedzony i sprawdzony, idziemy w gore TODO, moze cos byc zle
//				while (TreeOperations.isRightChild(nodeIndex, globalLevel)) {
//					nodeIndex = TreeOperations.parent(nodeIndex, globalLevel);
//					globalLevel++;
//				}
//				if (nodeIndex == rootIndex) {
//					return -1;
//				}
//
//				// lewe dziecko
//				nodeIndex = TreeOperations.parent(nodeIndex, globalLevel);
//				globalLevel++;
//				if (isImaginary(nodeIndex)) {
//					return -1;
//				} else {
//					// ten node overlapuje wiec jest nastepny
//					if (qStart <= intervals.get(nodeIndex).getEnd() && qEnd >= intervals.get(nodeIndex).getStart()) {
//						return nodeIndex;
//
//						// nie jest ten node, ale jest sens isc w glab
//					} else if (intervals.get(nodeIndex).max >= qStart) {
//						nodeIndex = TreeOperations.right(nodeIndex,globalLevel);
//						globalLevel--;
//						while (isImaginary(nodeIndex)) {
//							nodeIndex = TreeOperations.left(nodeIndex, globalLevel);
//							globalLevel--;
//						}
//						return leastOverlap(nodeIndex, globalLevel);
//					} else {
//						return -1;
//					}
//				}
//			}
//		}
//
//
//		private int leastOverlap(int from, int fromLevel) {
//			int nodeIndex = from;
//			int result = -1;
//			if (intervals.get(nodeIndex).max < qStart) {
//				return -1;
//			}
//			while (true) {
//				if (intervals.get(nodeIndex).overlaps(qStart,qEnd)) {
//					result = nodeIndex;
//					nodeIndex = TreeOperations.left(nodeIndex, fromLevel);
//					fromLevel--;
//					if (fromLevel == -1 || intervals.get(nodeIndex).max < qStart)
//						break;
//				} else {
//					int left = TreeOperations.left(nodeIndex, fromLevel);
//					if (left != -1 && intervals.get(left).max >= qStart) {
//						nodeIndex = left;
//						fromLevel--;
//					} else {
//						if (intervals.get(nodeIndex).getStart() > qEnd) {
//							break;
//						}
//						nodeIndex = TreeOperations.right(nodeIndex, fromLevel);
//						fromLevel--;
//
//						if (fromLevel == -1) {
//							break;
//						}
//
//						while (isImaginary(nodeIndex)) {
//							nodeIndex = TreeOperations.left(nodeIndex, fromLevel);
//							fromLevel--;
//						}
//
//						if (intervals.get(nodeIndex).max < qStart) {
//							break;
//						}
//					}
//				}
//			}
//			globalLevel = fromLevel;
//			return result;
//		}
//	}
//
//	public static class CustomNode<V> extends BaseNode<V> {
//		private int start;
//		private int end;
//		int max = -1;
//		private ArrayList<V> values = new ArrayList<>(1);
//
//		@Override
//		public int getStart() {
//			return start;
//		}
//
//		@Override
//		public int getEnd() {
//			return end;
//		}
//
//		@Override
//		public ArrayList<V> getValue() {
//			return values;
//		}
//
//		public CustomNode(int start, int end, V value) {
//			this.start = start;
//			this.end = end;
//			this.values.add(value);
//		}
//
//		boolean overlaps(final int qStart, final int qEnd) {
//			return this.start <= qEnd && this.end >= qStart;
//		}
//
//	}
//
//	public class CustomNodeComparator implements Comparator<CustomNode<V>> {
//		@Override
//		public int compare(CustomNode o1, CustomNode o2) {
//			int result = 0;
//
//			if (o1.getStart() > o2.getStart())
//				result = 1;
//			else if (o1.getStart() < o2.getStart())
//				result = -1;
//			else if (o1.getEnd() > o2.getEnd())
//				result = 1;
//			else if (o1.getEnd() < o2.getEnd())
//				result = -1;
//			return result;
//		}
//	}
//}
