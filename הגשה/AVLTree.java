
/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info.
 *
 */
//tal nof
//		i.d. : 313370686
//		username: talnof
//
//roie gilad
//		i.d : 314967878
//		username: roiegilad
//
//

public class AVLTree {
	private IAVLNode root;
	private IAVLNode min;
	private IAVLNode max;


	// Time Complexity O(1)
	public AVLTree(IAVLNode r) { // constructor for split
		this.root = r;
		this.min = myMin(r);
		this.max = myMax(r);	}


	// Time Complexity O(1)
	public AVLTree() {} // default constructor




	/**
	 * public boolean empty()
	 *
	 * Returns true if and only if the tree is empty.
	 *
	 */
	// Time Complexity O(1)
	public boolean empty() {
		return (root == null || !root.isRealNode());// root == null or root is digital iff the tree is empty
	}
	// Time Complexity O(1)
	public IAVLNode getMax() {
		return this.max;
	}
	// Time Complexity O(1)
	public IAVLNode getMin() {
		return this.min;
	}


	//insert!!
//first func for insert
	// Time complexity O(log(this.size()))
	private IAVLNode place_to_insert(IAVLNode node) { //the last node to insert
		// - returns the parent or the node itself if the key already exists, dont call this func if the node is null
		IAVLNode search = this.root;
		IAVLNode place = search; //always not null
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
	// Time complexity O(log(this.size()))
	private int rebalance(IAVLNode node, int d){
		if (node == null){
			return 0;
		}
		int left_dist = node.getRankLeft();
		int right_dist = node.getRankRight();

		if ((left_dist == 0 && right_dist == 1) || (left_dist == 1 && right_dist == 0)) //case A - (1,0)/(0,1) - promote
		{
			node.updateNode(); //promote
			if (node.getParent() != null){
				return rebalance(node.getParent(), d+1); //counting 1 promotion
			} else{
				return d+1;
			}

		}
		else if (left_dist == 0 && right_dist == 2) { //case B (0,2) - need a rotation, rebalance complete after
			IAVLNode z = node;
			IAVLNode x = node.getLeft();
			int left_left_dist = x.getRankLeft();
			int left_right_dist = x.getRankRight();
			if (left_left_dist == 1 && left_right_dist == 2) { // (1,2)
				rotate_right(x,z);
				z.updateNode(); //demote z + size
				x.updateNode(); //update size
				return d + 2; // one rotation, one demotion

			} else if (left_left_dist == 1 && left_right_dist ==1){ // need ??
				rotate_right(x, z);
				x.updateNode();
				if (node.getParent() != null){
					return rebalance(x.getParent(), d+2); //counting 1 promotion
				} else{
					return d+2;
				}
			}

			else { // (2,1) - double rotation
				IAVLNode b = x.getRight();
				rotate_left(x, b);
				rotate_right(b, z);
				x.updateNode();
				z.updateNode(); //set z height
				b.updateNode(); //set b height
				return d + 5; // two rotations, 3 promotions

			}
		}
		else if (right_dist == 0 && left_dist == 2) { //symetric case (2,0)
			IAVLNode z = node;
			IAVLNode x = node.getRight();
			int right_right_dist = x.getRankRight();
			int right_left_dist = x.getRankLeft();
			if (right_right_dist == 1 && right_left_dist == 2) { //(2,1)
				rotate_left(z, x);
				z.updateNode(); //demote z
				x.updateNode();
				return d + 2;

			} else if (right_right_dist == 1 && right_left_dist ==1){ //?? need
				rotate_left(z, x);
				x.updateNode();
				if (node.getParent() != null){
					return rebalance(x.getParent(), d+2); //counting 1 promotion
				} else{
					return d+2;
				}
			}
			else { //(1,2)
				IAVLNode b = x.getLeft();
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
	// Time Complexity O(1)
	private void rotate_right(IAVLNode left_child, IAVLNode node){ //gets (x,z) x is the left child of z, changes between them
		node.setLeft(left_child.getRight()); // z left child is b
		node.getLeft().setParent(node); // b's father is z
		if (node.getParent() != null){ //update the parent

			if (node.getParent().getKey() > node.getKey()){ //checks if the parent child is left or right
				node.getParent().setLeft(left_child);
			} else {
				node.getParent().setRight(left_child);
			}

		} else{
			this.root = left_child; //can be a problem with join_in- there is no tree //TODO ????
		}
		left_child.setParent(node.getParent());  //attach the new child to the parent
		left_child.setRight(node); // z is right child of x
		node.setParent(left_child); // x is father of z

	}



	//main func
	// Time Complexity O(Log(this.size())
	public int insert(int k, String i) { //TODO CHECK WHY WE INSERT SAME KEY TWICE
		IAVLNode new_node = new AVLNode(k, i);
		if (this.empty()) {
			this.root = new_node;
			this.min = new_node;
			this.max = new_node;
			return 0;
		}

		IAVLNode place_to_insert = place_to_insert(new_node);
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


	// Time Complexity O(Log(this.size())
	private void updateTillRoot(IAVLNode node){
		while (node !=  null){
			node.updateNode();
			node = node.getParent();
		}
	}


	// Time Complexity O(Log(this.size())
	private IAVLNode predecessor(IAVLNode node){
		IAVLNode check;
		if (node.getLeft().isRealNode()){
			check = myMax(node.getLeft());
			}
		 else{ //the successor is above
			check = node.getParent();
		}
		while (check == null && check.getKey() > node.getKey()) {
			check = check.getParent();
		}
		return check;

	}
	// Time Complexity O(this.size())
	private int inorder_walk_key(IAVLNode root, int d, int[] result){
		if (!root.isRealNode()) {
			return d;
		}
		int place = inorder_walk_key(root.getLeft(), d, result);
		result[place] = root.getKey();
		return inorder_walk_key(root.getRight(), place+1, result);
	}

	// Time Complexity O(this.size())
	private int inorder_walk_val(IAVLNode root, int d, String[] result){
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
	// Time Complexity O(this.size())
	public int[] keysToArray()
	{
		if (this.empty()){
			return  new int[0];}

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
	// Time Complexity O(this.size())
	public String[] infoToArray(){
		if (this.empty()){
			return  new String[0];}
		String[] result = new String[this.root.getSize()];
		inorder_walk_val(this.root, 0, result);
		return result;
	}

	/**
	 * public int join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	 *
	 * precondition: keys(t) < x < keys() or keys(t) > x > keys(). t/tree might be empty (rank = -1).
	 * postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {
		if (t.empty() && this.empty()) { //both empty
			this.root = x;
			this.max = x;
			this.min = x;
			return 1;
		}
		if (this.empty()){ // this tree empty
			IAVLNode fictive_node = new AVLNode();
			if (t.getRoot().getKey() > x.getKey()){
				this.min = x;
				this.max = t.getMax();
				this.root = join_in(fictive_node, x, t.getRoot());
			} else {
				this.min = t.getMin();
				this.max = x;
				this.root = join_in(t.getRoot(), x, fictive_node);
			}
			return t.getRoot().getHeight()+1;
		}
		if (t.empty()){ // t tree is empty
			IAVLNode fictive_node = new AVLNode();
			if (this.getRoot().getKey() > x.getKey()){
				this.min = x;
				this.root = join_in(fictive_node, x, this.getRoot());
			} else {
				this.max = x;
				this.root = join_in(this.getRoot(), x, fictive_node);
			}
			return this.getRoot().getHeight()+1;
		}
		else if (t.getRoot().getKey() < x.getKey()) { //both not empty t is the smallest
			this.min = t.getMin();
			this.root = join_in(t.getRoot(), x, this.getRoot());
		} else { //both not empty this is the smallest
			this.max = t.getMax();
			this.root = join_in(this.getRoot(), x, t.getRoot());
		}
		int h_1 = t.getRoot().getHeight();
		int h_2 = this.getRoot().getHeight();
		return Math.abs(h_1-h_2)+1;
	}

	// Time complexity O(log(|this.size()-t.size()|+1))
	private IAVLNode join_in(IAVLNode t1, IAVLNode X, IAVLNode t2){ //(left side- smallest, x, right side- biggest),
		// returns the root!! - the upper node! the join fix the empty cases- join_in takes non empty trees..
		int h_1 = t1.getHeight();
		int h_2 = t2.getHeight();
		if (!t1.isRealNode() && !t2.isRealNode()){
			return X;
		}
		if (h_1 == h_2){ //the two trees equal - just attach x
			X.setLeft(t1);
			t1.setParent(X);
			X.setRight(t2);
			t2.setParent(X);
			X.updateNode();
			return X;
		}
		if (h_1 < h_2){
			X.setLeft(t1);
			t1.setParent(X);
			IAVLNode node_travel = t2;
			IAVLNode saver = node_travel;
			while (node_travel.getHeight() > h_1){
				saver = node_travel;
				node_travel = node_travel.getLeft();
			}
			IAVLNode parent_to_save = saver; //save it to attach to x
			X.setRight(node_travel);
			node_travel.setParent(X);
			parent_to_save.setLeft(X);
			X.setParent(parent_to_save);
			X.updateNode();
			rebalance(parent_to_save, 0);
			updateTillRoot(X);
			return find_root(t2); //this is the new root
		} else {
			X.setRight(t2);
			t2.setParent(X);
			IAVLNode node_travel = t1;
			IAVLNode saver = node_travel;
			while (node_travel.getHeight() > h_2){
				saver = node_travel;
				node_travel = node_travel.getRight();
			}
			IAVLNode parent_to_save = saver; //save it to attach to x
			X.setLeft(node_travel);
			node_travel.setParent(X);
			parent_to_save.setRight(X);
			X.setParent(parent_to_save);
			X.updateNode();
			rebalance(parent_to_save, 0);
			updateTillRoot(X);
			return find_root(t1); //this is the new root
		}
	}

	// Time complexity O(log(|this.size()-t.size()|+1))
	private IAVLNode find_root(IAVLNode node){
		while (node.getParent() != null){
			node = node.getParent();
		}
		return node;
	}




	/**
	 * public String search(int k)
	 *
	 * Returns the info of an item with key k if it exists in the tree.
	 * otherwise, returns null.
	 */
	// Time Complexity O(Log(this.size())
	public String search(int k){

		IAVLNode node = this.find(k); // find searching for node with key = k

		if (node != null){           // if found return the value of the key
			return node.getValue();}
		else{
			return null;}}	// if not return null



	// Time Complexity O(1)
	private void rotate_left(IAVLNode node1 , IAVLNode node2) {
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
	// Time Complexity O(Log(this.size())
	public int delete(int k){
		IAVLNode nDelete = this.find(k); // searching for node with key = K that is about to be deleted
		if (nDelete == null){ return -1;} // if node wasn't found return -1

		else{
			if (this.root.getSize() > 1 ){ // updating min or max if necessary
				if (nDelete.getKey() == this.min.getKey()){
					this.min = this.successor(nDelete);}
				if (nDelete.getKey() == this.max.getKey()){
					this.max = this.predecessor(nDelete);}
			}
			IAVLNode toBeRebalance = this.deleteRetrieve(nDelete); // delete nDelete and retrieve the node that rebalancing should start from

			if (toBeRebalance == null){return 0;}
			else {
				return this.reBalanceDelete(toBeRebalance,0);}}
	}


// from here if sub Function for the delete function
	/** sub Functions for delete
	 * private int reBalanceDelete(IAVLNode node)
	 * takes a node that maybe need to rebalance and rebalance it while counting rotates and promotes/demotes
	 * @return the count of rotates and promotes/demotes
	 */
	// Time Complexity O(Log(this.size())
	private int reBalanceDelete(IAVLNode node , int cnt) {
		int L = node.getRankLeft();
		int R = node.getRankRight();
		boolean balanced = false;
		if (L== 3 && R == 1){ // the node that is about to rebalance is in (3,1) condition
			balanced = true;
			IAVLNode RightChild = node.getRight();
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
			IAVLNode LeftChild = node.getLeft();
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

	// Time Complexity O(1)
	private int reBalanceCase22(IAVLNode node , int cnt) { //rebalance after delete (2,2) case
		node.updateNode(); // demote
		if (this.root == node){ // no need to go and rebalance parent
			this.updateTillRoot(node);
			return  cnt + 1;}
		else{
			return  reBalanceDelete(node.getParent(), 1+ cnt);}} // check if parent if rebalanced

	// Time Complexity O(1)
	private int reBalanceCase3112(IAVLNode z, IAVLNode y , IAVLNode a, boolean left , int cnt ) { //rebalance after delete 31 - 12 / 13 - 21 case
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
			this.updateTillRoot(a);
			return cnt + 6;}
		else { // go up and check if rebalanced
			return  this.reBalanceDelete(a.getParent() , 6 + cnt);}
	}
	// Time Complexity O(1)
	private int reBalanceCase3121(IAVLNode z, IAVLNode y, boolean left, int cnt) { //rebalance after delete 31 - 21 / 13 - 12 case
		if (left){
			rotate_left(z,y);	// rotate left on (z,y) edge
		}
		else if (!left) {
			rotate_right(y,z);// rotate right (y,z) edge
		}
		z.updateNode(); // demote twice z
		y.updateNode(); //TODO UPDATE COST
		if (this.root == y){ // there is no need to go up for rebalancing
			this.updateTillRoot(y);
			return cnt + 3;}
		else { // go up and check if rebalanced
			return  reBalanceDelete(y.getParent() , 3 + cnt);}
	}

	// Time Complexity O(1)
	private int reBalanceCase3111(IAVLNode z, IAVLNode y, boolean left, int cnt ) { //rebalance after delete 31 - 11 / 13 - 11 case
		if (left) {
			rotate_left(z,y);	}// rotate left on the (z,y) edge

		else {
			rotate_right(y,z);	}// rotate right on the (y,z) edge
		z.updateNode();// demote z
		y.updateNode();// promote y
		this.updateTillRoot(y);
		return 3;}

	//



	// Time Complexity O(Log(this.size())
	private IAVLNode deleteRetrieve(IAVLNode nDelete) {
		IAVLNode returnNode = null;
		if ((!nDelete.getLeft().isRealNode()) && (!nDelete.getRight().isRealNode())) { // true iff nDelete is Leaf
			returnNode = deleteRetrieveLeaf(nDelete);}

		else if ((nDelete.getLeft().isRealNode()) && (!nDelete.getRight().isRealNode())) { // unary node with only left node
			returnNode = deleteRetrieveLeft(nDelete);}

		else if ((!nDelete.getLeft().isRealNode()) && (nDelete.getRight().isRealNode())) { // unary node with only Right node
			returnNode = deleteRetrieveRight(nDelete);}

		else if ((nDelete.getLeft().isRealNode()) && (nDelete.getRight().isRealNode())) { // nDelete has two child (happy family!)
			returnNode = deleteRetrieveFamily(nDelete);}

		return returnNode;}
	// Time Complexity O(Log(this.size())
	private IAVLNode deleteRetrieveFamily(IAVLNode nDelete) { // delete & retrieve node to be balanced for node with two children
		IAVLNode parent = nDelete.getParent();
		IAVLNode mySuccessor = this.successor(nDelete); //finds the successor that will replace the deleted node
		IAVLNode tmp = deleteRetrieve(mySuccessor);

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

	// Time Complexity O(1)
	private IAVLNode deleteRetrieveRight(IAVLNode nDelete) { // delete & retrieve node to be balanced for node with one right children
		IAVLNode parent = nDelete.getParent();

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
	// Time Complexity O(1)
	private IAVLNode deleteRetrieveLeft(IAVLNode nDelete) { // delete & retrieve node to be balance for root with one left children
		IAVLNode parent = nDelete.getParent();

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



	// Time Complexity O(1)
	private IAVLNode deleteRetrieveLeaf(IAVLNode nDelete) { // delete & retrieve node to be balanced for node that is a Leaf

		if (nDelete == this.root) {  // root special case
			this.root = null;
			return null;}

		IAVLNode parent = nDelete.getParent();

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


// Time Complexity O(Log(this.size())
	private IAVLNode successor(IAVLNode node) { // finding the successor of a node

		if ( node.getRight().isRealNode()){ // if I have a right child so my successor is on that sub tree
			return  myMin(node.getRight());}

		//else my successor is above me
		IAVLNode parent = node.getParent();

		while (parent != null && node == parent.getRight()){ // go up till you come from the left or you are in the root
			node = parent;
			parent = node.getParent(); }
		return parent;}


	// Time Complexity O(Log(this.size())
	private IAVLNode myMin(IAVLNode node) { // finding the most minimum node in my sub tree
		if (node.isRealNode()){
			while (node.getLeft().isRealNode()){ // go left if you can
				node = node.getLeft();}}
		return node;}

	// Time Complexity O(Log(this.size())
	private IAVLNode myMax(IAVLNode node) {  // finding the most maximal node in my sub tree
		if (node.isRealNode()){
		while ( node.getRight().isRealNode()){ // go right if you can
			node = node.getRight();}}
		return node;}




	
	/**
	 * public StringBe min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty.
	 */
	// Time Complexity O(1)
	public String min()	{return (this.empty()) ? null : min.getValue();	}


	// Time Complexity O(Log(this.size())
	private IAVLNode find(int k){ // finding node with key = K
		IAVLNode curr = this.root;

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
	// Time Complexity O(1)
	public String max()	{return (this.empty()) ? null : max.getValue();	}


	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 */
	// Time Complexity O(1)
	public int size(){
		if (this.empty()){return 0;}
		return root.getSize(); 	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 */
	// Time Complexity O(1)
	public IAVLNode getRoot() {
		if (this.empty()){
			return null;}
		return root;	}

	/**
	 * public AVLTree[] split(int x)
	 *
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 *
	 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	 * postcondition: none
	 */
	// Time Complexity O(Log(this.size())
	public AVLTree[] split(int x){


		IAVLNode digital = new AVLNode();
		IAVLNode X = this.find(x);
		IAVLNode L = X.getLeft();
		IAVLNode R = X.getRight();
		IAVLNode parent = X.getParent();
		IAVLNode newParent;
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

		AVLTree left = new AVLTree(L);						 // updating all fields
		AVLTree right = new AVLTree(R);						// updating all fields
		L.updateNode();
		R.updateNode();
		return new AVLTree[]{left, right};
	}




	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{
		public int getKey(); // Returns node's key (for virtual node return -1).
		public String getValue(); // Returns node's value [info], for virtual node returns null.
		public void setLeft(IAVLNode node); // Sets left child.
		public IAVLNode getLeft(); // Returns left child, if there is no left child returns null.
		public void setRight(IAVLNode node); // Sets right child.
		public IAVLNode getRight(); // Returns right child, if there is no right child return null.
		public void setParent(IAVLNode node); // Sets parent.
		public IAVLNode getParent(); // Returns the parent, if there is no parent return null.
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
	 * This class can and MUST be modified (It must implement IAVLNode).
	 */
	public static class AVLNode implements IAVLNode { //TODO inner class static?

		private int key;
		private String info;
		private IAVLNode Left;
		private IAVLNode Right;
		private int Height;
		private IAVLNode parent;
		private int size;
		private static final IAVLNode VirtualNode = new AVLNode();// same digital node for all real nodes

		// Time Complexity O(1)
		public AVLNode(Integer key, String info){
			this.key = key;
			this.info = info;
			this.Left = VirtualNode;
			this.Right = VirtualNode;
			this.size = 1;

		}

		// Time Complexity O(1)
		public AVLNode() { //default constructor for digital node
			this.key = -1;
			this.info = null;
			this.Left = null;
			this.Right = null;
			this.Height = -1;
			this.size = 0;
		}


		// Time Complexity O(1)
		public int getKey(){
			return this.key;
		}


		// Time Complexity O(1)
		public String getValue(){
			return this.info;		}
		// Time Complexity O(1)
		public void setLeft(IAVLNode node){
			this.Left = node;	}
		// Time Complexity O(1)
		public IAVLNode getLeft(){
			return this.Left;
		}

		// Time Complexity O(1)
		public void setRight(IAVLNode node){
			this.Right = node;}

		// Time Complexity O(1)
		public IAVLNode getRight() {
			return this.Right;}

		// Time Complexity O(1)
		public int getSize() {
			return this.size;
		}

		// Time Complexity O(1)
		public void setParent(IAVLNode node){
			this.parent = node;
		}

		// Time Complexity O(1)
		public IAVLNode getParent(){
			return this.parent;
		}

		// Time Complexity O(1)
		public boolean isRealNode(){
			return this.key != -1; // node is real iff node.key != -1
		}

		// Time Complexity O(1)
		public void setHeight(int height){
			this.Height = height;
		}
		// Time Complexity O(1)
		public int getHeight(){return this.Height; }


		// Time Complexity O(1)
		@Override
		public int getRankLeft() { // return the the difference rank from left
			return this.Height - this.getLeft().getHeight();
		}

		// Time Complexity O(1)
		@Override
		public int getRankRight() { // return the the difference rank from right
			return this.Height - this.getRight().getHeight();
		}

		// Time Complexity O(1)
		@Override
		public void setHeightAlone() { //updating the height of a node
			if ( !this.isRealNode()){return;} // digital node - go back
			this.Height = Math.max(this.Left.getHeight(), this.Right.getHeight()) +1;
		}
		// Time Complexity O(1)
		@Override
		public void setSizeAlone() {
			if ( !this.isRealNode()){return;} // digital node - go back
			this.size = this.Left.getSize() + this.Right.getSize() + 1; }

		// Time Complexity O(1)
		public void updateNode(){
			this.setHeightAlone();
			this.setSizeAlone();}


	}

}
  
