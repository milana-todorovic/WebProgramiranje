package repository.file_repos;

import beans.User;
import repository.generics.GenericFileRepository;
import repository.interfaces.AdminRepository;
import repository.util.IntegerIDGenerator;

public class AdminFileRepository extends GenericFileRepository<User, Integer> implements AdminRepository {

	private IntegerIDGenerator generator;

	public AdminFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected User writeResolve(User entity) {
		return entity;
	}

	@Override
	protected User readResolve(User entity) {
		return entity;
	}
}