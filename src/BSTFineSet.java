public class BSTFineSet implements Set {
    private BSTFine<Integer> bst;
    
    public BSTFineSet() {
        this.bst = new BSTFine<>();
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
