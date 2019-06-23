package com.samisari.cezmi.interpreter;

public class Tree<N> {
	Class<N>			elementType;
	private TreeNode<N>	root;
	private TreeNode<N>	currentNode;

	public Tree() {
		setRoot(new TreeNode<>(null));
		setCurrentNode(getRoot());
	}
	public Tree(N value) {
		setRoot(new TreeNode<>(value));
		setCurrentNode(getRoot());
	}
	
	public TreeNode<N> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<N> root) {
		this.root = root;
	}

	public TreeNode<N> getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(TreeNode<N> currentNode) {
		this.currentNode = currentNode;
	}

	public void processBreathFirst() {
		
	}
	public static void main(String[] args) {
		Tree<String> tree = new Tree<>("+");
		TreeNode<String> root = tree.getRoot();
		root.addChild(new TreeNode<>("1"));
		root.addChild(new TreeNode<>("1"));
		
	}
}
