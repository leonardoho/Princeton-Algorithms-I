/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node first;
    private Node last;

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node(Item item) {
            this.item = item;
        }
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;
        public boolean hasNext()    { return current != null; }
        public void remove()        { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

    }

    public Deque() {                           // construct an empty deque
        n = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {                 // is the deque empty?
        return n == 0;
    }

    public int size() {                       // return the number of items on the deque
        return n;
    }

    public void addFirst(Item item) {         // add the item to the front
        if (item == null)
            throw new IllegalArgumentException();

        if (first == null) {
            first = new Node(item);
            first.next = null;
            last = first;
        } else {
          Node oldFirst = this.first;
          first = new Node(item);
          first.next = oldFirst;
          oldFirst.prev = first;
        }
        first.prev = null;
        n++;
    }

    public void addLast(Item item) {          // add the item to the end
        if (item == null)
            throw new IllegalArgumentException();

        if (last == null) {
            last = new Node(item);
            last.prev = null;
            first = last;
        } else {
            Node oldLast = this.last;
            last = new Node(item);
            last.prev = oldLast;
            oldLast.next = last;
        }
        last.next = null;
        n++;
    }

    public Item removeFirst() {               // remove and return the item from the front
        if (first == null) {
            throw new NoSuchElementException();
        }

        Node oldFirst = this.first;
        if (n == 1) {
            first = null;
        } else {
            first = oldFirst.next;
            first.prev = null;
        }
        n--;
        return oldFirst.item;
    }

    public Item removeLast() {                 // remove and return the item from the end
        if (last == null) {
            throw new NoSuchElementException();
        }

        Node oldLast = this.last;
        if (n == 1) {
            last = null;
        } else {
            last = oldLast.prev;
            last.next = null;
        }
        n--;
        return oldLast.item;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new ListIterator();
    }

    public static void main(String[] args) {  // unit testing (optional)
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(4);
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        for(int i : deque)
            StdOut.println(i);

    }
}
