package repository.generics;

public interface SimpleRepository<T extends Entity<ID>, ID> {
	
	void deleteByID(ID id);

	Boolean existsByID(ID id);

	T simpleGetByID(ID id);

	T create(T entity);

}
