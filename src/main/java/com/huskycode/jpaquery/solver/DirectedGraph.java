package com.huskycode.jpaquery.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DirectedGraph<T> {
	Map<NodeWarper<T>, List<NodeWarper<T>>> childrenMap = new HashMap<NodeWarper<T>, List<NodeWarper<T>>>();
	Map<NodeWarper<T>, List<NodeWarper<T>>> parentMap = new HashMap<NodeWarper<T>, List<NodeWarper<T>>>();
	Set<NodeWarper<T>> allNodes = new HashSet<NodeWarper<T>>();
	Map<NodeWarper<T>, NodeWarper<T>> actualNodesMap = new HashMap<NodeWarper<T>, NodeWarper<T>>();
	private DirectedGraph() {}
	
	public static <T> DirectedGraph<T> newInstance() {
		return new DirectedGraph<T>();
	}
	
	public void addNode(T node) {
		allNodes.add(getOrCreateNode(node));
	}
	
	public void addRelation(T from, T to) {
		NodeWarper<T> child = getOrCreateNode(from);
		NodeWarper<T> parent = getOrCreateNode(to);
		getOrCreate(childrenMap, parent).add(child);
		getOrCreate(parentMap, child).add(parent);
		allNodes.add(child);
		allNodes.add(parent);
	}
	
	public void computeNodeLevel() {
		int max = allNodes.size();
		int count = 0;
		while(count++ < max) {
			boolean noChange = true;
			for (NodeWarper<T> node : parentMap.keySet()) {
				int maxParentLevel = getMaxLevelOfParent(node);
				int toBeNodeLevel = maxParentLevel + 1;
				if (toBeNodeLevel > node.getLevel()) {
					node.setLevel(toBeNodeLevel);
					noChange = false;
				}
			}
			
			if (noChange) {
				break;
			}
		}
	}
	
	private NodeWarper<T> getOrCreateNode(T t) {
		NodeWarper<T> key = NodeWarper.newInstance(t);
		NodeWarper<T> value = actualNodesMap.get(key);
		if (value == null) {
			value = key;
			actualNodesMap.put(key, value);
		}
		return value;
	}
	
	public List<T> getInorderNodeAscendent() {
		Comparator<Node<T>> comparator = NodeComparatorAsc.getInstance();
		return getInorderNode(comparator);
	}
	
//	public List<T> getInorderNodeAscendent(Comparator<T> tieBreaker) {
//		Comparator<Node<T>> comparatorAsc = NodeComparatorAsc.getInstance();
//		Comparator<Node<T>> comparator = NodeComparatorWithTieBreaker.newInstance(comparatorAsc, tieBreaker);
//		return getInorderNode(comparator);
//	}
//	
	public List<T> getInorderNodeDescendent() {
		Comparator<Node<T>> comparator = NodeComparatorDesc.getInstance();
		return getInorderNode(comparator);
	}
	
//	public List<T> getInorderNodeDescendent(Comparator<T> tieBreaker) {
//		Comparator<Node<T>> comparatorDesc = NodeComparatorDesc.getInstance();
//		Comparator<Node<T>> comparator = NodeComparatorWithTieBreaker.newInstance(comparatorDesc, tieBreaker);
//		return getInorderNode(comparator);
//	}
	
	@SuppressWarnings("unchecked")
	private List<T> getInorderNode(Comparator<Node<T>> comparator) {
		NodeWarper<T>[] arrayData = allNodes.toArray(new NodeWarper[0]);
		Arrays.sort(arrayData, comparator);
		return toList(arrayData);
	}
	
	private int getMaxLevelOfParent(NodeWarper<T> node) {
		int max = -1;
		for (NodeWarper<T> parent : parentMap.get(node)) {
			if (max < parent.getLevel()) {
				max = parent.getLevel();
			}
		}
		return max;
	}
	
	private List<T> toList(NodeWarper<T>[] data) {
		List<T> result = new ArrayList<T>(data.length);
		for (NodeWarper<T> node : data) {
			result.add(node.get());
		}
		
		return result;
	}
	
	private  <K, V> List<V> getOrCreate(Map<K, List<V>> map, K key) {
		List<V> value = map.get(key);
		if (value == null) {
			value = new ArrayList<V>();
			map.put(key, value);
		}
		return value;	
	}

	private static class NodeComparatorWithTieBreaker<T> implements Comparator<Node<T>> {

		private Comparator<Node<T>> theCompator;
		private Comparator<T> theTieBreaker;
		
		private NodeComparatorWithTieBreaker(Comparator<Node<T>> theCompator, Comparator<T> theTieBreaker) {
			this.theCompator = theCompator;
			this.theTieBreaker = theTieBreaker;
		}
		
		public static <T> NodeComparatorWithTieBreaker<T> newInstance(Comparator<Node<T>> theCompator, Comparator<T> theTieBreaker) {
			return new NodeComparatorWithTieBreaker<T>(theCompator, theTieBreaker);
		}
		
		@Override
		public int compare(Node<T> o1, Node<T> o2) {
			int value = theCompator.compare(o1, o2);
			if (value == 0) {
				value = theTieBreaker.compare(o1.get(), o2.get());
			}
			return value;
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	private static class NodeComparatorAsc<T> implements Comparator<Node<T>> {
		
		private static final NodeComparatorAsc INSTANCE = new NodeComparatorAsc();
		
		@SuppressWarnings("unchecked")
		public static final <T> Comparator<Node<T>> getInstance() {
			return INSTANCE;
		}
		
		@Override
		public int compare(Node<T> o1, Node<T> o2) {
			return o1.getLevel() - o2.getLevel();
		}	
	}
	
	@SuppressWarnings("rawtypes")
	private static class NodeComparatorDesc<T> implements Comparator<Node<T>> {

		private static final NodeComparatorDesc INSTANCE = new NodeComparatorDesc();
		
		@SuppressWarnings("unchecked")
		public static final <T> Comparator<Node<T>> getInstance() {
			return INSTANCE;
		}
		
		@Override
		public int compare(Node<T> o1, Node<T> o2) {
			Comparator<Node<T>> ascComparator = NodeComparatorAsc.getInstance();
			return -ascComparator.compare(o1, o2);
		}	
	}
	
	interface Node<T> {
		int getLevel();
		T get();
	}
	
	private static class NodeWarper<T> implements Node<T> {
		private T instance;
		private int level;
		
		private NodeWarper(T t) {
			this.instance = t;
		}
		
		public static <T> NodeWarper<T> newInstance(T t) {
			return new NodeWarper<T>(t);
		}

		public T get() {
			return instance;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getLevel() {
			return level;
		}

		@Override
		public int hashCode() {
			return instance.hashCode();
		}

		@Override
		public String toString() {
			return "NodeWarper [instance=" + instance + ", level=" + level
					+ "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			NodeWarper<T> other = (NodeWarper<T>) obj;
			return (this.instance == other.instance);
		} 
	}
}
