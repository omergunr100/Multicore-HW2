// we assume the Set contains integers (for this assignment)

public interface Set
{
	// returns 'true' if x was added, 'false' if x was already in the set
	public boolean add(int x);
	
	// returns 'true' if x was removed, false if x wasn't in the set
	public boolean remove(int x);

	// returns 'true' if x is in the set, 'false' if it isn't
	public boolean contains(int x);
}
