package cs1501_p1;

import org.w3c.dom.Node;

public class BST<T extends Comparable<T>> implements BST_Inter<T> {

    private BTNode<T> root;


    public BST() {
        this.root = null;
    }


    public void put(T key) {
        root = put_cur(key, root);
    }

    public BTNode<T> put_cur(T key, BTNode<T> cur) {
        //if it's an empty node, creat a new node and the value of it is key.
        if (cur == null) {
            return new BTNode(key);
        }

        //if key less than cur, go left and continue, and if key greater than cur, go right and continue.
        if (key.compareTo(cur.getKey()) < 0) {
            cur.setLeft(put_cur(key, cur.getLeft()));
        } else if (key.compareTo(cur.getKey()) > 0) {
            cur.setRight(put_cur(key, cur.getRight()));
        }
        return cur;
    }

    public boolean contains(T key) {
        return contains_cur(key, root);
    }

    public boolean contains_cur(T key, BTNode<T> cur) {
        //can not found key
        if(cur == null) {
            return false; 
        }

        //if key less than cur, go left and continue, and if key greater than cur, go right and continue, else key equal to cur.
        if(key.compareTo(cur.getKey()) < 0) {
            return contains_cur(key, cur.getLeft());
        } 
        else if(key.compareTo(cur.getKey()) > 0) {
            return contains_cur(key, cur.getRight());
        } else {
            return true;
        }
    }

    public void delete(T key) {
        delete_cur(key, root, null);
    }

    //cur's parent node is to connect cur's parent to cur's child
    public void delete_cur(T key, BTNode<T> cur, BTNode<T> parent) {
        //can not found key
        if(cur == null) {
            return;
        }

        //try to find key
        if(key.compareTo(cur.getKey()) < 0) {
            delete_cur(key, cur.getLeft(), cur);
        } else if(key.compareTo(cur.getKey()) > 0) {
            delete_cur(key, cur.getRight(), cur);
        } else {
            //found key to delete

            //if cur have no more than one child, replace cur with the child
            if(cur.getLeft() == null) {
                if(parent.getLeft() == cur) {
                    parent.setLeft(cur.getRight());
                } else {
                    parent.setRight(cur.getRight());
                }
            } else if (cur.getRight() == null) {
                if(parent.getLeft() == cur) {
                    parent.setLeft(cur.getLeft());
                } else {
                    parent.setRight(cur.getLeft());
                }
            } else {
                //if cur have two childern

                //deleted the minimum node in the right sub-tree of cur, and assigned to min
                BTNode<T> min = deleteMin(cur.getRight());

                //replace cur with min
                if(parent.getLeft() == cur) {
                    parent.setLeft(min);
                } else {
                    parent.setRight(min);
                }
                min.setLeft(cur.getLeft());
                min.setRight(cur.getRight());
            }
        }
    }

    public BTNode<T> deleteMin(BTNode<T> cur) {
        return deleteMin_cur(cur, null);
    }

    public BTNode<T> deleteMin_cur(BTNode<T> cur, BTNode<T> parent) {
        //go left and continue
        if(cur.getLeft() != null) {
            return deleteMin_cur(cur.getLeft(), cur);
        } else {
            //found the minimum node

            //deleted the minimum node
            parent.setLeft(cur.getRight());

            //return the mimimum node
            return cur;
        }
    }
    /**
     * Determine the height of the BST
     *
     * <p>
     * A single node tree has a height of 1, an empty tree has a height of 0.
     *
     * @return int value indicating the height of the BST
     */
    public int height(){
        return height_cur(root);
    }

    public int height_cur(BTNode<T> cur) {
        //edge case
        if(cur == null) {
            return 0;
        }

        //recursion
        int leftHeight = height_cur(cur.getLeft());
        int rightHeight = height_cur(cur.getRight());
        
        //Add one(itself) to the larger one between left-tree height or right-tree height
        return (leftHeight >= rightHeight ? leftHeight : rightHeight) + 1;
    }
    /**
     * Determine if the BST is height-balanced
     *
     * <p>
     * A height balanced binary tree is one where the left and right subtrees
     * of all nodes differ in height by no more than 1.
     *
     * @return true if the BST is height-balanced, false if it is not
     */
    public boolean isBalanced(){
        return isBalanced_cur(root);
    }

    public boolean isBalanced_cur(BTNode<T> cur) {
        //edge case
        if(cur == null) {
            return true;
        }

        //calculate the height diff
        int leftHeight = height_cur(cur.getLeft());
        int rightHeight = height_cur(cur.getRight());
        int height = leftHeight - rightHeight;
        
        //left and right subtree both balanced(recursion), and the height diff is less or equal to 1.
        if(Math.abs(height) <= 1 && isBalanced_cur(cur.getLeft()) && isBalanced_cur(cur.getRight())) {
            return true;
        }
        return false;
    }


    /**
     * Produce a ':' separated String of all keys in ascending order
     *
     * <p>
     * Perform an in-order traversal of the tree and produce a String
     * containing the keys in ascending order, separated by ':'s.
     * 
     * @return String containing the keys in ascending order, ':' separated
     */
    public String inOrderTraversal(){

        return inOrderTraversal_cur(root);
    }

    public String inOrderTraversal_cur(BTNode<T> cur){
        //edge case
        if(cur == null) {
            return "";
        }

        //first go left
        String result = "";
        if(cur.getLeft() != null) {
            result += inOrderTraversal_cur(cur.getLeft()) + ":";
        }

        //append itself
        result += cur.getKey().toString();

        //then go right
        if(cur.getRight() != null) {
            result += ":" + inOrderTraversal_cur(cur.getRight());
        }        
        return result;
    }

    /**
     * Produce String representation of the BST
     * 
     * <p>
     * Perform a pre-order traversal of the BST in order to produce a String
     * representation of the BST. The reprsentation should be a comma separated
     * list where each entry represents a single node. Each entry should take
     * the form: *type*(*key*). You should track 4 node types:
     * `R`: The root of the tree
     * `I`: An interior node of the tree (e.g., not the root, not a leaf)
     * `L`: A leaf of the tree
     * `X`: A stand-in for a null reference
     * For each node, you should list its left child first, then its right
     * child. You do not need to list children of leaves. The `X` type is only
     * for nodes that have one valid child.
     * 
     * @return String representation of the BST
     */
    public String serialize(){
        return serialize_cur(root, root);
    }

    public String serialize_cur(BTNode<T> cur, BTNode<T> root) {
        String result = "";

        //append itself
        result += serializType(cur, root);
        
        //cur is not X(NULL) and it's not leaf, go recursion
        if(cur != null && (cur.getLeft() != null || cur.getRight() != null)) {
            String leftResult = serialize_cur(cur.getLeft(), root);
            if(!leftResult.isEmpty()) {
                result += "," + leftResult;
            }
            String rightResult = serialize_cur(cur.getRight(), root);
            if(!rightResult.isEmpty()) {
                result += "," + rightResult;
            }
        }
        
        return result;
    }

    public String serializType(BTNode<T> node, BTNode<T> root) {
      
        if(node == null) {
            return "X(NULL)";
        }
        if(node == root) {
            return "R(" + node.getKey().toString() + ")";
        }
        if(node.getLeft() != null || node.getRight() != null) {
            return "I(" + node.getKey().toString() + ")";
        } else {
            return "L(" + node.getKey().toString() + ")";
        }
    }

    /**
     * Produce a deep copy of the BST that is reversed (i.e., left children
     * hold keys greater than the current key, right children hold keys less
     * than the current key).
     *
     * @return Deep copy of the BST reversed
     */
    public BST_Inter<T> reverse(){
        BST<T> reversed = new BST<T>();
        reversed.root = reverse_cur(root);
        return reversed;
    }

    public BTNode<T> reverse_cur(BTNode<T> cur){
        //edge case
        if (cur == null) {
            return null;
        }

        //deep copy and reversed
        BTNode<T> reversedNode = new BTNode<T>(cur.getKey());
        reversedNode.setLeft(reverse_cur(cur.getRight()));
        reversedNode.setRight(reverse_cur(cur.getLeft()));
        return reversedNode;
    }
    
}