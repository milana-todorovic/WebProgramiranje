package repository.generics;

import java.util.function.Predicate;

public interface Repository<T extends Entity<ID>, ID> {

	Integer count();

	void delete(T entity);

	void deleteByID(ID id);

	Boolean existsByID(ID id);

	Iterable<T> getAll();
	
	Iterable<T> getMatching(Predicate<T> condition);

	T fullGetByID(ID id);
	
	T simpleGetByID(ID id);

	T create(T entity);

	T update(T entity);

}
