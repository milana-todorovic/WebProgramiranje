package repository.generics;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class GenericFileRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {

	private String filePath;

	public GenericFileRepository(String filePath) {
		this.filePath = filePath;
		try {
			File file = new File(filePath);
			file.createNewFile();
		} catch (Exception e) {

		}
	}

	protected abstract ID generateID();

	protected abstract T writeResolve(T entity);

	protected abstract T readResolve(T entity);

	private List<T> readFile() {
		ObjectMapper mapper = new ObjectMapper();
		List<T> entities = null;
		try {
			entities = mapper.readValue(Paths.get(filePath).toFile(), new TypeReference<List<T>>() {
			});
		} catch (Exception e) {

		}
		return entities;
	}

	private void writeFile(List<T> entities) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(Paths.get(filePath).toFile(), entities);
		} catch (Exception e) {

		}
	}

	@Override
	public Integer count() {
		List<T> entities = readFile();
		return entities.size();
	}

	@Override
	public void delete(T entity) {
		deleteByID(entity.getID());
	}

	@Override
	public void deleteByID(ID id) {
		List<T> entities = readFile();
		entities.removeIf(current -> current.getID().equals(id));
		writeFile(entities);
	}

	@Override
	public Boolean existsByID(ID id) {
		for (T entity : readFile()) {
			if (entity.getID().equals(id))
				return true;
		}
		return false;
	}

	@Override
	public Iterable<T> getAll() {
		List<T> entities = new ArrayList<T>();
		for (T entity : readFile()) {
			entities.add(readResolve(entity));
		}
		return entities;
	}

	@Override
	public Iterable<T> getMatching(Predicate<T> condition) {
		List<T> entities = new ArrayList<T>();
		for (T entity : readFile()) {
			if (condition.test(entity))
				entities.add(readResolve(entity));
		}
		return entities;
	}

	@Override
	public T getByID(ID id) {
		for (T entity : readFile()) {
			if (entity.getID().equals(id))
				return readResolve(entity);
		}
		return null;
	}

	@Override
	public T create(T entity) {
		entity.setID(generateID());
		List<T> entities = readFile();
		entities.add(writeResolve(entity));
		writeFile(entities);
		return entity;
	}

	@Override
	public T update(T entity) {
		List<T> entities = readFile();
		entities.removeIf(current -> current.getID().equals(entity.getID()));
		entities.add(writeResolve(entity));
		writeFile(entities);
		return entity;
	}

}
