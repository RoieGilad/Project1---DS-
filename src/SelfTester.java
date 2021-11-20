import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfTester {

    public static void main(String[] args) {
        int size = 2 ^ 6;
        ArrayList<Integer> numToInsert = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            numToInsert.add(i + 1);
        }
        numToInsert.add(size - 1); // duble insert
        Collections.shuffle(numToInsert);

        AVLTree testing = new AVLTree();
        for (int i : numToInsert) {
            testing.insert(i, "test");
            if (!checkBalanceOfTree(testing.getRoot())) {
                System.out.println("error in insert" + i);
          //      TreePrinter.print(testing.getRoot());
            }
        }
        Collections.shuffle(numToInsert);
        for (int i : numToInsert) {
            testing.delete(i);
            if (!checkBalanceOfTree(testing.getRoot())) {
                System.out.println("error in delete" + i);
                // print
            }

        }
    }

    public static  boolean checkBalanceOfTree(AVLTree.IAVLNode current) {
        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;
        if (current.getRight() != null) {
            balancedRight = checkBalanceOfTree(current.getRight());
            rightHeight = getDepth(current.getRight());
        }
        if (current.getLeft() != null) {
            balancedLeft = checkBalanceOfTree(current.getLeft());
            leftHeight = getDepth(current.getLeft());
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }

    private static int getDepth(AVLTree.IAVLNode n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight() != null)
            rightHeight = getDepth(n.getRight());
        if (n.getLeft() != null)
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight) + 1;
    }
}


