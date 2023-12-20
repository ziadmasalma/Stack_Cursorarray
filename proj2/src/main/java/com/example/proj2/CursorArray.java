package com.example.proj2;

public class CursorArray <Object> {
	Node<Object>[] cursorArray;

	// Constructor: creates a cursor array with the given capacity
	public CursorArray(int capacity) {
		cursorArray = new Node[capacity];
		initialization();
	}

	// Initializes the cursor array by linking each node to the next one
	private int initialization() {
		for (int i = 0; i < cursorArray.length - 1; i++)
			cursorArray[i] = new Node<>(null, i + 1);
		cursorArray[cursorArray.length - 1] = new Node<>(null, 0);
		return 0;
	}

	// Allocates a new node from the cursor array
	private int malloc() {
		int p = cursorArray[0].next;
		cursorArray[0].next = cursorArray[p].next;
		return p;
	}

	// Returns true if the list with the given index is null
	private boolean isNull(int l) {
		return cursorArray[l] == null;
	}

	// Returns true if the list with the given index is empty
	public boolean isEmpty(int l) {
		return cursorArray[l].next == 0;
	}

	// Creates a new empty list and returns its index
	public int createList() {
		int l = malloc();
		if (l == 0)
			System.out.println("Error: Out of space!!!");
		else
			cursorArray[l] = new Node("-", 0);
		return l;
	}

	// Inserts a new node with the given data at the head of the list with the given index
	// Returns false if the list is null or if there is no space available
	public boolean insertAtHead(Object data, int l) {
		if (isNull(l)) // list not created
			return false;
		int p = malloc();
		if (p != 0) {
			cursorArray[p] = new Node(data, cursorArray[l].next);
			cursorArray[l].next = p;
		} else {
			return false;
		}
		return true;
	}

	// Deletes the first node of the cursor
	public Object deleteFirst(int l) {
		if (!isNull(l) && !isEmpty(l)) {
			int p = cursorArray[l].next;
			cursorArray[l].next = cursorArray[cursorArray[l].next].next;
			Node temp = cursorArray[p];
			return (Object) temp.data;
		}
		return null;
	}

	// Return the element at the head of the list without removing it.
	public Object getFirst(int l) {
		if (!isNull(l) && !isEmpty(l))
			return cursorArray[cursorArray[l].next].data;
		return null;
	}


}