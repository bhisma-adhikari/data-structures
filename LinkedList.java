// This program implements DOUBLY-LINKED-LIST 

// AVAILABLE METHODS: 
// - insertAt(element, index)   --> void 
// - removeAt(index)            --> element 
// - append(element)            --> void 
// - peekAt(index)              --> element

import java.util.StringJoiner;

class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size; // will be used to check validity of index

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
            this.head.prev = null;
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

    public void append(T elem) {
        this.insertAt(elem, this.size);
    }

    public T peekAt(int index) {
        // returns element at given index
        return this.getNodeAt(index).data;
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

    }
}
