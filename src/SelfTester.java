import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfTester {

    public static void main(String[] args) {
        int size =(int) Math.pow(2,5);
        int numberOfDelete = 10;
        ArrayList<Integer> numToInsert = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            numToInsert.add(i + 1);
        }
        numToInsert.add(size - 1); // double insert
        Collections.shuffle(numToInsert);
        System.out.println(numToInsert);
//        int[] numToInsert = {1,2,3, 4};
        AVLTree testing = new AVLTree();
        for (int i : numToInsert) {
            testing.insert(i, "test");
            if (i % 2 == 0) {
                if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
                    System.out.println("error in balance");
                }

            }
        }
        System.out.println(testing.getRoot().getRight().getSize());
        System.out.println(testing.getRoot().getLeft().getSize());
        System.out.println(testing.size());
      if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
          System.out.println("error in insert" );}
//            TreePrinter.print(testing.getRoot());
      else {
          System.out.println("insert work");}

  //      Collections.shuffle(numToInsert);
//     boolean errorDelete = false;
//     int cnt = 0;
//     for (int i : numToInsert) {
//         testing.delete(i);
//         cnt++;
//       if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
//           errorDelete = true;
//           System.out.println("error in delete" + i);
//           //      TreePrinter.print(testing.getRoot());
//
//           // print
//       }
//         if (cnt > numberOfDelete ){
//             break;}
//  }
//         if (!errorDelete){
//         System.out.println("delete work");}

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


