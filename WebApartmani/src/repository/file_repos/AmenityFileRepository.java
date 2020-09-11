package repository.file_repos;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Amenity;
import repository.generics.GenericFileRepository;
import repository.interfaces.AmenityRepository;
import repository.util.Sequencer;
import repository.util.IntegerIDGenerator;

public class AmenityFileRepository extends GenericFileRepository<Amenity, Integer> implements AmenityRepository {

	private IntegerIDGenerator generator;

	protected AmenityFileRepository(String filePath) {
		super(filePath);
		generator = new Sequencer(getAllIDs());
	}

	@Override
	protected Integer generateID(Amenity entity) {
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
	
	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Amenity>>() {
		};
	}
}
