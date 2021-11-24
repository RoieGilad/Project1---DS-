import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelfTester {

    public static void main(String[] args) {
        boolean errorDelete = false;

        while (!errorDelete){
        int size =(int) Math.pow(2,15);
        int numberOfDelete = (int) Math.pow(2,10);
       ArrayList<Integer> numToInsert = new ArrayList<>();
       for (int i = 0; i < size - 1; i++) {
           numToInsert.add(i + 1);
       }
       numToInsert.add(size - 1); // double insert
       Collections.shuffle(numToInsert);
  //      System.out.println(numToInsert);
       int[] numToInsertByHand = {6, 1, 9, 12, 11, 4, 8, 13, 14, 15, 2, 5, 3, 15, 10, 7};
        AVLTree testing = new AVLTree();
        for (int i : numToInsert) {
            testing.insert(i, ""+i);
//           if (i % 2 == 0) {
//               if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
//                   System.out.println("error in balance");
                }



 //      System.out.println(testing.getRoot().getRight().getSize());
 //      System.out.println(testing.getRoot().getLeft().getSize());
   //    System.out.println(testing.size());
  //     System.out.println(testing.getRoot().getHeight());

      if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
          System.out.println("error in insert - in balanced" );}
      if (!SelfTester.checkOrderingOfTree(testing.getRoot())){
          System.out.println("error in insert - in order" );}


//            TreePrinter.print(testing.getRoot());
      else {
          System.out.println("insert work");}

      Collections.shuffle(numToInsert);
      int[] numToDelete = {13,11};
   int cnt = 0;
   for (int i : numToInsert) {
 //      System.out.println(i);
       testing.delete(i);
       cnt++;

     if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
         errorDelete = true;
         System.out.println("error in delete" + i +"- balanced"); }
         //      TreePrinter.print(testing.getRoot());if (!SelfTester.checkOrderingOfTree(testing.getRoot())){
          if (!SelfTester.checkOrderingOfTree(testing.getRoot())){
             errorDelete = true;
             System.out.println("error in delete  - in order" + i);}
         // print

       if (cnt > numberOfDelete ){
           break;}
}
       if (!errorDelete){
       System.out.println("delete work");}
       else{
           System.out.println("delete failed");}

    }}




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

    public static boolean checkOrderingOfTree(AVLTree.IAVLNode current) {
        if (current.getLeft().isRealNode()) {
            if (Integer.parseInt(current.getLeft().getValue()) > Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getLeft());
        } else if (current.getRight().isRealNode()) {
            if (Integer.parseInt(current.getRight().getValue()) < Integer.parseInt(current.getValue()))
                return false;
            else
                return checkOrderingOfTree(current.getRight());
        } else if (!current.getLeft().isRealNode() && !current.getRight().isRealNode())
            return true;

        return true;
    }
}


