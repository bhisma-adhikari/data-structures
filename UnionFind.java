import java.util.*;

// Unlike William Fiset's implementation, this implementation accepts the 
// actual objects (rather than their mapped indices) for various APIs. In 
// other words the bijection mapping from objects to indices in maintained
// internally in the implementation of UnionFind itself 

// Public Methods: 
// UnionFind(Set of elements)     --> void     O(n)      : returns an instance of UnionFind 
// unify(elem1, elem2)            --> void     O(n)+     : merge corresponding components. If they are already connected, do nothing. 
// find(elem1)                    --> elem     O(n)+     : returns the root element of the component to which elem1 belongs
// getComponentSize(elem)        --> int      O(n)+     : returns the size of the component to which elem belongs 
// areConnected(elem1, elem2)     --> bool     O(n)+     : check if elem1 and elem2 are connected 
// getComponentsCount()           --> int      O(1)      : return the total number of components 

class UnionFind<T> {
    private final int N_ELEMS;
    int nComp; // number of components

    // This implementation deals internally in indices rather than objects.
    // These two maps are for necessary conversions.
    private Map<T, Integer> mapElemToIndex;
    private Map<Integer, T> mapIndexToElem;

    // parent[i] gives index of parent elem that maps to i by this.mapElemToIndex
    private int[] parent;

    // size[i] gives the size of the component to which the element mapped to i
    // (by this.mapElemToIndex) belongs.
    // This is valid only for those indices which are still valid root indices.
    private int[] size;

    public UnionFind(Set<T> elems) {

        this.mapElemToIndex = new HashMap<>();
        this.mapIndexToElem = new HashMap<>();

        int i = 0;
        for (T elem : elems) {
            this.mapElemToIndex.put(elem, i);
            this.mapIndexToElem.put(i, elem);
            i++;
        }
        this.N_ELEMS = elems.size();
        this.nComp = this.N_ELEMS;

        // set parent of each element, which initially is itself for all elements
        // set component size of all components to 1
        this.parent = new int[this.N_ELEMS];
        this.size = new int[this.N_ELEMS];
        for (i = 0; i < this.parent.length; i++) {
            this.parent[i] = i;
            this.size[i] = 1;
        }
    }

    @Override
    public String toString() {
        // key: root index
        // value: set of elements
        Map<Integer, Set<T>> map = new HashMap<>();

        for (int i = 0; i < this.N_ELEMS; i++) {
            T elem = this.mapIndexToElem.get(i);

            int rootIndex = this.findRootElementIndexAndCompressPath(i);
            if (map.containsKey(rootIndex)) {
                map.get(rootIndex).add(elem);
            } else {
                Set<T> set = new TreeSet<>(Arrays.asList(elem));
                map.put(rootIndex, set);
            }
        }

        String str = "";
        for (Set<T> elems : map.values()) {
            for (T elem : elems) {
                str += elem + ", ";
            }
            str += "\n";
        }
        return str;

    }

    public void unify(T elem1, T elem2) {
        int index1 = this.mapElemToIndex.get(elem1);
        int index2 = this.mapElemToIndex.get(elem2);

        int rootIndex1 = this.findRootElementIndexAndCompressPath(index1);
        int rootIndex2 = this.findRootElementIndexAndCompressPath(index2);

        if (rootIndex1 == rootIndex2) { // already unified
            return;
        }

        int size1 = this.size[rootIndex1];
        int size2 = this.size[rootIndex2];

        // merge smaller component into the larger component
        if (size1 < size2) {
            this.parent[rootIndex1] = rootIndex2;
            this.size[rootIndex2] += this.size[rootIndex1];
        } else {
            this.parent[rootIndex2] = rootIndex1;
            this.size[rootIndex1] += this.size[rootIndex2];
        }

        this.nComp--;

    }

    private int findRootElementIndexAndCompressPath(int elemIndex) {
        // return the index of root element,
        // Additionally, perform path compression.

        // 'rootIndex' will take the value of rootIndex after while loop exits
        int rootIndex = elemIndex;
        while (rootIndex != this.parent[elemIndex]) {
            rootIndex = this.parent[elemIndex];
        }
        // Path compression: Set the parent of an element directly to the root element
        // Necessary to reduce the complexity to O(n)+
        while (elemIndex != rootIndex) {
            int earlierParent = this.parent[elemIndex];
            this.parent[elemIndex] = rootIndex;
            elemIndex = earlierParent;
        }
        return rootIndex;
    }

    public T find(T elem) {
        int elemIndex = this.mapElemToIndex.get(elem);
        int rootIndex = this.findRootElementIndexAndCompressPath(elemIndex);
        return this.mapIndexToElem.get(rootIndex);
    }

    public int getComponentSize(T elem) {
        int elemIndex = this.mapElemToIndex.get(elem);
        int rootIndex = this.findRootElementIndexAndCompressPath(elemIndex);
        return this.size[rootIndex];
    }

    public boolean areConnected(T elem1, T elem2) {
        int elemIndex1 = this.mapElemToIndex.get(elem1);
        int elemIndex2 = this.mapElemToIndex.get(elem2);
        int rootIndex1 = this.findRootElementIndexAndCompressPath(elemIndex1);
        int rootIndex2 = this.findRootElementIndexAndCompressPath(elemIndex2);
        return rootIndex1 == rootIndex2;

    }

    public int getComponentCount() {
        return this.nComp;
    }

    public static void main(String[] args) {
        Set<String> set = new TreeSet<>();
        set.add("House");
        set.add("Dog");
        set.add("Daddy");
        set.add("Apple");
        set.add("Google");

        UnionFind<String> uf = new UnionFind<>(set);
        // System.out.println(uf.elemIndexMap);

        System.out.println(uf);

        System.out.println(uf.areConnected("Apple", "Google"));

        uf.unify("Apple", "Google");
        System.out.println("Apple and Google unified\n");
        System.out.println(uf);
        System.out.println(uf.areConnected("Apple", "Google"));

        System.out.println(uf.find("Apple"));
        System.out.println(uf.find("Google"));

        System.out.println(uf.getComponentSize("Apple"));
        System.out.println(uf.getComponentSize("Daddy"));

    }
}