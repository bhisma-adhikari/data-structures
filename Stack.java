// This program implements STACK

// AVAILABLE METHODS: 
// - push(element)  --> void 
// - pop()          --> element (removes element from stack)
// - peek()         --> element (does not remove element from stack)

import java.util.StringJoiner;
import java.util.Iterator;

class Stack<T> implements Iterable {
    private int size;
    private Node<T> head;

    public Stack() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        Node trav = this.head;
        while (trav != null) {
            sj.add(trav.data.toString());
            trav = trav.prev;
        }
        return sj.toString();
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            int index = size;
            Node<T> trav = head;

            @Override
            public boolean hasNext() {
                return index != 0;
            }

            @Override
            public T next() {
                T data = trav.data;
                trav = trav.prev;
                index--;
                return data;
            }
        };
    }

    private class Node<T> {
        private T data;
        private Node<T> prev;

        public Node(T data, Node<T> prev) {
            this.data = data;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return this.data.toString();
        }
    }

    public void push(T elem) {
        if (this.size == 0) {
            this.head = new Node<T>(elem, null);
        } else {
            this.head = new Node<T>(elem, this.head);
        }
        this.size++;
    }

    public T pop() {
        if (this.size == 0) {
            throw new java.util.EmptyStackException();
        }
        T data = this.head.data;
        this.head = this.head.prev;
        this.size--;
        return data;
    }

    public T peek() {
        if (this.size == 0) {
            throw new java.util.EmptyStackException();
        }
        return this.head.data;
    }

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(6);

        System.out.println(stack);
        // stack.pop();
        // System.out.println(stack);
        // stack.pop();
        // System.out.println(stack);
        // stack.pop();
        // System.out.println(stack);

        Iterator itr = stack.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

        System.out.println(stack.peek());

    }
}