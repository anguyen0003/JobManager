package com.example.jobmanager;

import java.util.ArrayList;
import java.util.List;

// Binary Search Tree
public class ApplicationTree {

    private Node root;

    // Inserts application into tree by date or job title
    public void insert(Application application, boolean sortByDate) {
        root = insertRecursion(root, application, sortByDate);
    }

    private Node insertRecursion(Node root, Application application, boolean sortByDate) {
        if (root == null) {
            return new Node(application);
        }

        int compareValue;
        if (sortByDate) {
            compareValue = application.getAppliedDate().compareTo(root.data.getAppliedDate());
        } else {
            compareValue = application.getJobTitle().compareTo(root.data.getJobTitle());
        }

        if (compareValue < 0) {
            root.left = insertRecursion(root.left, application, sortByDate);
        } else {
            root.right = insertRecursion(root.right, application, sortByDate);
        }

        return root;
    }

    // In order traversal for sorting
    public List<Application> inOrderTraversal() {
        List<Application> sortedList = new ArrayList<>();
        inOrderRecursion(root, sortedList);
        return sortedList;
    }

    private void inOrderRecursion(Node root, List<Application> sortedList) {
        if (root != null) {
            inOrderRecursion(root.left, sortedList);
            sortedList.add(root.data);
            inOrderRecursion(root.right, sortedList);
        }
    }

    public void clear() {
        root = null;
    }

    private static class Node {
        Application data;
        Node left;
        Node right;

        Node(Application data) {
            this.data = data;
        }
    }
}