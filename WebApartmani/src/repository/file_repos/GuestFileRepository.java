package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Apartment;
import beans.Guest;
import repository.generics.GenericFileRepository;
import repository.interfaces.GuestRepository;

public class GuestFileRepository extends GenericFileRepository<Guest, Integer> implements GuestRepository {

	private ApartmentFileRepository apartmentRepository;
	private ReservationFileRepository reservationRepository;

	protected GuestFileRepository(String filePath) {
		super(filePath);
	}

	void setApartmentRepository(ApartmentFileRepository apartmentRepository) {
		this.apartmentRepository = apartmentRepository;
	}

	void setReservationRepository(ReservationFileRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	protected Integer generateID(Guest entity) {
		return entity.getID();
	}

	@Override
	protected Guest writeResolve(Guest entity) {
		Guest writingCopy = new Guest();
		writingCopy.setID(entity.getID());
		writingCopy.setUsername(entity.getUsername());
		writingCopy.setPassword(entity.getPassword());
		writingCopy.setName(entity.getName());
		writingCopy.setSurname(entity.getSurname());
		writingCopy.setGender(entity.getGender());
		writingCopy.setRole(entity.getRole());
		writingCopy.setReservations(null);
		writeResolveRented(entity, writingCopy);
		return writingCopy;
	}

	private void writeResolveRented(Guest entity, Guest writingCopy) {
		List<Apartment> rentedApartments = new ArrayList<Apartment>();
		for (Apartment apartment : entity.getRentedApartments()) {
			if (apartment != null)
				rentedApartments.add(apartmentRepository.stripToReference(apartment));
		}
		writingCopy.setRentedApartments(rentedApartments);
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
			Apartment rentedApartment = apartmentRepository.simpleGetByID(apartmentID.getID());
			if (rentedApartment != null)
				rentedApartments.add(rentedApartment);
		}
		entity.setRentedApartments(rentedApartments);
	}

	private void readResolveReservations(Guest entity) {
		entity.setReservations(reservationRepository.getByGuestInternal(entity));
	}

	@Override
	protected Guest stripToReference(Guest entity) {
		if (entity == null)
			return null;
		Guest reference = new Guest();
		reference.setID(entity.getID());
		reference.setUsername(null);
		reference.setPassword(null);
		reference.setName(null);
		reference.setSurname(null);
		reference.setGender(null);
		reference.setRole(null);
		reference.setRentedApartments(null);
		reference.setReservations(null);
		return reference;
	}

	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Guest>>() {
		};
	}

}
