import java.util.concurrent.locks.ReentrantLock;

public class BSTOpt<T extends Comparable<T>> {
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
        Node curr = this.sentinel.Left;

        if (curr == null) return false;
        
        while (curr != null) {
            // compare value with current node value
            int compare = value.compareTo(curr.Value);
            // check if value is found
            if (compare == 0) {
                return !curr.Marked;
            }
            // if value is less than current node value
            else if (compare < 0) {
                if (curr.Left == null) return false;
                curr = curr.Left;
            }
            // if value is greater than current node value
            else {
                if (curr.Right == null) return false;
                curr = curr.Right;
            }
        }
        return false;
    }

    public boolean Insert(T value) {
        while (true) {
            Node prev = this.sentinel;
            Node curr = prev.Left;

            if (curr == null) {
                prev.lock();
                if (prev.Left != null) {
                    prev.unlock();
                    continue;
                }
                prev.Left = new Node(value);
                prev.unlock();
                return true;
            }

            while (curr != null) {
                // compare value with current node value
                int compare = value.compareTo(curr.Value);
                // if value is found
                if (compare == 0) {
                    prev.lock();
                    curr.lock();
                    if (prev.Left == curr || prev.Right == curr) {
                        boolean originalMarked = curr.Marked;
                        curr.Marked = false;
                        curr.unlock();
                        prev.unlock();
                        return originalMarked;
                    }
                    else {
                        curr.unlock();
                        prev.unlock();
                        break;
                    }
                }
                // if value is less than current node value
                else if (compare < 0) {
                    // if there is no left child
                    if (curr.Left == null) {
                        prev.lock();
                        curr.lock();
                        if (prev.Left == curr || prev.Right == curr) {
                            curr.Left = new Node(value);
                            curr.unlock();
                            prev.unlock();
                            return true;
                        }
                        else {
                            curr.unlock();
                            prev.unlock();
                            break;
                        }
                    }
                    prev = curr;
                    curr = curr.Left;
                }
                // if value is greater than current node value
                else {
                    // if there is no right child
                    if (curr.Right == null) {
                        prev.lock();
                        curr.lock();
                        if (prev.Left == curr || prev.Right == curr) {
                            curr.Right = new Node(value);
                            curr.unlock();
                            prev.unlock();
                            return true;
                        }
                        else {
                            curr.unlock();
                            prev.unlock();
                            break;
                        }
                    }
                    prev = curr;
                    curr = curr.Right;
                }
            }
        }
    }

    public boolean Delete(T value) {
        while (true) {
            Node prev = this.sentinel;
            Node curr = prev.Left;

            if (curr == null) return false;

            while (curr != null) {
                // compare value with current node value
                int compare = value.compareTo(curr.Value);
                // if value is found
                if (compare == 0) {
                    prev.lock();
                    curr.lock();
                    if (prev.Left != curr && prev.Right != curr) {
                        curr.unlock();
                        prev.unlock();
                        break;
                    }
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
                    if (curr.Left == null) return false;

                    prev = curr;
                    curr = curr.Left;
                }
                // if value is greater than current node value
                else {
                    // if there is no right child
                    if (curr.Right == null) return false;

                    prev = curr;
                    curr = curr.Right;
                }
            }
        }
    }
}
