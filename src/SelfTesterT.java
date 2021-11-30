import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelfTesterT {

    public static void main(String[] args) {
        insertDeleteTester(10, 10,10 );
        joinSpliteTester(10 , 10);


    }







    private static void joinSpliteTester(int s, int reptitions) {
        int size =(int) Math.pow(2,s);
        boolean error = false;

        Random rand = new Random();
        int cnt = 0 ;
        while ( !error && (cnt < reptitions)){
            ArrayList<Integer> numToInsert = new ArrayList<>();
            ArrayList<Integer> numToInsertLeft = new ArrayList<>();
            ArrayList<Integer> numToInsertRight = new ArrayList<>();
            int intSplit = 1 + rand.nextInt(size);           // a random node X s.t. left.keys < X < right.keys for split & join
            System.out.println("the splitter / join index is " + intSplit);
            for (int i = 0; i < size ; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);

                if (i+1 < intSplit ){                      // insert the low keys here
                    numToInsertLeft.add( i+1 );}
                if (intSplit < i +1 ){                     // intsplite stay in the side
                    numToInsertRight.add( i+1 );}           // insert the high key here

            }
            // mixing the insert order
            Collections.shuffle(numToInsertLeft);
            Collections.shuffle(numToInsertRight);
            Collections.shuffle(numToInsert);

            AVLTreeT left = new AVLTreeT();
            AVLTreeT right = new AVLTreeT();
            AVLTreeT treeInCheck = null;
            for (int i : numToInsertLeft) { // creating left tree
                left.insert(i, ""+i);}

            for (int i : numToInsertRight) { // creating right tree
                right.insert(i, ""+i);}

            AVLTreeT.IAVLNodeT X = new AVLTreeT.AVLNodeT(intSplit , ""+intSplit);
            double coin = rand.nextFloat();
            if (coin < 0.5){
                left.join(X , right);
                treeInCheck = left; }
            else {
                right.join(X , left);
                treeInCheck = right ; }

            if (!SelfTesterT.checkBalanceOfTree(treeInCheck.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in join - in balanced" );
                error = true;
            }
            if (!SelfTesterT.checkOrderingOfTree(treeInCheck.getRoot())){          // checking if the tree is a BST
                System.out.println("error in join - in order" );
                error = true;}

            if (!(treeInCheck.min().equals("1")) ){
                System.out.println("error in join - in minimum");
                error = true;}
            if (!(treeInCheck.max().equals("" + size)) ){
                System.out.println("error in join - in maximum");
                error = true;}
            if ( !(treeInCheck.size() == size)){
                System.out.println("error in join - in maximum");
                error = true;}


            // test split
            AVLTreeT mainTree = new AVLTreeT();
//        System.out.println(numToInsert);
            int[] insert = {1, 6, 2, 7, 4, 8, 5, 3};
            intSplit = 8;
            for (int i : numToInsert) { // creating left tree
                mainTree.insert(i, ""+i);}

            AVLTreeT[] arr = mainTree.split(intSplit); // lets split
            AVLTreeT leftTree = arr[0];
            AVLTreeT rightTree = arr[1];

            if (leftTree.getRoot() != null && !SelfTesterT.checkBalanceOfTree(leftTree.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in left tree split - in balanced" );
                error = true;
            }
            if (leftTree.getRoot() != null && !SelfTesterT.checkOrderingOfTree(leftTree.getRoot())){          // checking if the tree is a BST
                System.out.println("error in left tree split - in order" );
                error = true;}

            if (leftTree.getRoot() != null && !(leftTree.min().equals("1")) ){
                System.out.println("the current minimum is " + leftTree.min());
                System.out.println("error in left tree in split - in minimum");
                error = true;}
            if (leftTree.getRoot() != null && !(leftTree.max().equals("" + (intSplit - 1))) ){
                System.out.println("error in left tree in split - in maximum");
                error = true;}
            if (leftTree.getRoot() != null && !(leftTree.size() == (intSplit - 1))){
                System.out.println("error in left tree in split - in size");
                error = true;}


            if (rightTree.getRoot() != null && !SelfTesterT.checkBalanceOfTree(rightTree.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in right tree split - in balanced" );
                error = true;
            }
            if (rightTree.getRoot() != null && !SelfTesterT.checkOrderingOfTree(rightTree.getRoot())){          // checking if the tree is a BST
                System.out.println("error in right tree split - in order" );
                error = true;}

            if (rightTree.getRoot() != null && !(rightTree.min().equals(("" + (intSplit + 1)))) ){
                System.out.println("the current minimum is " + rightTree.min());
                System.out.println("error in right tree in split - in minimum");
                error = true;}
            if (rightTree.getRoot() != null && !(rightTree.max().equals("" + size) )){
                System.out.println("error in right tree in split - in maximum");
                error = true;}
            if (rightTree.getRoot() != null && !(rightTree.size() == (size - intSplit))){
                System.out.println("error in right tree split - in size");
                error = true;}

            cnt++;
        }}



    public static void  insertDeleteTester(int s , int delete , int reptitions){
        boolean errorDelete = false;
        int rep_counter = 0;

        while (!errorDelete && rep_counter < reptitions   ){                               // while there is no error - keep testing
            int size =(int) Math.pow(2,s);                     // set the size of your tree
            int numberOfDelete = (int) Math.pow(2,delete) -1;         // set the number of delete
            ArrayList<Integer> numToInsert = new ArrayList<>();
            for (int i = 0; i < size ; i++) {                 // insert all the numbers in to array-list
                numToInsert.add(i + 1);
            }
            //       numToInsert.add(size - 1);                           // double insert
            Collections.shuffle(numToInsert);                    // make the list in a random order
            //      System.out.println(numToInsert);                  // print the list if you want
            AVLTreeT testing = new AVLTreeT();
            for (int i : numToInsert) {
                testing.insert(i, ""+i);
//           if (i % 2 == 0) {                              // check every second insert that the tree is balanced
//               if (!SelfTester.checkBalanceOfTree(testing.getRoot())) {
//                   System.out.println("error in balance");
            }



            //      System.out.println(testing.getRoot().getRight().getSize());        // insanity checks
            //      System.out.println(testing.getRoot().getLeft().getSize());
            //    System.out.println(testing.size());
            //     System.out.println(testing.getRoot().getHeight());

            if (testing.getRoot() != null && !SelfTesterT.checkBalanceOfTree(testing.getRoot())) {          // checking if the tree is balanced
                System.out.println("error in insert - in balanced" );}
            if (testing.getRoot() != null && !SelfTesterT.checkOrderingOfTree(testing.getRoot())){          // checking if the tree is a BST
                System.out.println("error in insert - in order" );}


//            TreePrinter.print(testing.getRoot());
            else {
                System.out.println("insert work");}

            Collections.shuffle(numToInsert);                                 // shuffle the list again for random order in delete
            int[] numToDelete = {13,11};
            int cnt = 0;
            for (int i : numToInsert) {
                //      System.out.println(i);                                         // if you want to see the number that is being deleted
                testing.delete(i);
                //         System.out.println(i);
                cnt++;

                if (testing.getRoot() != null && !SelfTesterT.checkBalanceOfTree(testing.getRoot())) {           // checking if the tree is balanced
                    errorDelete = true;
                    System.out.println("error in delete" + i +"- balanced"); }

                if (testing.getRoot() != null && !SelfTesterT.checkOrderingOfTree(testing.getRoot())){
                    errorDelete = true;
                    System.out.println("error in delete  - in order" + i);}    // checking if the tree is a BST
                // print

                if (cnt > numberOfDelete ){
                    break;}
            }
            if (!errorDelete){
                System.out.println("delete work");}
            else{
                System.out.println("delete failed");}

            rep_counter++;
        }}


    public static  boolean checkBalanceOfTree(AVLTreeT.IAVLNodeT current) {
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

    private static int getDepth(AVLTreeT.IAVLNodeT n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.getRight() != null)
            rightHeight = getDepth(n.getRight());
        if (n.getLeft() != null)
            leftHeight = getDepth(n.getLeft());

        return Math.max(rightHeight, leftHeight) + 1;
    }

    public static boolean checkOrderingOfTree(AVLTreeT.IAVLNodeT current) {
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


