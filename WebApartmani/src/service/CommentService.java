package service;

import java.util.ArrayList;
import java.util.Collection;

import beans.Amenity;
import beans.Comment;
import custom_exception.BadRequestException;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.CommentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.ReservationRepository;

public class CommentService {

	private ReservationRepository reservationRepository;
	private GuestRepository guestRepository;
	private ApartmentRepository apartmentRepository;
	private CommentRepository commentRepository;

	public CommentService(ReservationRepository reservationRepository, GuestRepository guestRepository,
			ApartmentRepository apartmentRepository, CommentRepository commentRepository) {
		super();
		this.reservationRepository = reservationRepository;
		this.guestRepository = guestRepository;
		this.apartmentRepository = apartmentRepository;
		this.commentRepository = commentRepository;
	}

	public Collection<Comment> getAll() {

		Collection<Comment> comments = new ArrayList<Comment>();
		for (Comment comment : commentRepository.getAll()) {
			comment.getGuest().setPassword("");
			comments.add(comment);

		}
		return comments;

	}

	public Collection<Comment> getByApartmentID(Integer apartmentID) {
		// TODO isprazniti password gosta
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat klju�.");

		return apartmentRepository.fullGetByID(apartmentID).getComments();
	}

	public Comment getByID(Integer id) {

		if (id == null)
			throw new BadRequestException("Mora biti zadat klju�.");
		Comment comment = commentRepository.simpleGetByID(id);
		comment.getGuest().setPassword("");

		if (comment != null) {
			return comment;
		}
		return null;

	}

	public Comment create(Comment comment) {

		if (comment == null)
			throw new BadRequestException("Mora biti napisan komentar kako bi se mogao dodati.");
		validate(comment);
		return commentRepository.create(comment);

	}

	public Comment update(Integer id, Comment comment) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (comment == null)
			throw new BadRequestException("Mora biti napisan komentar apartmana koji se menja.");
		if (!id.equals(comment.getID()))
			throw new BadRequestException("Ključ se ne može menjati.");
		Comment current = commentRepository.simpleGetByID(id);
		if (current == null)
			throw new BadRequestException("Ne postoji  sa zadatim ključem.");
		validate(comment);
		current.setApartment(comment.getApartment());
		current.setGuest(comment.getGuest());
		current.setRating(comment.getRating());
		current.setText(comment.getText());

		return commentRepository.update(current);
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
