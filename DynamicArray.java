
// This program implements DYNAMIC ARRAY using STATIC ARRAY as 
// the underlying data structure. 
// The underlying static-array is doubled in size as its size 
// becomes insufficient, which can happen when performiing an 
// append/insert operation. However, the size is never shrunk. 

// AVAILABLE METHODS: 
// - append(element)            --> void 
// - insertAt(element, index)   --> void 
// - deleteAt(index)            --> void  
// - contains(element)          --> boolean
// - indexOf(element)           --> int 

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class DynamicArray<T> implements Iterable<T> {
    private int capacity;
    private int size; // current size of array
    private T[] data;

    public DynamicArray() {
        this.capacity = 2;
        this.size = 0;
        this.data = (T[]) new Object[capacity];
    }

    private void expandCapacity() {
        // double the capacity
        this.capacity *= 2;
        T[] newData = (T[]) new Object[this.capacity];
        for (int i = 0; i < this.size; i++) {
            newData[i] = this.data[i];
        }
        this.data = newData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[");
        for (int i = 0; i < this.size; i++) {
            if (i == 0) {
                sb.append(" ");
            } else {
                sb.append(", ");
            }
            sb.append(this.data[i]);
        }
        sb.append(" ]");

        sb.append("\nsize    : " + this.size);
        sb.append("\ncapacity: " + this.capacity);

        return sb.toString();
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return data[index++];
            }
        };
    }

    public void append(T x) {
        if (this.size == this.capacity) {
            this.expandCapacity();
        }
        this.data[this.size] = x;
        this.size++;
    }

    public void insertAt(int index, T x) {
        if (index > this.size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (this.size == this.capacity) {
            this.expandCapacity();
        }
        T[] newData = (T[]) new Object[this.capacity];
        for (int i = 0; i < this.size + 1; i++) {
            if (i < index) {
                newData[i] = this.data[i];
            } else if (i == index) {
                newData[i] = x;
            } else {
                newData[i] = this.data[i - 1];
            }
        }
        this.data = newData;
        this.size++;

    }

    public void deleteAt(int index) {
        if (index > this.size - 1 || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = index; i < this.size; i++) {
            this.data[i] = this.data[i + 1];
        }
        this.size--;
    }

    public boolean contains(T x) {
        // Return true if this dynamic array contains x, else return false
        for (T i : this.data) {
            if (i == x) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(T x) {
        // Return the index of element x,
        // If x is not present in this.data, return -1
        for (int i = 0; i < this.size; i++) {
            if (this.data[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        DynamicArray<Integer> da = new DynamicArray<>();
        System.out.println(da);

        da.append(2);
        da.append(6);
        da.append(1);
        da.append(8);
        da.append(3);
        da.insertAt(2, 55);
        System.out.println(da);

        da.deleteAt(2);

        System.out.println(da);

        System.out.println(da.contains(6));

        System.out.print(da.indexOf(89));

        Iterator itr = da.iterator();
        System.out.println();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

    }
}