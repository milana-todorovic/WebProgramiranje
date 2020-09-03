package repository.file_repos;

import beans.Host;
import repository.generics.GenericFileRepository;
import repository.interfaces.HostRepository;
import repository.util.IntegerIDGenerator;

public class HostFileRepository extends GenericFileRepository<Host, Integer> implements HostRepository {

	private IntegerIDGenerator generator;

	private ApartmentFileRepository apartmentRepository;

	public HostFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Host writeResolve(Host entity) {
		return entity;
	}

	@Override
	protected Host readResolve(Host entity) {
		readResolveApartments(entity);
		return entity;
	}

	private void readResolveApartments(Host entity) {
		entity.setApartments(apartmentRepository.getByHostInternal(entity));
	}
}
