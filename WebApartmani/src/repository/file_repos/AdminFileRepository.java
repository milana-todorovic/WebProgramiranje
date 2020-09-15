package repository.file_repos;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.User;
import repository.generics.GenericFileRepository;
import repository.interfaces.AdminRepository;

public class AdminFileRepository extends GenericFileRepository<User, Integer> implements AdminRepository {

	protected AdminFileRepository(String filePath) {
		super(filePath);
	}

	@Override
	protected Integer generateID(User entity) {
		return entity.getID();
	}

	@Override
	protected User writeResolve(User entity) {
		return entity;
	}

	@Override
	protected User readResolve(User entity) {
		return entity;
	}

	@Override
	protected User stripToReference(User entity) {
		if (entity == null)
			return null;
		User reference = new User();
		reference.setID(entity.getID());
		reference.setUsername(null);
		reference.setPassword(null);
		reference.setName(null);
		reference.setSurname(null);
		reference.setGender(null);
		reference.setRole(null);
		reference.setDeleted(null);
		return reference;
	}

	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<User>>() {
		};
	}

}
