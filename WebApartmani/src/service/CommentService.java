package service;

import java.util.ArrayList;
import java.util.Collection;

import beans.Apartment;
import beans.ApartmentStatus;
import beans.Comment;
import beans.Guest;
import beans.Reservation;
import custom_exception.BadRequestException;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.CommentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.ReservationRepository;
import util.CollectionUtil;
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
			if (!comment.getDeleted())
				comments.add(comment);
		}
		return comments;
	}

	public Collection<Comment> getAllShowingInActiveApartments() {
		return CollectionUtil.findAll(getAll(),
				comment -> comment.getShowing() && comment.getApartment().getStatus().equals(ApartmentStatus.ACTIVE));
	}

	public Collection<Comment> getByApartmentID(Integer apartmentID) {
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat ključ apartmana.");
		Collection<Comment> comments = commentRepository
				.getMatching(comment -> comment.getApartment().getID().equals(apartmentID));
		comments.removeIf(comment -> comment.getDeleted());
		comments.forEach(comment -> comment.getGuest().setPassword(""));
		return comments;
	}

	public Collection<Comment> getShowingByApartmentID(Integer apartmentID) {
		return CollectionUtil.findAll(getByApartmentID(apartmentID), comment -> comment.getShowing());
	}

	public Collection<Comment> getByApartmentHostID(Integer hostID) {
		if (hostID == null)
			throw new BadRequestException("Mora biti zadat ključ domaćina.");
		Collection<Comment> comments = commentRepository
				.getMatching(comment -> comment.getApartment().getHost().getID().equals(hostID));
		comments.removeIf(comment -> comment.getDeleted());
		comments.forEach(comment -> comment.getGuest().setPassword(""));
		return comments;
	}

	public Comment getByID(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Comment comment = commentRepository.fullGetByID(id);
		if (comment == null || comment.getDeleted())
			return null;
		comment.getGuest().setPassword("");
		return comment;
	}

	public Comment create(Comment comment) {
		if (comment == null)
			throw new BadRequestException("Mora biti zadat komentar koji se dodaje.");
		comment.setDeleted(false);
		comment.setShowing(false);
		validateAndFixReferences(comment);
		if (comment.getGuest().getDeleted() || comment.getGuest().getBlocked())
			throw new BadRequestException("Gost koji ostavlja komentar ne može biti obrisan ili blokiran.");
		if (comment.getApartment().getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Apartman za koji se ostavlja komentar ne može biti obrisan.");
		Collection<Reservation> reservations = reservationRepository
				.getMatching(reservation -> reservation.getGuest().equals(comment.getGuest())
						&& reservation.getApartment().equals(comment.getApartment())
						&& reservation.getGuestCanComment());
		if (reservations.isEmpty())
			throw new BadRequestException(
					"Gost može ostaviti komentar na apartman samo ako ima rezervaciju u tom apartmanu koja je završena ili odbijena.");
		Comment created = commentRepository.create(comment);
		created.getGuest().setPassword("");
		return created;
	}

	public Comment update(Integer id, Comment comment) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (comment == null)
			throw new BadRequestException("Mora biti zadata nova vrednost komentara.");
		if (!id.equals(comment.getID()))
			throw new BadRequestException("Ključ se ne može menjati.");
		Comment current = commentRepository.simpleGetByID(id);
		if (current == null || current.getDeleted())
			throw new BadRequestException("Ne postoji komentar sa zadatim ključem.");
		if (current.getGuest().getDeleted() || current.getGuest().getBlocked())
			throw new BadRequestException("Ne može se menjati komentar čiji je kreator obrisan ili blokiran.");
		if (!current.getApartment().equals(comment.getApartment()))
			throw new BadRequestException("Apartman na koji se komentar odnosi se ne može menjati.");
		if (!current.getGuest().equals(comment.getGuest()))
			throw new BadRequestException("Gost koji je ostavio komentar se ne može menjati.");
		if (!current.getShowing().equals(comment.getShowing()))
			throw new BadRequestException("Tokom izmene komentara se ne može menjati njegov status.");
		validateAndFixReferences(comment);
		current.setRating(comment.getRating());
		current.setText(comment.getText());
		Comment updated = commentRepository.update(current);
		updated.getGuest().setPassword("");
		return updated;
	}

	public Comment changeStatus(Integer id, Boolean showing) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (showing == null)
			throw new BadRequestException("Mora biti zadata nova vrednost statusa komentara.");
		Comment current = commentRepository.simpleGetByID(id);
		if (current == null || current.getDeleted())
			throw new BadRequestException("Ne postoji komentar sa zadatim ključem.");
		current.setShowing(showing);
		Comment updated = commentRepository.update(current);
		updated.getGuest().setPassword("");
		return updated;
	}

	public void delete(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Comment comment = commentRepository.simpleGetByID(id);
		if (comment == null || comment.getDeleted())
			throw new BadRequestException("Ne postoji komentar sa zadatim ključem.");
		comment.setDeleted(true);
		commentRepository.update(comment);
	}

	private void validateAndFixReferences(Comment comment) {
		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(comment.getText())) {
			valid = false;
			error.append("Tekst komentara je obavezan.");
		}

		if (comment.getRating() == null) {
			valid = false;
			error.append("Ocena je obavezna.");
		} else if (comment.getRating() < 1 || comment.getRating() > 10) {
			valid = false;
			error.append("Ocena je od 1 do 10.");
		}

		if (comment.getGuest() == null || comment.getGuest().getID() == null) {
			valid = false;
			error.append("Gost koji je ostavio komentar je obavezan.");
		} else {
			Guest guest = guestRepository.simpleGetByID(comment.getGuest().getID());
			comment.setGuest(guest);
			if (guest == null) {
				valid = false;
				error.append("Gost koji je ostavio komentar mora postojati.");
			}
		}

		if (comment.getApartment() == null || comment.getApartment().getID() == null) {
			valid = false;
			error.append("Apartmant za koji je ostavljen komentar je obavezan.");
		} else {
			Apartment apartment = apartmentRepository.simpleGetByID(comment.getApartment().getID());
			comment.setApartment(apartment);
			if (apartment == null) {
				valid = false;
				error.append("Apartman za koji je ostavljen komentar mora postojati.");
			}
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

}
