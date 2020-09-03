package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import beans.Apartment;
import beans.Guest;
import repository.generics.GenericFileRepository;
import repository.interfaces.GuestRepository;
import repository.util.IntegerIDGenerator;

public class GuestFileRepository extends GenericFileRepository<Guest, Integer> implements GuestRepository {

	private IntegerIDGenerator generator;

	private ApartmentFileRepository apartmentRepository;
	private ReservationFileRepository reservationRepository;

	public GuestFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	void setReservationRepository(ReservationFileRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Guest writeResolve(Guest entity) {
		return entity;
	}

	@Override
	protected Guest readResolve(Guest entity) {
		readResolveRented(entity);
		readResolveReservations(entity);
		return entity;
	}

	private void readResolveRented(Guest entity) {
		List<Apartment> rentedApartments = new ArrayList<Apartment>();
		for (Apartment apartmentID : entity.getRentedApartments()) {
			Apartment rentedApartment = apartmentRepository.getByID(apartmentID.getID());
			if (rentedApartment != null)
				rentedApartments.add(rentedApartment);
		}
		entity.setRentedApartments(rentedApartments);
	}

	private void readResolveReservations(Guest entity) {
		entity.setReservations(reservationRepository.getByGuestInternal(entity));
	}

}
