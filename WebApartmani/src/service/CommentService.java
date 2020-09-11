package service;

import java.util.Collection;

import beans.Comment;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.ReservationRepository;

public class CommentService {

	private ReservationRepository reservationRepository;
	private GuestRepository guestRepository;
	private ApartmentRepository apartmentRepository;

	public CommentService(ReservationRepository reservationRepository, GuestRepository guestRepository,
			ApartmentRepository apartmentRepository) {
		super();
		this.reservationRepository = reservationRepository;
		this.guestRepository = guestRepository;
		this.apartmentRepository = apartmentRepository;
	}

	public Collection<Comment> getByApartmentID(Integer apartmentID) {
		// TODO isprazniti password gosta
		return null;
	}

	public Comment getByID(Integer id) {
		// TODO isprazniti password gosta
		return null;
	}

	public Comment create(Comment comment) {
		// TODO validirati i upisati
		return null;
	}

	public Comment update(Integer id, Comment comment) {
		// TODO validirati i upisati
		return null;
	}

	public Comment changeStatus(Integer id, Boolean showing) {
		return null;
	}

	public void delete(Integer id) {
		// TODO logičko brisanje
	}

	private void validate(Comment comment) {
		// TODO
		// apartman i gost postoje
		// postoji rezervacija sa tačnim gostom, apartmanom, i statusom (ja bih pustila
		// da ostavlja koliko hoće komentara ako može?)
		// tekst nije prazan ili ne?
		// ocjena od 1 do 10
	}

}
