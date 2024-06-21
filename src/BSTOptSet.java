public class BSTOptSet implements Set{
    private BSTOpt<Integer> bst;
    
    public BSTOptSet() {
        this.bst = new BSTOpt<>();
    }
    
    @Override
    public boolean add(int x) {
        return this.bst.Insert(x);
    }

    @Override
    public boolean remove(int x) {
        return this.bst.Delete(x);
    }

    @Override
    public boolean contains(int x) {
        return this.bst.Search(x);
    }
}
