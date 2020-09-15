package service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import beans.Apartment;
import beans.ApartmentStatus;
import beans.Guest;
import beans.Reservation;
import beans.ReservationStatus;
import custom_exception.BadRequestException;
import repository.interfaces.GuestRepository;
import repository.interfaces.ReservationRepository;
import util.CollectionUtil;
import util.DateUtil;
import util.StringValidator;

public class ReservationService {

	private ReservationRepository reservationRepository;
	private ApartmentService apartmentService;
	private GuestRepository guestRepository;
	private HolidayService holidayService;

	private Double holidayMultiplier;
	private Double weekendMultiplier;

	public ReservationService(ReservationRepository reservationRepository, ApartmentService apartmentService,
			GuestRepository guestRepository, HolidayService holidayService) {
		super();
		this.reservationRepository = reservationRepository;
		this.apartmentService = apartmentService;
		this.guestRepository = guestRepository;
		this.holidayService = holidayService;

		// TODO namjestiti da se ovo čita iz nekog config fajla
		this.holidayMultiplier = 1.05;
		this.weekendMultiplier = 0.9;
	}

	public Collection<Reservation> getAll() {
		Collection<Reservation> reservations = reservationRepository.getAll();
		reservations.forEach(reservation -> reservation.getGuest().setPassword(""));
		return reservations;
	}

	public Collection<Reservation> getByGuestID(Integer guestID) {
		if (guestID == null)
			throw new BadRequestException("Mora biti zadat ključ gosta.");
		Collection<Reservation> reservations = reservationRepository
				.getMatching(reservation -> guestID.equals(reservation.getGuest().getID()));
		reservations.forEach(reservation -> reservation.getGuest().setPassword(""));
		return reservations;
	}

	public Collection<Reservation> getByApartmentHostID(Integer hostID) {
		if (hostID == null)
			throw new BadRequestException("Mora biti zadat ključ domaćina.");
		Collection<Reservation> reservations = reservationRepository
				.getMatching(reservation -> hostID.equals(reservation.getApartment().getHost().getID()));
		reservations.forEach(reservation -> reservation.getGuest().setPassword(""));
		return reservations;
	}

	public Reservation getByID(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Reservation reservation = reservationRepository.fullGetByID(id);
		if (reservation != null)
			reservation.getGuest().setPassword("");
		return reservation;
	}

	public Reservation create(Reservation reservation) {
		if (reservation == null)
			throw new BadRequestException("Mora biti zadata rezervacija koja se dodaje.");
		validate(reservation);
		reservation.setTotalPrice(calculatePrice(reservation));
		reservation.setStatus(ReservationStatus.CREATED);
		reservation.setGuestCanComment(false);
		Reservation created = reservationRepository.create(reservation);
		created.getGuest().setPassword("");
		return created;
	}

	public Reservation changeStatus(Integer id, ReservationStatus status) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ rezervacije.");
		if (status == null)
			throw new BadRequestException("Mora biti zadato novo stanje rezervacije.");
		Reservation reservation = reservationRepository.simpleGetByID(id);
		if (reservation == null)
			throw new BadRequestException("Ne postoji rezervacija sa zadatim ključem.");

		switch (status) {
		case CREATED:
			return transitionToCreated(reservation);
		case ACCEPTED:
			return transitionToAccepted(reservation);
		case REJECTED:
			return transitionToRejected(reservation);
		case CANCELLED:
			return transitionToCancelled(reservation);
		case FINISHED:
			return transitionToFinished(reservation);
		}

		return null;
	}

	private Reservation transitionToCreated(Reservation reservation) {
		if (reservation.getStatus().equals(ReservationStatus.CREATED))
			return reservation;
		else
			throw new BadRequestException();
	}

	private Reservation transitionToAccepted(Reservation reservation) {
		if (reservation.getStatus().equals(ReservationStatus.REJECTED))
			throw new BadRequestException("Nije moguće prihvatiti rezervaciju jer je već odbijena.");
		if (reservation.getStatus().equals(ReservationStatus.CANCELLED))
			throw new BadRequestException("Nije moguće prihvatiti rezervaciju jer je već otkazana.");
		if (reservation.getStatus().equals(ReservationStatus.FINISHED))
			throw new BadRequestException("Nije moguće prihvatiti rezervaciju jer je već završena.");
		if (reservation.getStatus().equals(ReservationStatus.ACCEPTED))
			return reservation;
		if (DateUtil.isBeforeToday(reservation.getStartDate()))
			throw new BadRequestException("Nije moguće prihvatiti rezervaciju jer prošao datum početka.");

		reservation.setStatus(ReservationStatus.ACCEPTED);
		return reservationRepository.update(reservation);
	}

	private Reservation transitionToRejected(Reservation reservation) {
		if (reservation.getStatus().equals(ReservationStatus.REJECTED))
			return reservation;
		if (reservation.getStatus().equals(ReservationStatus.CANCELLED))
			throw new BadRequestException("Nije moguće odbiti rezervaciju jer je već otkazana.");
		if (reservation.getStatus().equals(ReservationStatus.FINISHED))
			throw new BadRequestException("Nije moguće odbiti rezervaciju jer je već završena.");
		if (reservation.getStatus().equals(ReservationStatus.ACCEPTED)
				&& DateUtil.isBeforeToday(reservation.getStartDate()))
			throw new BadRequestException(
					"Nije moguće odbiti rezervaciju jer već prihvaćena i prošao je datum početka rezervacije.");

		reservation.setStatus(ReservationStatus.REJECTED);
		reservation.setGuestCanComment(true);
		return reservationRepository.update(reservation);
	}

	private Reservation transitionToCancelled(Reservation reservation) {
		if (reservation.getStatus().equals(ReservationStatus.REJECTED))
			throw new BadRequestException("Nije moguće otkazati rezervaciju jer je već odbijena.");
		if (reservation.getStatus().equals(ReservationStatus.CANCELLED))
			return reservation;
		if (reservation.getStatus().equals(ReservationStatus.FINISHED))
			throw new BadRequestException("Nije moguće otkazati rezervaciju jer je već završena.");
		if (reservation.getStatus().equals(ReservationStatus.ACCEPTED)
				&& DateUtil.isBeforeToday(reservation.getStartDate()))
			throw new BadRequestException(
					"Nije moguće otkazati rezervaciju jer već prihvaćena i prošao je datum početka rezervacije.");

		reservation.setStatus(ReservationStatus.CANCELLED);
		return reservationRepository.update(reservation);
	}

	private Reservation transitionToFinished(Reservation reservation) {
		if (reservation.getStatus().equals(ReservationStatus.FINISHED))
			return reservation;
		Date endDate = DateUtil.addDays(reservation.getStartDate(), reservation.getNumberOfNights() - 1);
		if (reservation.getStatus().equals(ReservationStatus.ACCEPTED) && DateUtil.isBeforeToday(endDate)) {
			reservation.setStatus(ReservationStatus.FINISHED);
			reservation.setGuestCanComment(true);
			return reservationRepository.update(reservation);
		}
		throw new BadRequestException("Rezervacija nije još završena.");
	}

	public Collection<Reservation> filterByGuestUsername(Collection<Reservation> reservations, String username) {
		if (StringValidator.isNullOrEmpty(username))
			return reservations;
		return CollectionUtil.findAll(reservations,
				reservation -> reservation.getGuest().getUsername().contains(username));
	}

	public Collection<Reservation> filterByStatus(Collection<Reservation> reservations,
			Collection<ReservationStatus> include) {
		if (include == null || include.isEmpty())
			return reservations;
		return CollectionUtil.findAll(reservations, reservation -> include.contains(reservation.getStatus()));
	}

	public Collection<Reservation> sortByPriceAscending(Collection<Reservation> reservations) {
		ArrayList<Reservation> sorted = new ArrayList<Reservation>(reservations);
		sorted.sort(Comparator.comparing(Reservation::getTotalPrice));
		return sorted;
	}

	public Collection<Reservation> sortByPriceDescending(Collection<Reservation> reservations) {
		ArrayList<Reservation> sorted = new ArrayList<Reservation>(reservations);
		sorted.sort(Collections.reverseOrder(Comparator.comparing(Reservation::getTotalPrice)));
		return sorted;
	}

	private Double calculatePrice(Reservation reservation) {
		Double total = 0.0;
		Double dailyBase = reservation.getApartment().getPricePerNight();
		for (Date date : DateUtil.makeList(reservation.getStartDate(), reservation.getNumberOfNights())) {
			Double priceForDate = dailyBase;
			if (holidayService.isHoldiay(date))
				priceForDate = priceForDate * holidayMultiplier;
			if (DateUtil.isWeekend(date))
				priceForDate = priceForDate * weekendMultiplier;
			total = total + dailyBase;
		}
		return total;
	}

	private void validate(Reservation reservation) {
		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (reservation.getGuest() == null || reservation.getGuest().getID() == null) {
			valid = false;
			error.append("Gost je obavezan.");
		} else {
			Guest guest = guestRepository.simpleGetByID(reservation.getGuest().getID());
			if (guest == null || guest.getDeleted()) {
				valid = false;
				error.append("Ne postoji zadati gost.");
			} else if (guest.getBlocked()) {
				valid = false;
				error.append("Ne može se kreirati rezervacija za blokiranog gosta.");
			}
		}

		if (reservation.getNumberOfNights() == null) {
			valid = false;
			error.append("Broj noćenja je obavezan.");
		} else if (reservation.getNumberOfNights() < 1) {
			valid = false;
			error.append("Broj noćenja ne može biti manji od 1.");
		}

		if (reservation.getStartDate() == null) {
			valid = false;
			error.append("Datum početka je obavezan.");
		} else if (!DateUtil.isAfterToday(reservation.getStartDate())) {
			valid = false;
			error.append("Datum početka mora biti u budućnosti.");
		}

		if (reservation.getApartment() == null || reservation.getApartment().getID() == null) {
			valid = false;
			error.append("Apartman je obavezan.");
		} else {
			Apartment apartment = apartmentService.getByID(reservation.getID());
			if (apartment == null) {
				valid = false;
				error.append("Ne postoji zadati apartman.");
			} else if (apartment.getStatus().equals(ApartmentStatus.ACTIVE)) {
				valid = false;
				error.append("Apartman mora biti aktivan.");
			} else {
				Collection<Date> reservationDates = DateUtil.makeList(reservation.getStartDate(),
						reservation.getNumberOfNights());
				if (!apartment.getAvailableDates().containsAll(reservationDates)) {
					valid = false;
					error.append("Apartman mora biti dostupan u toku rezervacije.");
				}
			}
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

}
