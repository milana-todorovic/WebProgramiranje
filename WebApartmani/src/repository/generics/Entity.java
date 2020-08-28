package repository.generics;

public interface Entity<ID> {

	ID getID();

	void setID(ID id);

}
