package com.example.proj2;

public interface StackInterFace<Object> {

	public void push(Object data);
	public Object pop();
	public Object peek();
	public boolean isEmpty();
	public void clear();
}
