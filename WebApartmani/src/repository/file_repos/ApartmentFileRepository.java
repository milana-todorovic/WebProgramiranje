package repository.file_repos;

import java.util.ArrayList;
import java.util.List;

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
		return entity;
	}

	@Override
	protected Apartment readResolve(Apartment entity) {
		readResolveAmenities(entity);
		if (entity.getHost() != null)
			readResolveHost(entity);
		readResolveComments(entity);
		readResolveReservations(entity);
		return entity;
	}

	private void readResolveAmenities(Apartment entity) {
		List<Amenity> amenities = new ArrayList<Amenity>();
		for (Amenity amenityID : entity.getAmenities()) {
			Amenity amenity = amenityRepository.getByID(amenityID.getID());
			if (amenity != null)
				amenities.add(amenity);
		}
		entity.setAmenities(amenities);
	}

	private void readResolveHost(Apartment entity) {
		Host host = hostRepository.getByID(entity.getHost().getID());
		entity.setHost(host);
		if (host != null) {
			host.getApartments().remove(entity);
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
				readResolveAmenities(entity);
				entity.setHost(host);
				readResolveComments(entity);
				readResolveReservations(entity);
			}
		}
		return retVal;
	}

}
