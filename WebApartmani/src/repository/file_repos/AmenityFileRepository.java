package repository.file_repos;

import beans.Amenity;
import repository.generics.GenericFileRepository;
import repository.interfaces.AmenityRepository;
import repository.util.IntegerIDGenerator;

public class AmenityFileRepository extends GenericFileRepository<Amenity, Integer> implements AmenityRepository {

	private IntegerIDGenerator generator;

	public AmenityFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Amenity writeResolve(Amenity entity) {
		return entity;
	}

	@Override
	protected Amenity readResolve(Amenity entity) {
		return entity;
	}

	@Override
	protected Amenity stripToReference(Amenity entity) {
		if (entity == null)
			return null;
		Amenity reference = new Amenity();
		reference.setID(entity.getID());
		reference.setName(null);
		return reference;
	}
}
