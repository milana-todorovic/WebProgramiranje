package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Apartment;
import beans.Guest;
import beans.Reservation;
import repository.generics.GenericFileRepository;
import repository.interfaces.ReservationRepository;
import repository.util.CounterGenerator;
import repository.util.IntegerIDGenerator;

public class ReservationFileRepository extends GenericFileRepository<Reservation, Integer>
		implements ReservationRepository {

	private IntegerIDGenerator generator;

	private ApartmentFileRepository apartmentRepository;
	private GuestFileRepository guestRepository;

	public ReservationFileRepository(String filePath) {
		super(filePath);
		generator = new CounterGenerator(getAllIDs());
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	void setGuestRepository(GuestFileRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Reservation writeResolve(Reservation entity) {
		Reservation writingCopy = new Reservation();
		writingCopy.setID(entity.getID());
		writingCopy.setApartment(apartmentRepository.stripToReference(entity.getApartment()));
		writingCopy.setGuest(guestRepository.stripToReference(entity.getGuest()));
		writingCopy.setGuestCanComment(entity.getGuestCanComment());
		writingCopy.setMessage(entity.getMessage());
		writingCopy.setNumberOfNights(entity.getNumberOfNights());
		writingCopy.setStartDate(entity.getStartDate());
		writingCopy.setStatus(entity.getStatus());
		writingCopy.setTotalPrice(entity.getTotalPrice());
		return writingCopy;
	}

	@Override
	protected Reservation readResolve(Reservation entity) {
		readResolveApartment(entity);
		readResolveGuest(entity);
		return entity;
	}

	private void readResolveApartment(Reservation entity) {
		if (entity.getApartment() == null)
			return;
		Apartment apartment = apartmentRepository.simpleGetByID(entity.getApartment().getID());
		entity.setApartment(apartment);
		if (apartment != null) {
			apartment.getReservations().add(entity);
		}
	}

	private void readResolveGuest(Reservation entity) {
		if (entity.getGuest() == null)
			return;
		Guest guest = guestRepository.simpleGetByID(entity.getGuest().getID());
		entity.setGuest(guest);
		if (guest != null) {
			guest.getReservations().add(entity);
		}
	}

	List<Reservation> getByApartmentInternal(Apartment apartment) {
		List<Reservation> retVal = new ArrayList<Reservation>();
		for (Reservation entity : readFile()) {
			if (entity.getApartment() != null && entity.getApartment().equals(apartment)) {
				entity.setApartment(apartment);
				retVal.add(entity);
			}
		}
		return retVal;
	}

	List<Reservation> getByGuestInternal(Guest guest) {
		List<Reservation> retVal = new ArrayList<Reservation>();
		for (Reservation entity : readFile()) {
			if (entity.getGuest() != null && entity.getGuest().equals(guest)) {
				entity.setGuest(guest);
				retVal.add(entity);
			}
		}
		return retVal;
	}

	@Override
	protected Reservation stripToReference(Reservation entity) {
		if (entity == null)
			return null;
		Reservation reference = new Reservation();
		reference.setID(entity.getID());
		reference.setApartment(null);
		reference.setGuest(null);
		reference.setGuestCanComment(null);
		reference.setMessage(null);
		reference.setNumberOfNights(null);
		reference.setStartDate(null);
		reference.setStatus(null);
		reference.setTotalPrice(null);
		return reference;
	}
	
	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Reservation>>() {
		};
	}

}
