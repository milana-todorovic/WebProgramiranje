package service;

import java.util.ArrayList;
import java.util.Collection;

import beans.Amenity;
import beans.Apartment;
import beans.Comment;
import custom_exception.BadRequestException;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.CommentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.ReservationRepository;
import util.StringValidator;

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
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");

		Comment current = commentRepository.simpleGetByID(id);
		if (current == null)
			throw new BadRequestException("Ne postoji  sa zadatim ključem.");
		current.setShowing(showing);

		return commentRepository.update(current);
	}

	public void delete(Integer id) {

		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Comment comment = commentRepository.simpleGetByID(id);
		if (comment == null || comment.getDeleted())
			throw new BadRequestException("Ne postoji sadržaj apartmana sa zadatim ključem.");
		comment.setDeleted(true);
		commentRepository.update(comment);
		for (Apartment apartment : apartmentRepository.getAll()) {
			apartment.getComments().remove(comment);
			apartmentRepository.update(apartment);
		}

	}

	private void validate(Comment comment) {
		// TODO
		// apartman i gost postoje
		// postoji rezervacija sa tačnim gostom, apartmanom, i statusom (ja bih pustila
		// da ostavlja koliko hoće komentara ako može?)

		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(comment.getText())) {
			valid = false;
			error.append("Tekst komentara je obavezan.");
		} else if (comment.getRating() < 1 || comment.getRating() > 10) {
			valid = false;
			error.append("Ocena je od 1 do 10.");
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

}
