// This program implements DOUBLY-LINKED-LIST 

// AVAILABLE METHODS: 
// - insertAt(element, index)   --> void            O(n)  
// - removeAt(index)            --> element         O(n) 
// - remove(element)            --> boolean         O(n)    removes first occurrence of the element (returns true if an element was removed, else returns false)
// - append(element)            --> void            O(1)
// - peekAt(index)              --> element         O(n)
// - removeTail()               --> element         O(1) 

import java.util.StringJoiner;
import java.util.Iterator;

public class DoublyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size; // will be used to check validity of index

    // TODO: don't know why 'static' is used. (removing 'static' seems to have no
    // effect)
    // I just copied it from what was done in william-fiset's code
    private static class Node<T> {
        private T data;
        private Node<T> prev;
        private Node<T> next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return this.data.toString();
        }
    }

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]"); // separater, prefix, suffix
        Node<T> n = this.head;
        while (n != null) {
            sj.add(n.toString());
            n = n.next;
        }
        return sj.toString();
    }

    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return peekAt(index++);
            }
        };
    }

    private Node<T> getNodeAt(int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IllegalArgumentException("Invalid index " + index);
        }
        if (index < this.size) {
            Node<T> trav = this.head;
            for (int i = 0; i < index; i++) {
                trav = trav.next;
            }
            return trav;
        } else {
            Node<T> trav = this.tail;
            for (int i = this.size - 1; i > index; i--) {
                trav = trav.prev;
            }
            return trav;
        }
    }

    public void insertAt(T elem, int index) {
        if (index < 0 || index > this.size) {
            throw new IllegalArgumentException("Invalid index " + index);
        }

        if (index == 0) {
            // insert before current head (which may be null in case this.size == 0)
            Node<T> node = new Node<T>(elem, null, this.head);
            if (this.head != null) {
                this.head.prev = node;
            }
            this.head = node;
            if (this.tail == null) {
                this.tail = node;
            }
        } else if (index == this.size) {
            // insert after current tail
            // if program control reaches here, this.tail is guarranted not to be null.
            Node<T> node = new Node<>(elem, this.tail, null);
            this.tail.next = node;
            this.tail = node;
        } else {
            Node<T> trav = this.getNodeAt(index);
            Node<T> node = new Node<>(elem, trav.prev, trav);
            trav.prev.next = node;
            trav.prev = node;
        }
        this.size++;
    }

    public T removeAt(int index) {
        // returns the removed element
        if (index < 0 || index > this.size - 1) {
            throw new IllegalArgumentException("Invalid index " + index);
        }

        T data;
        if (index == 0) {
            // remove first element
            data = this.head.data;
            this.head = this.head.next;
            if (this.head != null) {
                this.head.prev = null;
            }
        } else if (index == this.size - 1) {
            // remove last element
            data = this.tail.data;
            this.tail = this.tail.prev;
            this.tail.next = null;
        } else {
            Node<T> trav = this.getNodeAt(index);
            data = trav.data;
            trav.prev.next = trav.next;
            trav.next.prev = trav.prev;
        }
        this.size--;
        return data;
    }

    public boolean remove(T elem) {
        if (this.size == 0) {
            return false;
        }

        Node trav = this.head;
        while (trav != null) {
            if (trav.data == elem) {
                // we could reuse the method removeAt() by tracking index
                // That would simplify code, but would be inefficient
                
                if (trav.prev == null) { // trav is head
                    this.head = trav.next;
                } else {
                    trav.prev.next = trav.next;
                }

                if (trav.next == null) { // trav is tail
                    this.tail = trav.prev;
                } else {
                    trav.next.prev = trav.prev;
                }
                this.size--;
                return true;
            }
            trav = trav.next;
        }
        return false;
    }

    public void append(T elem) {
        this.insertAt(elem, this.size);
    }

    public T peekAt(int index) {
        // returns element at given index
        return this.getNodeAt(index).data;
    }

    public T removeTail() {
        if (this.size == 0) {
            throw new RuntimeException("Cannot remove element from empty linked-list");
        }
        return this.removeAt(this.size - 1);
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();
        dll.insertAt(44, 0);
        dll.insertAt(21, 0);
        System.out.println(dll);

        dll.append(5);
        dll.append(8);
        dll.append(3);
        dll.append(7);
        dll.insertAt(9, 3);
        System.out.println(dll);
        dll.removeAt(0);
        System.out.println(dll);

        dll.insertAt(9, 3);
        System.out.println(dll);
        dll.removeAt(0);
        System.out.println(dll);

        System.out.println(dll.peekAt(3));

        Iterator itr = dll.iterator();
        System.out.println();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        dll.insertAt(3, 0);

        System.out.println(dll);
        System.out.println(dll.removeTail());
        System.out.println(dll.removeTail());

        System.out.println(dll);
        System.out.println(dll.remove(8));
        System.out.println(dll);

    }
}
