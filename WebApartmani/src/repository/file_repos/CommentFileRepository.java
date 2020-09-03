package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import beans.Apartment;
import beans.Comment;
import beans.Guest;
import repository.generics.GenericFileRepository;
import repository.interfaces.CommentRepository;
import repository.util.IntegerIDGenerator;

public class CommentFileRepository extends GenericFileRepository<Comment, Integer> implements CommentRepository {

	private IntegerIDGenerator generator;

	private GuestFileRepository guestRepository;
	private ApartmentFileRepository apartmentRepository;

	public CommentFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	void setGuestRepository(GuestFileRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Comment writeResolve(Comment entity) {
		return entity;
	}

	@Override
	protected Comment readResolve(Comment entity) {
		if (entity.getGuest() != null)
			readResolveGuest(entity);
		if (entity.getApartment() != null)
			readResolveApartment(entity);
		return entity;
	}

	private void readResolveGuest(Comment entity) {
		Guest guest = guestRepository.getByID(entity.getGuest().getID());
		entity.setGuest(guest);
	}

	private void readResolveApartment(Comment entity) {
		Apartment apartment = apartmentRepository.getByID(entity.getApartment().getID());
		entity.setApartment(apartment);
		if (apartment != null) {
			apartment.getComments().remove(entity);
			apartment.getComments().add(entity);
		}
	}

	List<Comment> getByApartmentInternal(Apartment apartment) {
		List<Comment> retVal = new ArrayList<Comment>();
		for (Comment entity : readFile()) {
			if (entity.getApartment() != null && entity.getApartment().equals(apartment)) {
				if (entity.getGuest() != null)
					readResolveGuest(entity);
				entity.setApartment(apartment);
				retVal.add(entity);
			}
		}
		return retVal;
	}

}
