package repository.interfaces;

import repository.file_repos.AdminFileRepository;
import repository.file_repos.AmenityFileRepository;
import repository.file_repos.ApartmentFileRepository;
import repository.file_repos.CommentFileRepository;
import repository.file_repos.GuestFileRepository;
import repository.file_repos.HostFileRepository;
import repository.file_repos.ReservationFileRepository;

public interface RepositoryContainer {

	AdminFileRepository getAdminRepository();

	AmenityFileRepository getAmenityRepository();

	ApartmentFileRepository getApartmentRepository();

	CommentFileRepository getCommentRepository();

	GuestFileRepository getGuestRepository();

	HostFileRepository getHostRepository();

	ReservationFileRepository getReservationRepository();

}