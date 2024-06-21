import java.util.concurrent.locks.ReentrantLock;

public class BSTFine<T extends Comparable<T>> {
    public class Node {
        public volatile Node Left;
        public volatile Node Right;
        public volatile T Value;
        public volatile boolean Marked;
        public final ReentrantLock Lock;
        
        public Node() {
            this.Marked = false;
            this.Value = null; 
            this.Left = this.Right = null;
            this.Lock = new ReentrantLock();
        }
        
        public Node(T value) {
            this();
            this.Value = value;
        }
        
        public void lock() {
            this.Lock.lock();
        }
        
        public void unlock() {
            this.Lock.unlock();
        }
    }
    
    private final Node sentinel = new Node();

    public boolean Search(T value) {
        Node prev = this.sentinel;
        Node curr = prev.Left;

        if (curr == null) return false;

        boolean found = false;
        while (!found) {
            // keep track of previous node
            var release = prev;
            // compare value with current node value
            int compare = value.compareTo(curr.Value);
            // check if value is found
            found = compare == 0;
            if (found && curr.Marked) {
                release.unlock();
                curr.unlock();
                return false;
            }
            // if value is less than current node value
            if (!found && compare < 0) {
                prev = curr;
                if (curr.Left == null) {
                    release.unlock();
                    curr.unlock();
                    return false;
                }
                curr = curr.Left;
                curr.lock();
            }
            // if value is greater than current node value
            else if (!found) {
                prev = curr;
                if (curr.Right == null) {
                    release.unlock();
                    curr.unlock();
                    return false;
                }
                curr = curr.Right;
                curr.lock();
            }
            // release previous node lock
            release.unlock();
        }
        curr.unlock();
        return false;
    }

    public boolean Insert(T value) {
        Node prev = this.sentinel;
        Node curr;
        // capture initial node
        prev.lock();
        curr = prev.Left;
        if (curr == null) {
            prev.Left = new Node(value);
            prev.unlock();
            return true;
        }
        curr.lock();

        while (curr != null) {
            // keep track of previous node
            var release = prev;
            // compare value with current node value
            int compare = value.compareTo(curr.Value);
            // if value is found
            if (compare == 0) {
                boolean originalMarked = curr.Marked;
                curr.Marked = false;
                curr.unlock();
                prev.unlock();
                return originalMarked;
            }
            // if value is less than current node value
            else if (compare < 0) {
                // if there is no left child
                if (curr.Left == null) {
                    curr.Left = new Node(value);
                    curr.unlock();
                    prev.unlock();
                    return true;
                }
                prev = curr;
                curr = curr.Left;
                curr.lock();
            }
            // if value is greater than current node value
            else {
                // if there is no right child
                if (curr.Right == null) {
                    curr.Right = new Node(value);
                    curr.unlock();
                    prev.unlock();
                    return true;
                }
                prev = curr;
                curr = curr.Right;
                curr.lock();
            }
            // release previous node lock
            release.unlock();
        }
        // unreachable code
        return false;
    }

    public boolean Delete(T value) {
        Node prev = this.sentinel;
        Node curr;
        // capture initial node
        prev.lock();
        curr = prev.Left;
        if (curr == null) {
            prev.unlock();
            return false;
        }
        curr.lock();

        while (curr != null) {
            // keep track of previous node
            var release = prev;
            // compare value with current node value
            int compare = value.compareTo(curr.Value);
            // if value is found
            if (compare == 0) {
                boolean originalMarked = curr.Marked;
                // if node has no children
                if (curr.Left == curr.Right && curr.Left == null) {
                    if (prev.Left == curr) prev.Left = null;
                    else prev.Right = null;
                }
                // if node has right child
                else if (curr.Left == null) {
                    if (prev.Left == curr) prev.Left = curr.Right;
                    else prev.Right = curr.Right;
                }
                // if node has left child
                else if (curr.Right == null) {
                    if (prev.Left == curr) prev.Left = curr.Left;
                    else prev.Right = curr.Left;
                }
                // if node has two children
                else {
                    curr.Marked = true;
                }
                curr.unlock();
                prev.unlock();
                return !originalMarked;
            }
            // if value is less than current node value
            else if (compare < 0) {
                // if there is no left child
                if (curr.Left == null) {
                    curr.unlock();
                    prev.unlock();
                    return false;
                }
                prev = curr;
                curr = curr.Left;
                curr.lock();
            }
            // if value is greater than current node value
            else {
                // if there is no right child
                if (curr.Right == null) {
                    curr.unlock();
                    prev.unlock();
                    return false;
                }
                prev = curr;
                curr = curr.Right;
                curr.lock();
            }
            // release previous node lock
            release.unlock();
        }
        if (curr != null) curr.unlock();
        return false;
    }
}
