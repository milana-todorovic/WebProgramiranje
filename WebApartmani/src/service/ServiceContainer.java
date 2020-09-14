package service;

import repository.file_repos.FileRepositoryContainer;
import repository.interfaces.RepositoryContainer;

public class ServiceContainer {

	private AmenityService amenityService;
	private ApartmentService apartmentService;
	private CommentService commentService;
	private HolidayService holidayService;
	private ReservationService reservationService;
	private UserService userService;

	private RepositoryContainer repo;

	public ServiceContainer(String root) {
		repo = new FileRepositoryContainer(root);

		amenityService = new AmenityService(repo.getAmenityRepository(), repo.getApartmentRepository());
		apartmentService = new ApartmentService(repo.getApartmentRepository(), repo.getHostRepository(),
				repo.getImageRepository(), repo.getAmenityRepository());
		commentService = new CommentService(repo.getReservationRepository(), repo.getGuestRepository(),
				repo.getApartmentRepository());
		holidayService = new HolidayService(repo.getHolidayRepository());
		reservationService = new ReservationService(repo.getReservationRepository(), apartmentService,
				repo.getGuestRepository(), repo.getHolidayRepository());
		userService = new UserService(repo.getAdminRepository(), repo.getHostRepository(), repo.getGuestRepository(),
				root);
	}

	public AmenityService getAmenityService() {
		return amenityService;
	}

	public ApartmentService getApartmentService() {
		return apartmentService;
	}

	public CommentService getCommentService() {
		return commentService;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public ReservationService getReservationService() {
		return reservationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public RepositoryContainer getRepo() {
		return repo;
	}

}
