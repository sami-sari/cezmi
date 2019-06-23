package com.samisari.cezmi.interpreter;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
	T							value;
	private List<TreeNode<T>>	children;
	private TreeNode<T>			parent;

	public TreeNode(T value) {
		setValue(value);
		setChildren(new ArrayList<TreeNode<T>>() );
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}
	
	public void addChild(TreeNode<T> child) {
		getChildren().add(child);
	}

	public void insertChild(int index, TreeNode<T> child) {
		getChildren().add(index, child);
	}
	
	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}
}
