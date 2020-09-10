package repository.generics;

import java.util.Collection;
import java.util.function.Predicate;

public interface Repository<T extends Entity<ID>, ID> extends SimpleRepository<T, ID> {

	Integer count();

	void delete(T entity);

	void deleteByID(ID id);

	Boolean existsByID(ID id);

	Collection<T> getAll();

	Collection<T> getMatching(Predicate<T> condition);

	T fullGetByID(ID id);

	T simpleGetByID(ID id);

	T create(T entity);

	T update(T entity);

}
