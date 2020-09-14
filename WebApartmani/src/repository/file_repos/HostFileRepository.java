package repository.file_repos;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Host;
import repository.generics.GenericFileRepository;
import repository.interfaces.HostRepository;

public class HostFileRepository extends GenericFileRepository<Host, Integer> implements HostRepository {

	private ApartmentFileRepository apartmentRepository;

	protected HostFileRepository(String filePath) {
		super(filePath);
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	@Override
	protected Integer generateID(Host entity) {
		return entity.getID();
	}

	@Override
	protected Host writeResolve(Host entity) {
		Host writingCopy = new Host();
		writingCopy.setID(entity.getID());
		writingCopy.setUsername(entity.getUsername());
		writingCopy.setPassword(entity.getPassword());
		writingCopy.setName(entity.getName());
		writingCopy.setSurname(entity.getSurname());
		writingCopy.setGender(entity.getGender());
		writingCopy.setRole(entity.getRole());
		writingCopy.setApartments(null);
		writingCopy.setBlocked(entity.getBlocked());
		writingCopy.setDeleted(entity.getDeleted());
		return writingCopy;
	}

	@Override
	protected Host readResolve(Host entity) {
		readResolveApartments(entity);
		return entity;
	}

	private void readResolveApartments(Host entity) {
		entity.setApartments(apartmentRepository.getByHostInternal(entity));
	}

	@Override
	protected Host stripToReference(Host entity) {
		if (entity == null)
			return null;
		Host reference = new Host();
		reference.setID(entity.getID());
		reference.setUsername(null);
		reference.setPassword(null);
		reference.setName(null);
		reference.setSurname(null);
		reference.setGender(null);
		reference.setRole(null);
		reference.setApartments(null);
		return reference;
	}

	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Host>>() {
		};
	}
}
