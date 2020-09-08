package repository.interfaces;

public interface RepositoryContainer {

	AdminRepository getAdminRepository();

	AmenityRepository getAmenityRepository();

	ApartmentRepository getApartmentRepository();

	CommentRepository getCommentRepository();

	GuestRepository getGuestRepository();

	HostRepository getHostRepository();

	ReservationRepository getReservationRepository();
	
	ImageRepository getImageRepository();

}