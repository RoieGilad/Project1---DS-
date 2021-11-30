import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * AVLTree
 *
 * An implementation of aמ AVL Tree with
 * distinct integer keys and info.
 *
 */


public class AVLTreeT {

	private int maximalJoin;
	private int sumJoin;
	private int numJoin;

	public static void main(String[] args) {
		// put the call fo the method in theoretic part here
		firstQuestion();

	}

	private static int num_of_changes(ArrayList<Integer> lst){ //o(n^2) checks if i>j and a[i] < a[j]
		int cnt = 0;
		for (int j = 0; j < lst.size(); j++){
			for (int i = j+1; i < lst.size(); i++){
				if (lst.get(j) > lst.get(i)){
					cnt += 1;
				}
			}
		}
		return cnt;
	}


	public static void firstQuestion() {
		Random rand = new Random();
		for (int i = 1; i <= 5; i++) {
			ArrayList<Integer> from_max_to_min = new ArrayList<>();
			int size = (int) (1000 * Math.pow(2, i));
			System.out.println("the current size is" + size + " = 1000*2^" + i);
			for (int k = size; k > 0; k--) {                 // insert all the numbers in to array-list
				from_max_to_min.add(k);
			}
			System.out.println("num of changes in the ordered list: " + num_of_changes(from_max_to_min));
			AVLTreeT ordered = new AVLTreeT();
			int cost_of_search_ordered = 0;
			for (int num: from_max_to_min){
				cost_of_search_ordered += ordered.search_for_question1(num);
				ordered.insert(num, "" + num);
			}
			System.out.println("num of search in the ordered list: " + cost_of_search_ordered);


			Collections.shuffle(from_max_to_min);
			System.out.println("num of changes in the shuffled list: " + num_of_changes(from_max_to_min));
			AVLTreeT shuffle = new AVLTreeT();
			int cost_of_search_shuffle = 0;
			for (int num: from_max_to_min){
				cost_of_search_shuffle += shuffle.search_for_question1(num);
				shuffle.insert(num, "" + num);
			}
			System.out.println("num of search in the shuffled list: " + cost_of_search_shuffle);

		}
	}




	private IAVLNodeT root;
	private IAVLNodeT min;
	private IAVLNodeT max;

	public static void secondQuestion(){
		Random rand = new Random();
		for (int i = 1 ; i <= 10 ; i++){
			ArrayList<Integer> numToInsert = new ArrayList<>();
			int size = (int) (1000*Math.pow(2,i));
			System.out.println("the current size is" + size + " = 1000*2^" + i);
			for (int k = 0; k < size ; k++) {                 // insert all the numbers in to array-list
				numToInsert.add(k + 1);}

			AVLTreeT random = new AVLTreeT();
			AVLTreeT rootPredecessor = new AVLTreeT();
			Collections.shuffle(numToInsert);

			for(int j : numToInsert){
				random.insert(j , ""+j);
				rootPredecessor.insert(j , ""+j);}

			System.out.println("random check:");
			int intSplit = 1 + rand.nextInt(size-1);
			System.out.println("the random key is " + intSplit);

			random.split(intSplit);
			System.out.println("random - the maximal join was " + random.getMaximalJoin());
			System.out.println("random - the average join was " + random.getAverageJoin());

			System.out.println("maximum in the left subtree of the root check:");
			int x = rootPredecessor.predecessor(rootPredecessor.getRoot()).getKey();
			System.out.println("the maximum key in the left subtree of the root is " + x);
			rootPredecessor.split(x);
			System.out.println("root predecessor - the maximal join was " + rootPredecessor.getMaximalJoin());
			System.out.println("root predecessor - the average join was " + rootPredecessor.getAverageJoin());

		}
	}

	private double getAverageJoin() {
		return this.sumJoin / this.numJoin;	}

	private int getMaximalJoin() {
		return this.maximalJoin;
	}


	public AVLTreeT(IAVLNodeT r) { // constructor for split
		this.root = r;
		this.min = myMin(r);
		this.max = myMax(r);	}


	public AVLTreeT() {}




	/**
	 * public boolean empty()
	 *
	 * Returns true if and only if the tree is empty.
	 *
	 */
	public boolean empty() {
		return (root == null || !root.isRealNode());// root == null iff the tree is empty
	}


	private int search_for_question1(int k) { //the last node to insert
		// - returns the parent or the node itself if the key already exists, dont call this func if the node is null
		IAVLNodeT search = this.max;
		if (search == null){
			return 0;
		}
		IAVLNodeT position = search;
		int cnt = 0;
		while (search.isRealNode()){
			position = search;
			cnt += 1;
			if (search.getKey() < k){
				search = search.getRight();
			}
			else if (search.getParent() != null && search.getParent().getRight() == search && search.getParent().getKey() > k){
				search = search.getParent();
			} else{
				search = search.getLeft();
			}
		}
		return cnt;
	}




	//insert!!
//first func for insert
	private AVLTreeT.IAVLNodeT place_to_insert(AVLTreeT.IAVLNodeT node) { //the last node to insert
		// - returns the parent or the node itself if the key already exists, dont call this func if the node is null
		AVLTreeT.IAVLNodeT search = this.root;
		AVLTreeT.IAVLNodeT place = search; //always not null
		while (search.isRealNode()) { // TODO	 CHECK IF ISREALNODE WAS THE ONLY PROBLEM
			place = search;
			if (search.getKey() == node.getKey()){
				return search;
			}
			if (search.getKey() > node.getKey()) {
				search = search.getLeft();
			} else  {
				search = search.getRight();
			}

		}
		return place;
	}

	//second func for insert
	private int rebalance(IAVLNodeT node, int d){
		if (node == null){
			return 0;
		}
		int left_dist = node.getRankLeft();
		int right_dist = node.getRankRight();

		if ((left_dist == 0 && right_dist == 1) || (left_dist == 1 && right_dist == 0)) //case A - (1,0)/(0,1) - promote
		{
			node.updateNode(); //promote
			if (node.getParent() != null){
				rebalance(node.getParent(), d+1); //counting 1 promotion
			} else{
				return d+1;
			}

		}
		else if (left_dist == 0 && right_dist == 2) { //case B (0,2) - need a rotation, rebalance complete after
			IAVLNodeT z = node;
			IAVLNodeT x = node.getLeft();
			int left_left_dist = x.getRankLeft();
			if (left_left_dist == 1) { // (1,2)
				rotate_right(x,z);
				z.updateNode(); //demote z + size
				x.updateNode(); //update size
				return d + 2; // one rotation, one demotion

			} else { // (2,1) - double rotation
				IAVLNodeT b = x.getRight();
				rotate_left(x, b);
				rotate_right(b, z);
				x.updateNode();
				z.updateNode(); //set z height
				b.updateNode(); //set b height
				return d + 5; // two rotations, 3 promotions

			}
		}
		else if (right_dist == 0 && left_dist == 2) { //symetric case (2,0)
			IAVLNodeT z = node;
			IAVLNodeT x = node.getRight();
			int right_right_dist = x.getRankRight();
			if (right_right_dist == 1) { //(2,1)
				rotate_left(z, x);
				z.updateNode(); //demote z
				x.updateNode();
				return d + 2;

			} else { //(1,2)
				IAVLNodeT b = x.getLeft();
				rotate_right(b, x);
				rotate_left(z, b);
				x.updateNode();
				z.updateNode(); //set z height
				b.updateNode(); //set b height
				return d + 5;

			}
		}
		return d;
	}

	//third func for insert
	private void rotate_right(IAVLNodeT left_child, IAVLNodeT node) { //gets (x,z) x is the left child of z, changes between them
		node.setLeft(left_child.getRight()); // z left child is b
		node.getLeft().setParent(node); // b's father is z
		if (node.getParent() != null) { //update the parent

			if (node.getParent().getKey() > node.getKey()) { //checks if the parent child is left or right
				node.getParent().setLeft(left_child);
			} else {
				node.getParent().setRight(left_child);
			}

		} else {
			this.root = left_child; //can be a problem with join_in- there is no tree //TODO ????
		}
		left_child.setParent(node.getParent());  //attach the new child to the parent
		left_child.setRight(node); // z is right child of x
		node.setParent(left_child); // x is father of z
	}

	//main func
	public int insert(int k, String i) { //TODO CHECK WHY WE INSERT SAME KEY TWICE
		IAVLNodeT new_node = new AVLNodeT(k, i);
		if (this.empty()) {
			this.root = new_node;
			this.min = new_node;
			this.max = new_node;
			return 0;
		}

		IAVLNodeT place_to_insert = place_to_insert(new_node);
		if (place_to_insert.getKey() == k){
			return -1;
		}
		new_node.setParent(place_to_insert); //attaches the node to the right place
		if (place_to_insert.getKey() > k){ //checks if the node is a right son or left
			place_to_insert.setLeft(new_node);
		} else {
			place_to_insert.setRight(new_node);
		}
		if (k < this.min.getKey()){ //update min
			this.min = new_node;
		}
		if (k > this.max.getKey()){ //update max
			this.max = new_node;
		}
		int result =  rebalance(place_to_insert, 0); // d is the number of operations
		updateTillRoot(place_to_insert); // if the rebalance isnt happening- the problem for size
		return result;
	}



	private void updateTillRoot(IAVLNodeT node){
		while (node.getParent() != null){
			node.updateNode();
			node = node.getParent();
		}
		node.updateNode(); //tal changed- we didnt update the last one
	}



	private IAVLNodeT predecessor(IAVLNodeT node){
		IAVLNodeT check;
		if (node.getLeft().isRealNode()){
			check = node.getLeft();
			while (check.getRight() != null){
				check = check.getRight();
			}
		} else{ //the successor is above
			check = node.getParent();
		}
		while (check.isRealNode() && check.getKey() > node.getKey()) {
			check = check.getParent();
		}
		return check;

	}

	private int inorder_walk_key(IAVLNodeT root, int d, int[] result){
		if (!root.isRealNode()) {
			return d;
		}
		int place = inorder_walk_key(root.getLeft(), d, result);
		result[place] = root.getKey();
		return inorder_walk_key(root.getRight(), place+1, result);
	}

	private int inorder_walk_val(IAVLNodeT root, int d, String[] result){
		if (!root.isRealNode()) {
			return d;
		}
		int place = inorder_walk_val(root.getLeft(), d, result);
		result[place] = root.getValue();
		return inorder_walk_val(root.getRight(), place+1, result);
	}


	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray()
	{
		int[] result = new int[this.root.getSize()];
		inorder_walk_key(this.root, 0, result);
		return result;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray()
	{
		String[] result = new String[this.root.getSize()];
		inorder_walk_val(this.root, 0, result);
		return result;
	}

	/**
	 * public int join(IAVLNodeT x, AVLTree t)
	 *
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	 *
	 * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
	 * postcondition: none
	 */
	public int join(IAVLNodeT x, AVLTreeT t) {
		if (t.empty() && this.empty()) { //both empty
			this.root = x;
			this.max = x;
			this.min = x;
			return 1;
		}
		if (this.empty()){ // this tree empty
			if (t.getRoot().getKey() > x.getKey()){
				this.min = x;
				this.max = t.max;
				this.root = join_in(this.getRoot(), x, t.getRoot());
			} else {
				this.min = t.min;
				this.max = x;
				this.root = join_in(t.getRoot(), x, this.getRoot());
			}
			return t.getRoot().getHeight()+1;
		}
		if (t.empty()){ // t tree is empty
			if (this.getRoot().getKey() > x.getKey()){
				this.min = x;
				this.root = join_in(t.getRoot(), x, this.getRoot());
			} else {
				this.max = x;
				this.root = join_in(this.getRoot(), x, t.getRoot());
			}
			return this.getRoot().getHeight()+1;
		}
		else if (t.root.getKey() < x.getKey()) { //both not empty t is the smallest
			this.min = t.min;
			this.root = join_in(t.getRoot(), x, this.getRoot());
		} else { //both not empty this is the smallest
			this.max = t.max;
			this.root = join_in(this.getRoot(), x, t.getRoot());
		}
		int h_1 = t.getRoot().getHeight();
		int h_2 = this.getRoot().getHeight();
		return Math.abs(h_1-h_2)+1;
	}

	private IAVLNodeT join_in(IAVLNodeT t1, IAVLNodeT X, IAVLNodeT t2){ //(left side- smallest, x, right side- biggest),
		// returns the root!! - the upper node! the join fix the empty cases- join_in takes non empty trees..
		int h_1 = t1.getHeight();
		int h_2 = t2.getHeight();
//		if (!t1.isRealNode() && !t2.isRealNode()){
//			return X;
//		}
//		if (Math.abs(h_1-h_2) <= 1){ //the two trees almost equal - just attach x
//			X.setRight(t1);
//			t1.setParent(X);
//			X.setLeft(t2);
//			t2.setParent(X);
//			return X;
//		}
		if (h_1 < h_2){
			IAVLNodeT node_travel = t2;
			while (node_travel.getHeight() > h_1){
				node_travel = node_travel.getLeft();
			}
			IAVLNodeT parent_to_save = node_travel.getParent(); //save it to attach to x
			X.setLeft(node_travel);
			node_travel.setParent(X);
			parent_to_save.setLeft(X);
			X.setParent(parent_to_save);
			rebalance(parent_to_save, 0);
			updateTillRoot(X);
			return t2; //this is the new root
		} else {
			IAVLNodeT node_travel = t1;
			while (node_travel.getHeight() > h_2){
				node_travel = node_travel.getRight();
			}
			IAVLNodeT parent_to_save = node_travel.getParent(); //save it to attach to x
			X.setRight(node_travel);
			node_travel.setParent(X);
			parent_to_save.setRight(X);
			X.setParent(parent_to_save);
			rebalance(parent_to_save, 0);
			updateTillRoot(X);
			if (h_1==h_2){
				return X;
			}
			return t1; //this is the new root
		}
	}







	/**
	 * public String search(int k)
	 *
	 * Returns the info of an item with key k if it exists in the tree.
	 * otherwise, returns null.
	 */
	public String search(int k){

		IAVLNodeT node = this.find(k); // find searching for node with key = k

		if (node != null){           // if found return the value of the key
			return node.getValue();}
		else{
			return null;}}	// if not return null




	private void rotate_left(IAVLNodeT node1 , IAVLNodeT node2) {
		node1.setRight(node2.getLeft());
		node1.getRight().setParent(node1);
		node2.setLeft(node1);
		if (node1.getParent() != null) {
			if (node1.getParent().getKey() < node1.getKey()){
				node1.getParent().setRight(node2);}
			else{
				node1.getParent().setLeft(node2);}
		} else {
			this.root = node2; //tal changed- new,   update the root
		}
		node2.setParent(node1.getParent());
		node1.setParent(node2);
	}








	/**
	 * public int delete(int k)
	 *
	 * Deletes an item with key k from the binary tree, if it is there.
	 * The tree must remain valid, i.e. keep its invariants.
	 * Returns the number of re-balancing operations, or 0 if no re-balancing operations were necessary.
	 * A promotion/rotation counts as one re-balance operation, double-rotation is counted as 2.
	 * Returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k){
		IAVLNodeT nDelete = this.find(k); // searching for node with key = K that is about to be deleted
		if (nDelete == null){ return -1;} // if node wasn't found return -1

		else{
			if (this.root.getSize() > 1 ){ // updating min or max if necessary
				if (nDelete.getKey() == this.min.getKey()){
					this.min = this.successor(nDelete);}
				if (nDelete.getKey() == this.max.getKey()){
					this.max = this.predecessor(nDelete);}
			}
			IAVLNodeT toBeRebalance = this.deleteRetrieve(nDelete); // delete nDelete and retrieve the node that rebalancing should start from

			if (toBeRebalance == null){return 0;}
			else {
				return this.reBalanceDelete(toBeRebalance,0);}}
	}


// from here if sub Function for the delete function
	/** sub Functions for delete
	 * private int reBalanceDelete(IAVLNodeT node)
	 * takes a node that maybe need to rebalance and rebalance it while counting rotates and promotes/demotes
	 * @return the count of rotates and promotes/demotes
	 */
	private int reBalanceDelete(IAVLNodeT node , int cnt) {
		int L = node.getRankLeft();
		int R = node.getRankRight();
		boolean balanced = false;
		if (L== 3 && R == 1){ // the node that is about to rebalance is in (3,1) condition
			balanced = true;
			IAVLNodeT RightChild = node.getRight();
			int RL = RightChild.getRankLeft();
			int RR = RightChild.getRankRight();

			if (RL == 1 && RR ==1){ // (3,1) condition and the left child is at (1,1) condiotion
				cnt += reBalanceCase3111(node, RightChild, true , cnt );}

			else if (RL == 2 && RR == 1){ // (3,1) condition and the right child is at (2,1) condiotion
				cnt += reBalanceCase3121(node, RightChild , true , cnt );}

			else if (RL == 1 && RR == 2){ // (3,1) condition and the right child is at (1,2) condiotion
				cnt += reBalanceCase3112(node,RightChild,RightChild.getLeft(),true , cnt );}
		}

		else if (L == 1 && R== 3 ){ // the node that is about to rebalance is in (1,3) condition
			balanced = true;
			IAVLNodeT LeftChild = node.getLeft();
			int LL = LeftChild.getRankLeft();
			int LR = LeftChild.getRankRight();

			if (LL == 1 && LR ==1){ // (1,3) condition and the left child is at (1,1) condiotion
				cnt += reBalanceCase3111( node,LeftChild, false , cnt );}

			else if (LL == 2 && LR == 1){ // (1,3) condition and the left child is at (2,1) condiotion
				cnt += reBalanceCase3112(node , LeftChild , LeftChild.getRight() ,false, cnt );}

			else if (LL == 1 && LR == 2){  // (1,3) condition and the left child is at (1,2) condiotion
				cnt += reBalanceCase3121( node,LeftChild, false, cnt  );}
		}

		else if (L == 2 && R == 2){ // the node that is about to rebalance is in (2,2) condition
			balanced = true;
			cnt += reBalanceCase22(node, cnt );}
		if (!balanced){
			this.updateTillRoot(node);
		}
		// the node is balanced
		return cnt ;}


	private int reBalanceCase22(IAVLNodeT node , int cnt) { //rebalance after delete (2,2) case
		node.updateNode(); // demote
		if (this.root == node){ // no need to go and rebalance parent
			return  cnt + 1;}
		else{
			return  reBalanceDelete(node.getParent(), 1+ cnt);}} // check if parent if rebalanced


	private int reBalanceCase3112(IAVLNodeT z, IAVLNodeT y , IAVLNodeT a, boolean left , int cnt ) { //rebalance after delete 31 - 12 / 13 - 21 case
		if (left){
			rotate_right(a,y);	// rotate right on the (a,y) edge
			rotate_left(z,a);// roate left on the (z,a) edge
		}

		else if (!left) {
			rotate_left(y,a);// rotate left on (y,a) edge
			rotate_right(a,z);// rotate right on (a,z)
		}
		z.updateNode();// promotes and demotes
		y.updateNode();
		a.updateNode();

		if (this.root == a){ // there is no need to go up for rebalancing
			return cnt + 6;}
		else { // go up and check if rebalanced
			return  this.reBalanceDelete(a.getParent() , 6 + cnt);}
	}

	private int reBalanceCase3121(IAVLNodeT z, IAVLNodeT y, boolean left, int cnt) { //rebalance after delete 31 - 21 / 13 - 12 case
		if (left){
			rotate_left(z,y);	// rotate left on (z,y) edge
		}
		else if (!left) {
			rotate_right(y,z);// rotate right (y,z) edge
		}
		z.updateNode(); // demote twice z
		if (this.root == y){ // there is no need to go up for rebalancing
			return cnt + 3;}
		else { // go up and check if rebalanced
			return  reBalanceDelete(y.getParent() , 3 + cnt);}
	}


	private int reBalanceCase3111(IAVLNodeT z, IAVLNodeT y, boolean left, int cnt ) { //rebalance after delete 31 - 11 / 13 - 11 case
		if (left) {
			rotate_left(z,y);	}// rotate left on the (z,y) edge

		else {
			rotate_right(y,z);	}// rotate right on the (y,z) edge
		z.updateNode();// demote z
		y.updateNode();// promote y
		return 3;}

	//




	private IAVLNodeT deleteRetrieve(IAVLNodeT nDelete) {
		IAVLNodeT returnNode = null;
		if ((!nDelete.getLeft().isRealNode()) && (!nDelete.getRight().isRealNode())) { // true iff nDelete is Leaf
			returnNode = deleteRetrieveLeaf(nDelete);}

		else if ((nDelete.getLeft().isRealNode()) && (!nDelete.getRight().isRealNode())) { // unary node with only left node
			returnNode = deleteRetrieveLeft(nDelete);}

		else if ((!nDelete.getLeft().isRealNode()) && (nDelete.getRight().isRealNode())) { // unary node with only Right node
			returnNode = deleteRetrieveRight(nDelete);}

		else if ((nDelete.getLeft().isRealNode()) && (nDelete.getRight().isRealNode())) { // nDelete has two child (happy family!)
			returnNode = deleteRetrieveFamily(nDelete);}

		return returnNode;}

	private IAVLNodeT deleteRetrieveFamily(IAVLNodeT nDelete) { // delete & retrieve node to be balanced for node with two children
		IAVLNodeT parent = nDelete.getParent();
		IAVLNodeT mySuccessor = this.successor(nDelete); //finds the successor that will replace the deleted node
		IAVLNodeT tmp = deleteRetrieve(mySuccessor);

		if (tmp.getKey() == nDelete.getKey()){ // special case
			tmp = mySuccessor;


		}

		if (nDelete == this.root) {  // root special case
			this.root = mySuccessor;
			mySuccessor.setParent(null);}

		else {							// updating my parent
			mySuccessor.setParent(parent);

			if (nDelete.getKey() < parent.getKey()) {
				parent.setLeft(mySuccessor);}
			else {
				parent.setRight(mySuccessor);}
		}
		//replacing nDelete with his successor
		mySuccessor.setLeft(nDelete.getLeft());
		mySuccessor.getLeft().setParent(mySuccessor); // left cant be a digital / null
		mySuccessor.setRight(nDelete.getRight());
		mySuccessor.getRight().setParent(mySuccessor);// // right cant be a digital / null
		mySuccessor.setSizeAlone(); // updating successor by the new sons
		mySuccessor.setHeight(nDelete.getHeight()); // keeping old height of a node
		return tmp;}


	private IAVLNodeT deleteRetrieveRight(IAVLNodeT nDelete) { // delete & retrieve node to be balanced for node with one right children
		IAVLNodeT parent = nDelete.getParent();

		if (nDelete == this.root) {  // root special case
			this.root = nDelete.getRight();
			this.root.setParent(null);
			return this.root;}
		else if (nDelete.getKey() < parent.getKey()) { // nDelete is left child of parent
			parent.setLeft(nDelete.getRight()); // byPass
			parent.getLeft().setParent(parent);
			return parent;}
		else {        // nDelete is right child of parent
			parent.setRight(nDelete.getRight()); // byPass
			parent.getRight().setParent(parent);
			return parent;
		}}

	private IAVLNodeT deleteRetrieveLeft(IAVLNodeT nDelete) { // delete & retrieve node to be balance for root with one left children
		IAVLNodeT parent = nDelete.getParent();

		if (nDelete == this.root) {  // root special case
			this.root = nDelete.getLeft();
			this.root.setParent(null);
			return this.root;}

		else if (nDelete.getKey() < parent.getKey()) { // nDelete is left child of parent
			parent.setLeft(nDelete.getLeft()); // byPass
			parent.getLeft().setParent(parent);
			return parent;}

		else {        // nDelete is right child of parent
			parent.setRight(nDelete.getLeft()); // byPass
			parent.getRight().setParent(parent);
			return parent;}}




	private IAVLNodeT deleteRetrieveLeaf(IAVLNodeT nDelete) { // delete & retrieve node to be balanced for node that is a Leaf

		if (nDelete == this.root) {  // root special case
			this.root = null;
			return null;}

		IAVLNodeT parent = nDelete.getParent();

		if (nDelete.getKey() < parent.getKey()) { // nDelete is left child of parent
			parent.setLeft(nDelete.getLeft()); // byPass
			parent.getLeft().setParent(parent);
			return parent;}

		else {        // nDelete is right child of parent
			parent.setRight(nDelete.getRight()); // byPass
			parent.getRight().setParent(parent);
			return parent;
		}}
// till here sub function for the delete function

	private IAVLNodeT successor(IAVLNodeT node) { // finding the successor of a node

		if ( node.getRight().isRealNode()){ // if I have a right child so my successor is on that sub tree
			return  myMin(node.getRight());}

		//else my successor is above me
		IAVLNodeT parent = node.getParent();

		while (parent != null && node == parent.getRight()){ // go up till you come from the left or you are in the root
			node = parent;
			parent = node.getParent(); }
		return parent;}



	private IAVLNodeT myMin(IAVLNodeT node) { // finding the most minimum node in my sub tree
		while (node.getLeft().isRealNode()){ // go left if you can
			node = node.getLeft();}
		return node;}


	private IAVLNodeT myMax(IAVLNodeT node) {
		while ( node.getRight().isRealNode()){
			node = node.getRight();}
		return node;}




	
	/**
	 * public StringBe min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty.
	 */
	public String min()
	{
		return (this.empty()) ? null : min.getValue();
	}

	private IAVLNodeT find(int k){ // finding node with key = K
		IAVLNodeT curr = this.root;

		while (curr != null && curr.isRealNode()){ // check if dead end

			if ( k== curr.getKey()){ //found node with key = K
				return curr;}

			else if (k < curr.getKey()){ // go left
				curr = curr.getLeft();}

			else { curr = curr.getRight();} // go right
		}

		if (curr == null){return null;}
		else if (curr.isRealNode()){ // if found real return the node
			return curr;}
		else { return null;}} // if not return null
	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty.
	 */
	public String max()
	{
		return (this.empty()) ? null : max.getValue();
	}


	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 */
	public int size(){
		if (this.empty()){return 0;}
		return root.getSize(); // to be replaced by student code
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 */
	public IAVLNodeT getRoot() {
		if (this.empty()){
			return null;}
		return root;
	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 *
	 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	 * postcondition: none
	 */
	public AVLTreeT[] split(int x){


		AVLTreeT.IAVLNodeT digital = new AVLTreeT.AVLNodeT();
		AVLTreeT.IAVLNodeT X = this.find(x);
		AVLTreeT.IAVLNodeT L = X.getLeft();
		AVLTreeT.IAVLNodeT R = X.getRight();
		AVLTreeT.IAVLNodeT parent = X.getParent();
		AVLTreeT.IAVLNodeT newParent;
		L.setParent(null);
		R.setParent(null);
		while (parent != null){ 							// move up till you reached the end
			newParent = parent.getParent();					// save for later
			parent.setParent(null);

			if ( parent.getLeft().getKey() == X.getKey() ){ // left side join
				parent.getRight().setParent(null);
				parent.setLeft(digital);
				R = join_in(R , parent , parent.getRight());
				R.updateNode();
				R.setParent(null);}

			else {
				parent.setRight(digital);
				parent.getLeft().setParent(null);
				L = join_in(parent.getLeft(), parent, L);     // right side join
				L.updateNode();
				L.setParent(null);}

			X = parent;										// moving up
			parent = newParent;	}							// moving up
		//	if (L == null){L = digital;}
		//	if (R == null){ R = digital;}

		AVLTreeT left = new AVLTreeT(L);						 // updating all fields
		AVLTreeT right = new AVLTreeT(R);						// updating all fields
		L.updateNode();
		R.updateNode();
		return new AVLTreeT[]{left, right};
	}



	/**
	 * public interface IAVLNodeT
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNodeT{
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNodeT node); // Sets left child.
		public IAVLNodeT getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNodeT node); // Sets right child.
		public IAVLNodeT getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNodeT node); // Sets parent.
		public IAVLNodeT getParent(); // Returns the parent, if there is no parent return null.
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node.
		public void setHeight(int height); // Sets the height of the node.
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes).
		public int getRankLeft();
		public int getRankRight();
		public void setHeightAlone();
		public int getSize();
		public void setSizeAlone();

		void updateNode();
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in another file.
	 *
	 * This class can and MUST be modified (It must implement IAVLNodeT).
	 */
	public static class AVLNodeT implements IAVLNodeT { //TODO inner class static?

		private int key;
		private String info;
		private IAVLNodeT Left;
		private IAVLNodeT Right;
		private int Height;
		private IAVLNodeT parent;
		private int size;
		private static final IAVLNodeT VirtualNode = new AVLNodeT();// same digital node for all real nodes

		public AVLNodeT(Integer key, String info){
			this.key = key;
			this.info = info;
			this.Left = VirtualNode;
			this.Right = VirtualNode;
			this.size = 1;

		}
		public AVLNodeT() { //default constructor for digital node
			this.key = -1;
			this.info = null;
			this.Left = null;
			this.Right = null;
			this.Height = -1;
			this.size = 0;
		}



		public int getKey(){
			return this.key;
		}

		public String getValue(){
			return this.info;		}

		public void setLeft(IAVLNodeT node){
			this.Left = node;	}

		public IAVLNodeT getLeft(){
			return this.Left;
		}


		public void setRight(IAVLNodeT node){
			this.Right = node;}

		public IAVLNodeT getRight() {
			return this.Right;}

		public int getSize() {
			return this.size;
		}


		public void setParent(IAVLNodeT node){
			this.parent = node;
		}
		public IAVLNodeT getParent(){
			return this.parent;
		}

		public boolean isRealNode(){
			return this.key != -1; // node is real iff node.key != -1
		}

		public void setHeight(int height){
			this.Height = height;
		}

		public int getHeight(){return this.Height; }

		@Override
		public int getRankLeft() { // return the the difference rank from left
			return this.Height - this.getLeft().getHeight();
		}

		@Override
		public int getRankRight() { // return the the difference rank from right
			return this.Height - this.getRight().getHeight();
		}

		@Override
		public void setHeightAlone() { //updating the height of a node
			if ( !this.isRealNode()){return;}
			this.Height = Math.max(this.Left.getHeight(), this.Right.getHeight()) +1;
		}

		@Override
		public void setSizeAlone() {
			if ( !this.isRealNode()){return;}
			this.size = this.Left.getSize() + this.Right.getSize() + 1; }

		public void updateNode(){
			this.setHeightAlone();
			this.setSizeAlone();}


}}
  
