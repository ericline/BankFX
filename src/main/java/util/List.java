package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * List class containing list methods.
 * @author Eric Lin Anish Mande
 */

public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;

    /**
     * Constructor to initialize the List
     * type-casted to E with a capacity of 4.
     */
    public List() {
        objects = (E[]) new Object[4];
        size = 0;
    }

    /**
     * Find the index of the specified object in the list.
     * @param e The object to search for.
     * @return The index of the object, or -1 if not found.
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i] != null && objects[i].equals(e))
                return i;
        }
        return -1;
    }

    /**
     * Grow the internal array when capacity is reached.
     * Increases capacity by 4.
     */

    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length + 4];
        for (int i = 0; i < size; i++) {
            newObjects[i] = objects[i];
        }
        objects = newObjects;
    }

    /**
     * Check if the list contains the specified object.
     * @param e The object to check.
     * @return True if the object is present, false otherwise.
     */
    public boolean contains(E e) {
        return find(e) != -1;
    }

    /**
     * Add a new object to the list.
     * If the internal array is full, it grows in size.
     * @param e The object to add.
     */
    public void add(E e) {
        if (size == objects.length)
            grow();
        objects[size++] = e;
    }

    /**
     * Remove the specified object from the list.
     * The last object is moved to the removed object's position.
     * @param e The object to remove.
     */
    public void remove(E e) {
        int idx = find(e);
        if (idx != -1) {
            objects[idx] = objects[--size];
            objects[size] = null;
        }
    }

    /**
     * Check if the list is empty.
     * @return True if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the number of elements in the list.
     * @return The size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * Return an iterator to traverse the list.
     * @return An Iterator for the list.
     */
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Get the object at the specified index.
     * @param index The index of the object.
     * @return The object at the given index.
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return objects[index];
    }

    /**
     * Set the object e at the specified index.
     * @param index The index of the object to set.
     * @param e The new object.
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        objects[index] = e;
    }

    /**
     * Get the index of the specified object e.
     * @param e The object to search for.
     * @return The index of the object, or -1 if not found.
     */
    public int indexOf(E e) {
        return find(e);
    }

    private class ListIterator<E> implements Iterator<E> {
        int current = 0; //current index when traversing the list (array)

        // Return false if it’s empty or at end of list
        @Override
        public boolean hasNext() {
            return current < size;
        }
        //return the next object in the list
        @Override
        public E next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (E) objects[current++];
        }
    }
}
