package repository.file_repos;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Holidays;
import repository.generics.GenericFileRepository;
import repository.interfaces.HolidayRepository;

public class HolidayFileRepository extends GenericFileRepository<Holidays, Integer> implements HolidayRepository {

	protected HolidayFileRepository(String filePath) {
		super(filePath);
	}

	@Override
	protected Integer generateID(Holidays entity) {
		return entity.getID();
	}

	@Override
	protected Holidays writeResolve(Holidays entity) {
		return entity;
	}

	@Override
	protected Holidays readResolve(Holidays entity) {
		return entity;
	}

	@Override
	protected Holidays stripToReference(Holidays entity) {
		if (entity == null)
			return null;
		Holidays reference = new Holidays();
		reference.setID(entity.getID());
		reference.setDates(null);
		return reference;
	}

	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Holidays>>() {
		};
	}

}
