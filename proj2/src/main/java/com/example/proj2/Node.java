package com.example.proj2;
public class Node<Object > {
	Object data;
	int next;

	public Node(Object data, int next) {
		this.data = data;
		this.next = next;
	}

	public String toString() {
		return "[" + data + " , " + next + "]";
	}
}
