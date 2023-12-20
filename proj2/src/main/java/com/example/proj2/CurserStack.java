package com.example.proj2;

// Implementation of a stack using a cursor array.
public class CurserStack<Object> implements StackInterFace<Object> {
	// Cursor array to store the stack elements.
	CursorArray<Object> ca = new CursorArray<>(1000);

	// ID of the list representing the stack.
	int l = ca.createList();

	@Override
	public void push(Object data) {
		// Insert element at the head of the list.
		if (!ca.insertAtHead(data, l)) {
			System.out.println("Error: Satck Overflow!!!!");
		}
	}

	@Override
	public Object pop() {
		// Return and remove the element at the head of the list.
		return ca.deleteFirst(l);
	}

	@Override
	public Object peek() {
		// Return the element at the head of the list without removing it.
		return ca.getFirst(l);
	}

	@Override
	public boolean isEmpty() {
		// Return true if the list is empty, false otherwise.
		return ca.isEmpty(l);
	}

	@Override
	public void clear() {
		// Remove all elements from the list.
		while (true) {
			if (ca.isEmpty(l)) {
				break;
			} else {
				ca.deleteFirst(l);
			}
		}
	}
}
