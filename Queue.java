// This program implements QUEUE using doubly-linked-list

// to run, execute the following command (need to compile DoublyLinkedList.java too):  

// javac Queue.java; java Queue.java 

// AVAILABLE METHODS: 
// - enqueue(element)   --> void (adds element to the back of the queue)            O(1)
// - dequeue()          --> element (removes element from the front of the queue)   O(1)
// - peek()               --> element (returns the value at front of the queue)     O(1)
// - remove(element)    --> element                                                 O(n)
// - contains(element)  --> boolean                                                 O(n)
// - isEmpty()          --> boolean                                                 O(1)
// - getSize()          --> int                                                     O(1)

class Queue<T> {
    private DoublyLinkedList<T> dll;

    public Queue() {
        this.dll = new DoublyLinkedList<T>();
    }

    @Override
    public String toString() {
        return this.dll.toString();
    }

    public void enqueue(T elem) {
        this.dll.append(elem);
    }

    public T dequeue() {
        return this.dll.removeAt(0);
    }

    public T peek() {
        return this.dll.peekAt(0);
    }

    public boolean remove(T elem) {
        return this.dll.remove(elem);
    }

    public boolean contains(T elem) {
        return this.dll.contains(elem);
    }

    public boolean isEmpty() {
        return this.dll.isEmpty();
    }

    public int getSize() {
        return this.dll.getSize();
    }

    public static void main(String[] args) {
        Queue<Integer> queue = new Queue<>();
        System.out.println(queue);

        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        System.out.println(queue);

        // queue.dequeue();
        // System.out.println(queue);
        // queue.dequeue();
        // System.out.println(queue);
        // queue.dequeue();
        // System.out.println(queue);

        System.out.println(queue.peek());

        System.out.println(queue.remove(444));
        System.out.println(queue);

        System.out.println(queue.remove(4));
        System.out.println(queue);

        System.out.println(queue.contains(5));
        System.out.println(queue.contains(55));

        System.out.println(queue.isEmpty());
        queue = new Queue<>();
        System.out.println(queue.isEmpty());

    }
}