import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

// This program implements the data-structure Priority Queue using min-heap.
// Priority queue is an abstract data structure. 

// This implementation is suited over "PriorityQueueQuickRemoval.java" if 'remove' operation 
// is NOT performed frequently. 

// Public Methods: 
// add(element)     --> void        O(log(n))   : adds element to the priority queue 
// remove(element)  --> boolean     O(n)        : removes element from the priority queue 
//                                                If the element occurs multiple times, the one 
//                                                at the lowest index will be removed.
//                                                Returns true if an element is successfully removed, else false.
// poll()           --> element     O(1)        : removes and returns root element; root element always 
//                                                has the highest priority for removal 
// peek()           --> element     O(1)        : returns root element 
// contains(element)--> boolean     O(n)        : checks if the element is contained in the PQ

class PriorityQueue<T extends Comparable> {
    private int heapSize; // current number of elements
    private List<T> heap;

    public PriorityQueue(T[] elements) {
        this.heap = new ArrayList<T>(Arrays.asList(elements));
        this.heapSize = elements.length;
        this.heapify();
    }

    @Override
    public String toString() {
        int SPACING = 1;

        String str = "empty heap";
        if (this.heapSize > 0) {
            str = "";
            int levels = (int) Math.ceil((Math.log(this.heapSize)) / (Math.log(2)));
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

    private void heapify() {
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
        T node1 = this.heap.get(index1);
        T node2 = this.heap.get(index2);
        this.heap.set(index1, node2);
        this.heap.set(index2, node1);
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

    public void add(T elem) { // O(log(n))
        if (elem == null) {
            throw new IllegalArgumentException();
        }
        this.heap.add(elem);
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
        this.heap.remove(this.heapSize - 1);
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

    public boolean contains(T elem) { // O(n)
        for (int i = 0; i < this.heapSize; i++) {
            if (this.heap.get(i).equals(elem)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Integer[] elements = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };

        PriorityQueue<Integer> pq = new PriorityQueue<>(elements);
        System.out.println(pq);
        System.out.println(pq.poll());
        System.out.println(pq);
        System.out.println(pq.peek());
        System.out.println(pq.contains(54));
    }
}