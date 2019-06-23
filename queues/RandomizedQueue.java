/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int n;

    public RandomizedQueue() {                // construct an empty randomized queue
        arr = (Item[]) new Object[2];
        n = 0;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = arr[i];
        }
        arr = temp;
    }

    public boolean isEmpty() {                // is the randomized queue empty?
        return n == 0;
    }

    public int size() {                       // return the number of items on the randomized queue
        return n;
    }

    public void enqueue(Item item) {          // add the item
        if (item == null)
            throw new IllegalArgumentException();

        if (n == arr.length)
            resize(2*arr.length);
        arr[n] = item;
        n++;
    }

    public Item dequeue() {                   // remove and return a random item
        if (n == 0)
            throw new NoSuchElementException();

        int random = StdRandom.uniform(n);
        Item removedItem = arr[random];
        arr[random] = arr[n-1];
        n--;
        return removedItem;
    }

    public Item sample() {                    // return a random item (but do not remove it)
        if (n == 0)
            throw new NoSuchElementException();

        int random = StdRandom.uniform(n);
        return arr[random];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()    { return i < n; }
        public void remove()        { throw new UnsupportedOperationException(); }
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            int j = i;
            i++;
            return arr[j];
        }
    }

    public static void main(String[] args) {  // unit testing (optional)
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        queue.enqueue(4);
        queue.enqueue(3);
        queue.enqueue(2);
        queue.enqueue(1);
        for(int i : queue)
            StdOut.println(i);

    }
}