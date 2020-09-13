package repository.generics;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import custom_exception.RepositoryException;

public abstract class GenericFileRepository<T extends Entity<ID>, ID> implements Repository<T, ID> {

	private String filePath;

	public GenericFileRepository(String filePath) {
		this.filePath = filePath;
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				writeFile(new ArrayList<T>());
			} catch (Exception e) {
				throw new RepositoryException(e);
			}
		}
	}

	protected abstract ID generateID(T entity);

	protected abstract T writeResolve(T entity);

	protected abstract T readResolve(T entity);

	protected abstract T stripToReference(T entity);

	protected abstract TypeReference<?> getListType();

	protected synchronized List<T> readFile() {
		ObjectMapper mapper = new ObjectMapper();
		List<T> entities = null;
		try {
			entities = mapper.readValue(Paths.get(filePath).toFile(), getListType());
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
		if (entities == null)
			return new ArrayList<T>();
		else
			return entities;
	}

	protected synchronized void writeFile(List<T> entities) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			mapper.writeValue(Paths.get(filePath).toFile(), entities);
		} catch (Exception e) {
			throw new RepositoryException(e);
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
		//entities.removeIf(current -> current.getID().equals(id));
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
	public Collection<T> getAll() {
		List<T> entities = new ArrayList<T>();
		for (T entity : readFile()) {
			entities.add(readResolve(entity));
		}
		return entities;
	}

	public Collection<ID> getAllIDs() {
		List<ID> allIDs = new ArrayList<ID>();
		for (T entity : readFile()) {
			allIDs.add(entity.getID());
		}
		return allIDs;
	}

	@Override
	public Collection<T> getMatching(Predicate<T> condition) {
		List<T> entities = new ArrayList<T>();
		for (T entity : readFile()) {
			if (condition.test(entity))
				entities.add(readResolve(entity));
		}
		return entities;
	}

	@Override
	public T fullGetByID(ID id) {
		for (T entity : readFile()) {
			if (entity.getID().equals(id))
				return readResolve(entity);
		}
		return null;
	}

	@Override
	public T simpleGetByID(ID id) {
		for (T entity : readFile()) {
			if (entity.getID().equals(id))
				return entity;
		}
		return null;
	}

	@Override
	public T create(T entity) {
		entity.setID(generateID(entity));
		List<T> entities = readFile();
		entities.add(writeResolve(entity));
		writeFile(entities);
		return entity;
	}

	@Override
	public T update(T entity) {
		List<T> entities = readFile();
		//entities.removeIf(current -> current.getID().equals(entity.getID()));
		entities.add(writeResolve(entity));
		writeFile(entities);
		return entity;
	}

}
