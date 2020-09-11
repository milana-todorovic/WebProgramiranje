package repository.file_repos;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Holiday;
import repository.generics.GenericFileRepository;
import repository.interfaces.HolidayRepository;
import repository.util.Sequencer;
import repository.util.IntegerIDGenerator;

public class HolidayFileRepository extends GenericFileRepository<Holiday, Integer> implements HolidayRepository {

	private IntegerIDGenerator generator;

	protected HolidayFileRepository(String filePath) {
		super(filePath);
		generator = new Sequencer(getAllIDs());
	}

	@Override
	protected Integer generateID(Holiday entity) {
		return generator.generateID();
	}

	@Override
	protected Holiday writeResolve(Holiday entity) {
		return entity;
	}

	@Override
	protected Holiday readResolve(Holiday entity) {
		return entity;
	}

	@Override
	protected Holiday stripToReference(Holiday entity) {
		if (entity == null)
			return null;
		Holiday reference = new Holiday();
		reference.setID(entity.getID());
		reference.setDate(null);
		return reference;
	}

	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Holiday>>() {
		};
	}

}
