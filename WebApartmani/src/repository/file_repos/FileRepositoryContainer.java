package repository.file_repos;

import java.io.File;

import repository.interfaces.RepositoryContainer;

public class FileRepositoryContainer implements RepositoryContainer {

	private AdminFileRepository adminRepository;
	private AmenityFileRepository amenityRepository;
	private ApartmentFileRepository apartmentRepository;
	private CommentFileRepository commentRepository;
	private GuestFileRepository guestRepository;
	private HostFileRepository hostRepository;
	private ReservationFileRepository reservationRepository;

	public FileRepositoryContainer(String root) {
		adminRepository = new AdminFileRepository(root + File.separator + "admin.json");
		amenityRepository = new AmenityFileRepository(root + File.separator + "amenity.json");
		apartmentRepository = new ApartmentFileRepository(root + File.separator + "apartment.json");
		commentRepository = new CommentFileRepository(root + File.separator + "comment.json");
		guestRepository = new GuestFileRepository(root + File.separator + "guest.json");
		hostRepository = new HostFileRepository(root + File.separator + "host.json");
		reservationRepository = new ReservationFileRepository(root + File.separator + "reservation.json");

		apartmentRepository.setAmenityRepository(amenityRepository);
		apartmentRepository.setCommentRepository(commentRepository);
		apartmentRepository.setHostRepository(hostRepository);
		apartmentRepository.setReservationRepository(reservationRepository);

		commentRepository.setApartmentRepository(apartmentRepository);
		commentRepository.setGuestRepository(guestRepository);

		guestRepository.setApartmentRepository(apartmentRepository);
		guestRepository.setReservationRepository(reservationRepository);

		hostRepository.setApartmentRepository(apartmentRepository);

		reservationRepository.setGuestRepository(guestRepository);
		reservationRepository.setApartmentRepository(apartmentRepository);
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getAdminRepository()
	 */
	@Override
	public AdminFileRepository getAdminRepository() {
		return adminRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getAmenityRepository()
	 */
	@Override
	public AmenityFileRepository getAmenityRepository() {
		return amenityRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getApartmentRepository()
	 */
	@Override
	public ApartmentFileRepository getApartmentRepository() {
		return apartmentRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getCommentRepository()
	 */
	@Override
	public CommentFileRepository getCommentRepository() {
		return commentRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getGuestRepository()
	 */
	@Override
	public GuestFileRepository getGuestRepository() {
		return guestRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getHostRepository()
	 */
	@Override
	public HostFileRepository getHostRepository() {
		return hostRepository;
	}

	/* (non-Javadoc)
	 * @see repository.file_repos.RepositoryContainer#getReservationRepository()
	 */
	@Override
	public ReservationFileRepository getReservationRepository() {
		return reservationRepository;
	}

}
