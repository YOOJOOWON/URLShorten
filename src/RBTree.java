import java.util.ArrayList;

public class RBTree {
    private final boolean RED = true;
    private final boolean BLACK = false;
    private final Node nil = new Node(-1);
    private final int minSize = 60;
    private final int maxSize = 195051;

    public Node root = nil;
    
    public ArrayList<Node> listForOutput = new  ArrayList<Node>();

    class Node {
        public int val;
        public Node left = nil, right = nil, parent = nil;
        public boolean color = BLACK;
                
        public Node(int newval) {
            val = newval;
        }
        
        public String name;
    }

    public RBTree() {
    }

    public void insert(Node tree, int n, String name) {
        Node z = new Node(n);
        Node y = nil;
        Node x = root;

        while (x != nil) {
            y = x;
            if (z.val < x.val)
                x = x.left;
            else x = x.right;
        }
        z.parent = y;

        if (y == nil) {
            root = z;
            z.color = BLACK;
            z.parent = nil;
        } else if (z.val < y.val)
            y.left = z;
        else y.right = z;
        z.left = nil;
        z.right = nil;
        z.color = RED;
        z.name = name;
        insert_fixup(z);
    }
    
    public void insert_fixup(Node z) {
        while (z.parent.color == RED) {
            Node y = nil;
            if (z.parent == z.parent.parent.left) {
                y = z.parent.parent.right;

                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }
                else if (z == z.parent.right) {
                    z = z.parent;
                    left_rotate(z);
                }
                z.parent.color = BLACK;
                z.parent.parent.color = RED;
                right_rotate(z.parent.parent);
            } else {
                y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                    continue;
                }
                else if (z == z.parent.left) {
                    z = z.parent;
                    right_rotate(z);
                }
                z.parent.color = BLACK;
                z.parent.parent.color = RED;
                left_rotate(z.parent.parent);
            }
        }
        root.color = BLACK;
    }

    public void left_rotate (Node node){
        if (node.parent == nil) {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
        else {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        }
    }

    public void right_rotate (Node node){
        if (node.parent == nil) {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
            }
        else {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }
            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        }
    }

    void transplant (Node u, Node v){
        if(u.parent == nil){
            root = v;
        }
        else if(u == u.parent.left){
            u.parent.left = v;
        }
        else
            u.parent.right = v;
        v.parent = u.parent;
    }

    Node min_value(Node tree){
        while (tree.left != nil)
            tree = tree.left;
        return tree;
    }
    
    Node max_value(Node tree){
        while (tree.right != nil)
            tree = tree.right;
        return tree;
    }


    public void delete (Node tree, int n) {
        Node z;
        if((z = search(root, n))==nil) {
        	return;
        }
        Node x;
        Node y = z;
        boolean y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = min_value(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==BLACK)
            delete_fixup(x);
        return;
    }

    public void delete_fixup (Node x){
        while(x != root && x.color == BLACK){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    left_rotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    right_rotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    left_rotate(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    right_rotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    left_rotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    right_rotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    Node search (Node tree, int val) {
        if (tree == nil)
            return nil;

        if (val == tree.val)
            return tree;
        else if (val < tree.val) {
            if (tree.left != nil)
                return search(tree.left, val);
        }
        else if (val > tree.val) {
            if (tree.right != nil)
                return search(tree.right, val);
        }
        return nil;
    }
    
    

    Node predecessor (Node tree) {
    	if (tree.left != nil){
    		return max_value(tree.left);
    	}
    	Node temp = tree.parent;
    	while (temp != nil && tree == temp.left){
    		tree = temp;
    		temp = temp.parent;
    	}
    	return temp;
    }
    
    Node succesor (Node tree) {
    	if (tree.right != nil){
    		return min_value(tree.right);
    	}
    	Node temp = tree.parent;
    	while (temp != nil && tree == temp.right){
    		tree = temp;
    		temp = temp.parent;
    	}
    	return temp;
    }
        
    public int findEmpty(){
    	int random = (int)(Math.random() * 2);
    	
    	if (root == nil){ if (random == 1)return minSize; else return maxSize;}
    	
    	
    	if (random == 1){
    		int index = minSize;
    		Node target = root;
    		while (target.left != nil){
    			target = target.left;
    		}
    		while (true){
    			if (target.val == index) {
    				target = succesor(target);
    				index++;
    			}
    			else return index;
    			if (index > maxSize) break;
    		}
    		return -1;	    	
    	}
    	else{
    		int index = maxSize;
    		Node target = root;
    		while (target.right != nil){
    			target = target.right;
    		}
    		while (true){
    			if (target.val == index) {
    				target = predecessor(target);
    				index--;
    			}
    			else return index;
    			if (index < 60) break;
    		}
    		return -1;
    	}      		
    	
    }
    
    public int findName(String name){
    	if (root == nil){return -1;}
    	addToList(root,0);
    	int r = -1;
    	for (int i = 0; i < listForOutput.size(); i++){
			System.out.println("first : " + listForOutput.get(i).name);
			System.out.println("secon : " + name);
			
    		if (name.equals(listForOutput.get(i).name)) {
    			System.out.println("found!");
    			r =  listForOutput.get(i).val;
    			listForOutput.clear();
    			break;
    		}
    	}
    	return r;
    	
    	
    }
    

    public void print(Node tree, int level) {
        if (tree.right != nil)
            print(tree.right, level + 1);
        for(int i = 0; i < level; i++)
            System.out.print("    ");
        System.out.print(tree.val);
        if (tree.color)
            System.out.print("R");
        else
            System.out.print("B");
        System.out.print(tree.name);
        System.out.println();
        if (tree.left != nil)
            print(tree.left, level + 1);
    }    
    
    public void addToList(Node tree, int level) {
    	if (tree.right != nil)
        	addToList(tree.right, level + 1);
        listForOutput.add(tree);
        if (tree.left != nil)
        	addToList(tree.left, level + 1);
    }
    
    
    public void inorder(Node tree) {
        if (tree == nil)
            return;
        else {
            inorder(tree.left);
            System.out.print(tree.val + " ");
            if  (tree.color == true)
            	System.out.print("R");
            else            	        	
                System.out.print("B");
            System.out.println();
            inorder(tree.right);
        }
    }

    
        
    
    int black_height () {
        int level = 0;
        Node n = root;
        while (n != nil) {
            if (n.color == BLACK)
                level++;
            n = n.right;
        }
        return level;
    } // calculate bh

}