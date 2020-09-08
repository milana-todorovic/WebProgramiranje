package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Apartment;
import beans.Comment;
import beans.Guest;
import repository.generics.GenericFileRepository;
import repository.interfaces.CommentRepository;
import repository.util.CounterGenerator;
import repository.util.IntegerIDGenerator;

public class CommentFileRepository extends GenericFileRepository<Comment, Integer> implements CommentRepository {

	private IntegerIDGenerator generator;

	private GuestFileRepository guestRepository;
	private ApartmentFileRepository apartmentRepository;

	public CommentFileRepository(String filePath) {
		super(filePath);
		generator = new CounterGenerator(getAllIDs());
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
		Comment writingCopy = new Comment();
		writingCopy.setID(entity.getID());
		writingCopy.setRating(entity.getRating());
		writingCopy.setShowing(entity.isShowing());
		writingCopy.setText(entity.getText());
		writingCopy.setApartment(apartmentRepository.stripToReference(entity.getApartment()));
		writingCopy.setGuest(guestRepository.stripToReference(entity.getGuest()));
		return writingCopy;
	}

	@Override
	protected Comment readResolve(Comment entity) {
		readResolveGuest(entity);
		readResolveApartment(entity);
		return entity;
	}

	private void readResolveGuest(Comment entity) {
		if (entity.getGuest() == null)
			return;
		Guest guest = guestRepository.simpleGetByID(entity.getGuest().getID());
		entity.setGuest(guest);
	}

	private void readResolveApartment(Comment entity) {
		if (entity.getApartment() == null)
			return;
		Apartment apartment = apartmentRepository.simpleGetByID(entity.getApartment().getID());
		entity.setApartment(apartment);
		if (apartment != null) {
			apartment.getComments().add(entity);
		}
	}

	List<Comment> getByApartmentInternal(Apartment apartment) {
		List<Comment> retVal = new ArrayList<Comment>();
		for (Comment entity : readFile()) {
			if (entity.getApartment() != null && entity.getApartment().equals(apartment)) {
				entity.setApartment(apartment);
				retVal.add(entity);
			}
		}
		return retVal;
	}

	@Override
	protected Comment stripToReference(Comment entity) {
		if (entity == null)
			return null;
		Comment reference = new Comment();
		reference.setID(entity.getID());
		reference.setApartment(null);
		reference.setGuest(null);
		reference.setRating(null);
		reference.setShowing(null);
		reference.setText(null);
		return reference;
	}
	
	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Comment>>() {
		};
	}

}
