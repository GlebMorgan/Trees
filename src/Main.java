import java.io.*;
import java.util.*;

class Node<T> {

    int key;
    T data;
    Node<T> left;
    Node<T> right;

    Node(int key, T data, Node<T> left, Node<T> right) {
        this.key = key;
        this.data = data;
        this.left = left;
        this.right = right;
    }
}

class Tree<T> {

    Node<T> root = null;
    private int nodesCount = 0;
    private int height = 0;

    Tree() {
    }

    void addTreeLeaf (int key, T value) {
        if (root == null) {
            root = new Node<> (key, value, null, null);
            nodesCount = 1;
            height = 1;
            return;
        }
        Node curr = root;
        int currHeight = 1;
        while (true) {
            currHeight++;
            if (key > curr.key) {
                if (curr.right == null) {
                    curr.right = new Node<> (key, value, null, null);
                    nodesCount++;
                    if (currHeight > height) height = currHeight;
                    break;
                }
                else curr = curr.right;
            }
            else {
                if (curr.left == null) {
                    curr.left = new Node<> (key, value, null, null);
                    nodesCount++;
                    if (currHeight > height) height = currHeight;
                    break;
                }
                else curr = curr.left;
            }
        }
    }

    void deleteAllTree () {
        nodesCount = 0;
        height = 0;
        root = null;
    }

    int getNodesCount() {
        return nodesCount;
    }

    int getTreeHeight () {
        return height;
    }

    private int getNumOf_ (int level) {
        int numOf_ = height-level;
        if (numOf_ == 0) return -3;
        if (numOf_ == 1) return -1;
        if (numOf_ == 2) return 1;
        numOf_ = 1;
        for (int i=1; i<height-level-1; i++) numOf_ = numOf_*2+2;
        return numOf_;
    }

    private void outPutSequenceOfChars(int count, char character) {
        for (short i=1; i<=count; i++) System.out.print(character);
    }

    void viewTree () {
        if (root == null) {
            System.out.print("Tree is empty.");
            return;
        }
        if (height>6) {
            System.out.print("Curr tree is too big to display. Try balancing."
                    + "Max elements count in balanced tree for displaying is 62.");
            return;
        }
        Node<T> treeCurr = root;
        boolean three_space = true;
        int level = 0, numOf_, numOfSpaces, i, elements_per_level;
        LinkedList<Node<T>> queue = new LinkedList<>();
        queue.offerLast(treeCurr);
        System.out.println();
        while (++level <= height) {
            numOf_ = getNumOf_(level);
            numOfSpaces = ( (numOf_*2 + 3) - numOf_ )*2 + 1;
            outPutSequenceOfChars(numOf_+4, ' '); //start spaces
            elements_per_level = (int) Math.pow (2, level-1);
            for (i=1; i<=elements_per_level; i++) {
                treeCurr = queue.removeFirst();
                outPutSequenceOfChars(numOf_, '_'); //"_"s before value
                if (treeCurr != null) System.out.print(treeCurr.key);
                else System.out.print((char)176);
                outPutSequenceOfChars(numOf_, '_'); //"_"s after value
                if (i != elements_per_level) {
                    if (level == height) {
                        if (three_space) {
                            System.out.print("   ");
                        }
                        else System.out.print(" ");
                        three_space=!three_space;
                    }
                    else outPutSequenceOfChars(numOfSpaces, ' '); //middle spaces
                }
                if (treeCurr != null) {
                    queue.offerLast(treeCurr.left);
                    queue.offerLast(treeCurr.right);
                }
                else {
                    queue.offerLast(null);
                    queue.offerLast(null);
                }
            }
            if (numOf_!=-3) {
                System.out.println();
                outPutSequenceOfChars(numOf_+3, ' '); //start spaces - 1
                for (i=1; i<=elements_per_level; i++) {
                    System.out.print("/");
                    if (numOf_==-1) System.out.print(' ');
                    else outPutSequenceOfChars(numOf_*2+1, ' '); //spaces between "/" & "\"
                    System.out.print("\\");
                    if (i!=elements_per_level) {
                        outPutSequenceOfChars(numOfSpaces-2, ' '); //-||- between "\" & "/"
                    }
                }
                System.out.println();
            }
        }
    }

    void viewTreeSimply (Node curr) {
        System.out.print(curr.key + " ");
        if (curr.left != null) {
            System.out.print("left ");
            viewTreeSimply(curr.left);
            System.out.print("< ");
        }
        if (curr.right != null) {
            System.out.print("right ");
            viewTreeSimply(curr.right);
            System.out.print("< ");
        }
    }

    private void treeToArray (Node<T> curr, ArrayList<Node<T>> array) {
        if(curr.left != null) treeToArray(curr.left, array);
        array.add(curr);
        if(curr.right != null) treeToArray(curr.right, array);
    }

    void balanceTree () {
        ArrayList<Node<T>> elementsArray = new ArrayList<>();
        treeToArray(root, elementsArray);
        root = null;
        System.out.println();
        root = createBalancedTreeFromArray (elementsArray, 0, elementsArray.size()-1);
        height = updateTreeHeight(root);
    }

    private Node<T> createBalancedTreeFromArray (ArrayList<Node<T>> array, int startPos, int endPos) {
        Node<T> curr;
        if (startPos+1==endPos) {
            curr = new Node<> (array.get(startPos).key, array.get(startPos).data, null, null);
            curr.right = new Node<> (array.get(endPos).key, array.get(startPos).data, null, null);
        }
        else {
            curr = new Node<> (array.get((endPos+startPos)/2).key, array.get(startPos).data, null, null);
            if (startPos!=endPos) {
                curr.left = createBalancedTreeFromArray(array, startPos, (endPos+startPos)/2-1);
                curr.right = createBalancedTreeFromArray(array, (endPos+startPos)/2+1, endPos);
            }
        }
        return curr;
    }

    private Node<T> deleteTreeElement (Node<T> removedNode) {
        if (removedNode.left == null) {
            removedNode = removedNode.right;
        }
        else if (removedNode.right == null) {
            removedNode = removedNode.left;
        }
        else if (removedNode.left.right == null) {
            removedNode.left.right = removedNode.right;
            removedNode = removedNode.left;
        }
        else {
            Node<T> successorParent = removedNode.left;
            while (successorParent.right.right != null) successorParent = successorParent.right;
            Node<T> temp = successorParent.right.left;
            successorParent.right.left = removedNode.left;
            successorParent.right.right = removedNode.right;
            removedNode = successorParent.right;
            successorParent.right = temp;
        }
        nodesCount--;
        return removedNode;
    }

    private void removeAllSpecifiedTreeNodesExceptRoot(Node<T> curr, int key) {
        while (curr.left != null && key == curr.left.key) {
            curr.left = deleteTreeElement (curr.left);
        }
        while (curr.right != null && key == curr.right.key) {
            curr.right = deleteTreeElement (curr.right);
        }
        if (curr.left != null) {
            removeAllSpecifiedTreeNodesExceptRoot(curr.left, key);
        }
        if (curr.right != null) {
            removeAllSpecifiedTreeNodesExceptRoot(curr.right, key);
        }
    }

    void removeAllSpecifiedTreeNodes(int key) {
        while (root != null && key == root.key) root = deleteTreeElement (root);
        if (root == null) return;
        removeAllSpecifiedTreeNodesExceptRoot(root, key);
        height = updateTreeHeight(root);
    }

    private int updateTreeHeight (Node<T> curr) {
        if (curr.left != null && curr.right != null) {
            return Math.max(updateTreeHeight(curr.left), updateTreeHeight(curr.right)) + 1;
        }
        if (curr.left != null) {
            return updateTreeHeight(curr.left) + 1;
        }
        if (curr.right != null) {
            return updateTreeHeight(curr.right) + 1;
        }
        return 1;
    }
}

public class Main {

    private static char readKey(java.util.Scanner scanner) {
        String input = scanner.nextLine().trim();
        if(input.isEmpty()) return '\0';
        else return input.toUpperCase().charAt(0);
    }

    public static void main(String[] args) throws IOException {

        System.out.println("--------------");
        System.out.println("Trees lab work");
        System.out.println("--------------");

        Scanner reader = new Scanner(System.in);
        char key;           // menu key-command
        int temp;       //buffer for function calls

        Random random = new Random();
        Tree<Double> tree = null;

        while (true) {

            System.out.print("\n\nEnter a command");
            System.out.print("(\"H\" for help): ");
            do {
                key = readKey(reader);
            } while (key<'A' || key>'Z');

            if (tree == null && key!='C' && key!='Q' && key!='H') {
                System.out.println("Tree is empty.");
                System.out.println("You have to create the tree first.");
                continue;
            }

            switch (key) {


                case 'H':
                    System.out.println("Help:");
                    System.out.println("C - Create new tree;");
                    System.out.println("V - View tree;");
                    System.out.println("S - Simple tree view;");
                    System.out.println("L - Length of the tree (output overall height);");
                    System.out.println("N - Number of elements (output total elements count);");
                    System.out.println("B - Balance tree;");
                    System.out.println("A - Add element;");
                    System.out.println("R - Remove specified element;");
                    System.out.println("D - Delete tree;");
                    System.out.println("Q - Quit program and delete tree.");
                    break;


                case 'C':
                    System.out.println("Create:");
                        System.out.println("\t\"R\" - randomise elements, ");
                        System.out.println("\t\"M\" - manual input, ");
                        System.out.println("\t\"C\" - cancel... ");
                    System.out.println("Note: existing tree would be deleted!");
                    while (true) {
                        key = readKey(reader);
                        if (key == 'R') {
                            if (tree != null) tree.deleteAllTree();
                            tree = new Tree<>();
                            System.out.println("Random tree creation:");
                            System.out.println("Specify number of elements: ");
                            while (!reader.hasNextInt()) {
                                reader.nextLine();
                            }
                            temp = reader.nextInt();
                            // while(reader.hasNext()) reader.nextLine();
                            while (temp-- > 0) tree.addTreeLeaf(random.nextInt(10), null);
                            tree.viewTree();
                            break;
                        }
                        if (key == 'M') {
                            if(tree != null) tree.deleteAllTree();
                            tree = new Tree<>();
                            boolean endOfInput = false;
                            System.out.println("Manual tree creation:");
                            System.out.print("Enter elements via \"Enter\" key ");
                            System.out.println("(type \"end\" to finish):");
                            while(true) {
                                while (!reader.hasNextInt()) {
                                     if(reader.nextLine().equals("end")) {
                                         endOfInput = true;
                                         break;
                                     }
                                }
                                if(endOfInput) break;
                                temp = reader.nextInt();
                                // while(reader.hasNext()) reader.nextLine();
                                tree.addTreeLeaf(temp, null);
                            }
                            tree.viewTree();
                            break;
                        }
                        if (key == 'C') {
                            System.out.println("Canceled.");
                            break;
                        }
                    }
                    System.out.println();
                    break;


                case 'V':
                    System.out.println("View:");
                    tree.viewTree();
                    System.out.println();
                    break;


                case 'S':
                    System.out.println("Simple view:");
                    tree.viewTreeSimply(tree.root);
                    System.out.println();
                    break;


                case 'L':
                    System.out.println("Height of the tree = " + tree.getTreeHeight());
                    break;


                case 'N':
                    System.out.println("Number of elements = " + tree.getNodesCount());
                    break;


                case 'B':
                    tree.balanceTree();
                    System.out.println("The tree was successfully balanced.");
                    break;


                case 'A':
                    System.out.println("Add:");
                    System.out.print("Specify an element to add to the tree: ");
                    while (!reader.hasNextInt()) {
                        reader.nextLine();
                    }
                    temp = reader.nextInt();
                    // while(reader.hasNext()) reader.nextLine();
                    tree.addTreeLeaf(temp, null);
                    System.out.println("An element with value of \"" + temp + "\" was added to the tree.");
                    break;


                case 'R':
                    System.out.println("Remove:");
                    System.out.print("Specify an element to remove from the tree: ");
                    while (!reader.hasNextInt()) {
                        reader.nextLine();
                    }
                    temp = reader.nextInt();
                    // while(reader.hasNext()) reader.nextLine();
                    tree.removeAllSpecifiedTreeNodes(temp);
                    System.out.println("All elements with value of \"" + temp + "\" were deleted from the tree.");
                    break;


                case 'D':
                    tree.deleteAllTree();
                    System.out.println("The tree was successfully deleted.");
                    break;


                case 'Q':
                    //tree = null;
                    return;


                default:
                    System.out.println("Error: Unknown command \"" + key + "\".");
                    break;
            }
        }
    }
}
