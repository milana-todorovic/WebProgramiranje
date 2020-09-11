package service;

import java.util.Collection;

import beans.Reservation;
import beans.ReservationStatus;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.HolidayRepository;
import repository.interfaces.ReservationRepository;

public class ReservationService {

	private ReservationRepository reservationRepository;
	private ApartmentService apartmentService;
	private GuestRepository guestRepository;
	private HolidayRepository holidayRepository;

	public ReservationService(ReservationRepository reservationRepository, ApartmentService apartmentService,
			GuestRepository guestRepository, HolidayRepository holidayRepository) {
		super();
		this.reservationRepository = reservationRepository;
		this.apartmentService = apartmentService;
		this.guestRepository = guestRepository;
		this.holidayRepository = holidayRepository;
	}

	public Collection<Reservation> getAll() {
		// TODO obavezno isprazniti passworde gostima
		return null;
	}

	public Collection<Reservation> getByGuestID(Integer guestID) {
		// TODO obavezno isprazniti passworde gostima
		return null;
	}

	public Collection<Reservation> getByApartmentHostID(Integer hostID) {
		// TODO obavezno isprazniti passworde gostima
		return null;
	}

	public Reservation getByID(Integer id) {
		// TODO definitivno sa učitanim apartmanom i gostom, ali mislim da nema potrebe
		// da se učitava host za apartman?
		// isprazniti password gostu
		return null;
	}

	public Reservation create(Reservation reservation) {
		// TODO postaviti cijenu, validirati i upisati
		return null;
	}

	public Reservation changeStatus(Integer id, ReservationStatus status) {
		// TODO validirati da je ok prelaz stanja i sačuvati
		return null;
	}

	public Collection<Reservation> filterByGuestUsername(Collection<Reservation> reservations, String username) {
		// TODO equals ili contains?
		return null;
	}

	public Collection<Reservation> filterByStatus(Collection<Reservation> reservations,
			Collection<ReservationStatus> include) {
		// TODO equals ili contains?
		return null;
	}

	public Collection<Reservation> sortByPriceAscending(Collection<Reservation> reservations) {
		// TODO
		return null;
	}

	public Collection<Reservation> sortByPriceDescending(Collection<Reservation> reservations) {
		// TODO
		return null;
	}

	private Double calculatePrice(Reservation reservation) {
		// TODO smanjiti cijenu noćenja za 10% za petak, subotu, nedelju
		// povećati cijenu noćenja za 5% za praznike
		// uraditi oboje za praznike koji su preko vikenda?
		return null;
	}

	private void validate(Reservation reservation) {
		// TODO
		// gost mora da postoji i ne bude blokiran ili logički obrisan
		// apartman mora da postoji, bude aktivan, i slobodan na te datume
		// broj noćenja >= 1
		// početni datum u budućnosti
		// još nešto?
	}

}
