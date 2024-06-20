import java.util.concurrent.atomic.AtomicReference;

public class BSTFine<T extends Comparable<T>> {
    public class Node {
        public AtomicReference<Node> Left;
        public AtomicReference<Node> Right;
        public T Value;
        
        public Node() {
            this.Value = null; 
            this.Left = this.Right = null;
        }
        
        public Node(T value) {
            this.Value = value;
            this.Left = this.Right = null;
        }
    }
    
    private AtomicReference<Node> sentinel = null;
    
    public boolean Search(T value) {
        if (this.sentinel == null) return false;
        AtomicReference<Node> curr = this.sentinel;
        while (curr != null) {
            if (curr.get().Value.equals(value)) return true;
            else if (curr.get().Value.compareTo(value) < 0) curr = curr.get().Left;
        }
    }
    
    public boolean Insert() {
        
    }
    
    public boolean Delete() {
        this.sentinel 
    }
}
