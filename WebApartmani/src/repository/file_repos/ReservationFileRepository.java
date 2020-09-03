package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import beans.Apartment;
import beans.Guest;
import beans.Reservation;
import repository.generics.GenericFileRepository;
import repository.interfaces.ReservationRepository;
import repository.util.IntegerIDGenerator;

public class ReservationFileRepository extends GenericFileRepository<Reservation, Integer>
		implements ReservationRepository {

	private IntegerIDGenerator generator;

	private ApartmentFileRepository apartmentRepository;
	private GuestFileRepository guestRepository;

	public ReservationFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
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
		return entity;
	}

	@Override
	protected Reservation readResolve(Reservation entity) {
		if (entity.getApartment() != null)
			readResolveApartment(entity);
		if (entity.getGuest() != null)
			readResolveGuest(entity);
		return entity;
	}

	private void readResolveApartment(Reservation entity) {
		Apartment apartment = apartmentRepository.getByID(entity.getApartment().getID());
		entity.setApartment(apartment);
		if (apartment != null) {
			apartment.getReservations().remove(entity);
			apartment.getReservations().add(entity);
		}
	}

	private void readResolveGuest(Reservation entity) {
		Guest guest = guestRepository.getByID(entity.getGuest().getID());
		entity.setGuest(guest);
		if (guest != null) {
			guest.getReservations().remove(entity);
			guest.getReservations().add(entity);
		}
	}

	List<Reservation> getByApartmentInternal(Apartment apartment) {
		List<Reservation> retVal = new ArrayList<Reservation>();
		for (Reservation entity : readFile()) {
			if (entity.getApartment() != null && entity.getApartment().equals(apartment)) {
				if (entity.getGuest() != null)
					readResolveGuest(entity);
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
				if (entity.getApartment() != null)
					readResolveApartment(entity);
				entity.setGuest(guest);
				retVal.add(entity);
			}
		}
		return retVal;
	}

}
