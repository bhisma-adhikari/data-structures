// This program implements the data-structure Priority Queue using min-heap.
// Priority queue is an abstract data structure. 

// Uses index table to reduce time complexity of
//    remove() from O(n) to O(log(n)).
//    contains() from O(n) to O(1)

// This, however, introduces constant overhead during element-addition. 

// Public Methods: 
// add(element)     --> void        O(log(n))   : adds element to the priority queue 
// remove(element)  --> boolean     O(log(n))   : removes element from the priority queue 
//                                                If the element occurs multiple times, the one 
//                                                at the lowest index will be removed.
// poll()           --> element     O(1)        : removes and returns root element; root element always 
//                                                has the highest priority for removal 
// peek()           --> element     O(1)        : returns root element 
// contains(element)--> boolean     O(1)        : checks if the element is contained in the PQ. Since we
//                                                are using an index table, the time complexity reduces to
//                                                constant time. 

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;
import java.util.HashMap;

class PriorityQueue<T extends Comparable> {
    private int heapSize; // current number of elements
    private List<T> heap;
    private Map<T, TreeSet<Integer>> indexTable = new HashMap<>();

    public PriorityQueue(T[] elements) {
        this.heap = new ArrayList<T>(Arrays.asList(elements));
        this.heapSize = elements.length;
        for (int i = 0; i < this.heapSize; i++) {
            T node = this.heap.get(i);
            this.addToIndexTable(node, i);
        }
        this.heapify();

    }

    @Override
    public String toString() {
        int SPACING = 1;

        String str = "empty heap";
        if (this.heapSize > 0) {
            str = "";
            int levels = (int) ((Math.log(this.heapSize)) / (Math.log(2))) + 1;
            int startIndex = 0;
            for (int i = 0; i < levels; i++) {
                int nElemMax = (int) Math.pow(2, i);
                String strLine = ""; // str representation for depth i
                for (int j = 0; j < nElemMax && startIndex + j < this.heapSize; j++) {
                    String elemStr = this.heap.get(startIndex + j).toString();
                    for (int k = 0; k < SPACING; k++) {
                        elemStr += " ";
                    }
                    strLine += elemStr;
                }
                str += strLine + "\n";
                startIndex += nElemMax;
            }
        }
        return str;
    }

    private void heapify() { // O(n)
        for (int i = Math.max(0, (this.heapSize / 2)) - 1; i >= 0; i--) {
            this.sink(i);
        }
    }

    private boolean isLess(int index1, int index2) {
        T node1 = this.heap.get(index1);
        T node2 = this.heap.get(index2);
        return node1.compareTo(node2) < 0;
    }

    private void swap(int index1, int index2) {
        // swaps the nodes in two indices
        // also updates this.indexTable

        // swap node values
        T node1 = this.heap.get(index1);
        T node2 = this.heap.get(index2);
        this.heap.set(index1, node2);
        this.heap.set(index2, node1);

        // update this.indexTable
        this.indexTable.get(node1).remove(index1);
        this.indexTable.get(node1).add(index2);
        this.indexTable.get(node2).remove(index2);
        this.indexTable.get(node2).add(index1);
    }

    // top to bottom node sink, O(log(n))
    private void sink(int index) {
        int left = 2 * index + 1; // left child node index
        int right = 2 * index + 2; // right child node index

        if (right < this.heapSize) {
            if (isLess(left, index) && isLess(right, index)) {
                if (isLess(left, right)) {
                    this.swap(index, left);
                    this.sink(left);
                } else {
                    this.swap(index, right);
                    this.sink(right);
                }
            } else if (isLess(left, index)) {
                this.swap(index, left);
                this.sink(left);
            } else if (isLess(right, index)) {
                this.swap(index, right);
                this.sink(right);
            }
        } else if (right == this.heapSize) { // very last element lies on left branch
            if (isLess(left, index)) {
                this.swap(index, left);
            }
        }
    }

    // bottom to up node swim, O(log(n))
    private void swim(int index) {
        if (index == 0) { // base case
            return;
        }

        int parent = (index - 1) / 2; // parent node index
        if (isLess(index, parent)) {
            this.swap(index, parent);
            this.swim(parent);
        }
    }

    // add a node value and its index to the indexTable
    private void addToIndexTable(T value, int index) {
        TreeSet<Integer> indices = this.indexTable.get(value);

        if (indices == null) { // no previous entry of 'value' in indexTable as a key
            indices = new TreeSet<>();
            indices.add(index);
            this.indexTable.put(value, indices);
        } else { // 'value' already exists in indexTable as a key
            indices.add(index);
        }
    }

    private void removeFromIndexTable(T value, int index) {
        TreeSet<Integer> indices = this.indexTable.get(value);
        if (indices != null) {
            indices.remove(index);
            if (indices.size() == 0) {
                this.indexTable.remove(value);
            }
        }
    }

    public void add(T elem) { // O(log(n))
        if (elem == null) {
            throw new IllegalArgumentException();
        }

        this.heap.add(elem);
        this.addToIndexTable(elem, this.heapSize);
        this.swim(this.heapSize);
        this.heapSize++;
    }

    private T removeAt(int index) {
        // returns the removed element if index is valid i.e. 0 <= index < this.heapSize
        // otherwise returns null

        if (index < 0 || index >= this.heapSize) {
            return null;
        }

        // approach:
        // 1. swap the element to be deleted with the last element in heap
        // 2. remove new last element of heap
        // 3. restore heap invariant by first (trying to) sink the swapped
        // element, followed by swimming it.

        T node = this.heap.get(index);
        this.swap(index, this.heapSize - 1);
        T removedNode = this.heap.remove(this.heapSize - 1);
        this.removeFromIndexTable(removedNode, this.heapSize - 1);
        this.heapSize--;

        this.sink(index);
        if (this.heap.get(index).equals(node)) { // trying to sink didn't work; so swim
            this.swim(index);
        }
        return node;
    }

    public boolean remove(T elem) { // O(n)
        if (elem == null) {
            return false;
        }
        // get index, O(n)
        int index = -1;
        for (int i = 0; i < this.heapSize; i++) {
            if (this.heap.get(i).equals(elem)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false; // element not found
        }

        this.removeAt(index);
        return true;
    }

    // remove and return root, O(1)
    // returns null if heap is empty
    public T poll() {
        return this.removeAt(0);
    }

    // return root if exists, else return null, O(1)
    public T peek() {
        if (this.heapSize > 0) {
            return this.heap.get(0);
        }
        return null;
    }

    public boolean contains(T elem) { // O(1)
        return this.indexTable.containsKey(elem);
    }

    public static void main(String[] args) {
        Integer[] elements = { 8, 3, 5, 6, 3, 3, 2 };

        PriorityQueue<Integer> pq = new PriorityQueue<>(elements);
        System.out.println(pq);
        System.out.println(pq.indexTable);

        pq.add(4);
        System.out.println("added 4");

        System.out.println(pq);
        System.out.println(pq.indexTable);

        pq.remove(2);
        System.out.println("removed 2");

        System.out.println(pq);
        System.out.println(pq.indexTable);

        Integer node = pq.poll();
        System.out.println("polled : " + node);

        System.out.println(pq);
        System.out.println(pq.indexTable);

        System.out.println(pq.contains(3));

    }
}
