package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import beans.Amenity;
import beans.Apartment;
import beans.Host;
import repository.generics.GenericFileRepository;
import repository.interfaces.ApartmentRepository;
import repository.util.IntegerIDGenerator;

public class ApartmentFileRepository extends GenericFileRepository<Apartment, Integer> implements ApartmentRepository {

	private IntegerIDGenerator generator;

	private AmenityFileRepository amenityRepository;
	private HostFileRepository hostRepository;
	private CommentFileRepository commentRepository;
	private ReservationFileRepository reservationRepository;

	public ApartmentFileRepository(String filePath) {
		super(filePath);
		generator = new IntegerIDGenerator(getAllIDs());
	}

	void setAmenityRepository(AmenityFileRepository amenityRepository) {
		this.amenityRepository = amenityRepository;
	}

	void setHostRepository(HostFileRepository hostRepository) {
		this.hostRepository = hostRepository;
	}

	void setCommentRepository(CommentFileRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	void setReservationRepository(ReservationFileRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Override
	protected Integer generateID() {
		return generator.generateID();
	}

	@Override
	protected Apartment writeResolve(Apartment entity) {
		Apartment writingCopy = new Apartment();
		writingCopy.setID(entity.getID());
		writingCopy.setApartmentType(entity.getApartmentType());
		writingCopy.setDatesForRenting(entity.getDatesForRenting());
		writingCopy.setAvailableDates(null);
		writingCopy.setCheckInTime(entity.getCheckInTime());
		writingCopy.setCheckOutTime(entity.getCheckOutTime());
		writingCopy.setImageKeys(entity.getImageKeys());
		writingCopy.setLocation(entity.getLocation());
		writingCopy.setName(entity.getName());
		writingCopy.setNumberOfGuests(entity.getNumberOfGuests());
		writingCopy.setNumberOfRooms(entity.getNumberOfRooms());
		writingCopy.setPricePerNight(entity.getPricePerNight());
		writingCopy.setStatus(entity.getStatus());

		writeResolveAmenities(entity, writingCopy);
		writingCopy.setHost(hostRepository.stripToReference(entity.getHost()));
		writingCopy.setComments(null);
		writingCopy.setReservations(null);
		return writingCopy;
	}

	private void writeResolveAmenities(Apartment entity, Apartment writingCopy) {
		List<Amenity> amenities = new ArrayList<Amenity>();
		for (Amenity amenity : entity.getAmenities()) {
			if (amenity != null)
				amenities.add(amenityRepository.stripToReference(amenity));
		}
		writingCopy.setAmenities(amenities);
	}

	@Override
	protected Apartment readResolve(Apartment entity) {
		readResolveAmenities(entity);
		readResolveHost(entity);
		readResolveComments(entity);
		readResolveReservations(entity);
		return entity;
	}

	private void readResolveAmenities(Apartment entity) {
		List<Amenity> amenities = new ArrayList<Amenity>();
		for (Amenity amenityID : entity.getAmenities()) {
			Amenity amenity = amenityRepository.simpleGetByID(amenityID.getID());
			if (amenity != null)
				amenities.add(amenity);
		}
		entity.setAmenities(amenities);
	}

	private void readResolveHost(Apartment entity) {
		if (entity.getHost() == null)
			return;
		Host host = hostRepository.simpleGetByID(entity.getHost().getID());
		entity.setHost(host);
		if (host != null) {
			host.getApartments().add(entity);
		}
	}

	private void readResolveComments(Apartment entity) {
		entity.setComments(commentRepository.getByApartmentInternal(entity));
	}

	private void readResolveReservations(Apartment entity) {
		entity.setReservations(reservationRepository.getByApartmentInternal(entity));
	}

	List<Apartment> getByHostInternal(Host host) {
		List<Apartment> retVal = new ArrayList<Apartment>();
		for (Apartment entity : readFile()) {
			if (entity.getHost() != null && entity.getHost().equals(host)) {
				entity.setHost(host);
				retVal.add(entity);
			}
		}
		return retVal;
	}

	@Override
	protected Apartment stripToReference(Apartment entity) {
		if (entity == null)
			return null;
		Apartment reference = new Apartment();
		reference.setID(entity.getID());
		reference.setAmenities(null);
		reference.setApartmentType(null);
		reference.setAvailableDates(null);
		reference.setCheckInTime(null);
		reference.setCheckOutTime(null);
		reference.setComments(null);
		reference.setDatesForRenting(null);
		reference.setHost(null);
		reference.setImageKeys(null);
		reference.setLocation(null);
		reference.setName(null);
		reference.setNumberOfGuests(null);
		reference.setNumberOfRooms(null);
		reference.setPricePerNight(null);
		reference.setReservations(null);
		reference.setStatus(null);
		return reference;
	}
	
	@Override
	protected TypeReference<?> getListType() {
		return new TypeReference<List<Apartment>>() {
		};
	}

}
