package repository.file_repos;

import java.io.File;

import custom_exception.InternalException;
import repository.interfaces.AdminRepository;
import repository.interfaces.AmenityRepository;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.CommentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.HolidayRepository;
import repository.interfaces.HostRepository;
import repository.interfaces.ImageRepository;
import repository.interfaces.RepositoryContainer;
import repository.interfaces.ReservationRepository;

public class FileRepositoryContainer implements RepositoryContainer {

	private AdminFileRepository adminRepository;
	private AmenityFileRepository amenityRepository;
	private ApartmentFileRepository apartmentRepository;
	private CommentFileRepository commentRepository;
	private GuestFileRepository guestRepository;
	private HostFileRepository hostRepository;
	private ReservationFileRepository reservationRepository;
	private HolidayFileRepository holidayRepository;
	private ImageFileRepository imageRepository;

	public FileRepositoryContainer(String root) {
		File file = new File(root);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (Exception e) {
				throw new InternalException(e);
			}
		}

		adminRepository = new AdminFileRepository(root + File.separator + "admin.json");
		amenityRepository = new AmenityFileRepository(root + File.separator + "amenity.json");
		apartmentRepository = new ApartmentFileRepository(root + File.separator + "apartment.json");
		commentRepository = new CommentFileRepository(root + File.separator + "comment.json");
		guestRepository = new GuestFileRepository(root + File.separator + "guest.json");
		hostRepository = new HostFileRepository(root + File.separator + "host.json");
		reservationRepository = new ReservationFileRepository(root + File.separator + "reservation.json");
		holidayRepository = new HolidayFileRepository(root + File.separator + "holiday.json");
		imageRepository = new ImageFileRepository(root + File.separator + "images");

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

	@Override
	public AdminRepository getAdminRepository() {
		return adminRepository;
	}

	@Override
	public AmenityRepository getAmenityRepository() {
		return amenityRepository;
	}

	@Override
	public ApartmentRepository getApartmentRepository() {
		return apartmentRepository;
	}

	@Override
	public CommentRepository getCommentRepository() {
		return commentRepository;
	}

	@Override
	public GuestRepository getGuestRepository() {
		return guestRepository;
	}

	@Override
	public HostRepository getHostRepository() {
		return hostRepository;
	}

	@Override
	public ReservationRepository getReservationRepository() {
		return reservationRepository;
	}

	@Override
	public ImageRepository getImageRepository() {
		return imageRepository;
	}

	@Override
	public HolidayRepository getHolidayRepository() {
		return holidayRepository;
	}

}
