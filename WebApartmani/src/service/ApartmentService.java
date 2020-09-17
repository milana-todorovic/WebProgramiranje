package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import beans.Amenity;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.ApartmentType;
import beans.Base64Image;
import beans.Host;
import beans.Reservation;
import custom_exception.BadRequestException;
import repository.interfaces.AmenityRepository;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.HostRepository;
import repository.interfaces.ImageRepository;
import util.CollectionUtil;
import util.DateUtil;
import util.StringValidator;

public class ApartmentService {

	private HostRepository hostRepository;
	private ImageRepository imageRepository;
	private AmenityRepository amenityRepository;
	private ApartmentRepository apartmentRepository;

	private Date defaultCheckIn;
	private Date defaultCheckOut;

	public ApartmentService(ApartmentRepository apartmentRepository, HostRepository hostRepository,
			ImageRepository imageRepository, AmenityRepository amenityRepository) {
		super();
		this.apartmentRepository = apartmentRepository;
		this.hostRepository = hostRepository;
		this.imageRepository = imageRepository;
		this.amenityRepository = amenityRepository;
		// TODO namjesti da se default check in i check out čita iz nekog config fajla
		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			this.defaultCheckIn = format.parse("14:00:00");
			this.defaultCheckOut = format.parse("10:00:00");
		} catch (ParseException e) {
			this.defaultCheckIn = new Date();
			this.defaultCheckOut = new Date();
			e.printStackTrace();
		}
	}

	public Collection<Apartment> getTitleImages(Collection<Apartment> apartments) {
		for (Apartment apartment : apartments) {
			Collection<String> images = new ArrayList<>();
			String key = CollectionUtil.findFirst(apartment.getImageKeys(), blah -> true);
			if (key == null)
				images.add("/WebApartmani/media/noimage.jpg");
			else {
				Base64Image image = imageRepository.simpleGetByID(key);
				if (image == null)
					images.add("/WebApartmani/media/noimage.jpg");
				else
					images.add(image.getData());
			}
			apartment.setImageKeys(images);
		}
		return apartments;
	}

	public Collection<Apartment> getAll() {
		Collection<Apartment> apartments = apartmentRepository.getAll();
		apartments.removeIf(apartment -> apartment.getStatus().equals(ApartmentStatus.DELETED));
		apartments.forEach(apartment -> apartment.getHost().setPassword(""));
		return apartments;
	}

	public Collection<Apartment> getActive() {
		Collection<Apartment> apartments = apartmentRepository
				.getMatching(apartment -> apartment.getStatus().equals(ApartmentStatus.ACTIVE));
		apartments.forEach(apartment -> apartment.getHost().setPassword(""));
		return apartments;
	}

	public Collection<Apartment> getByHostID(Integer hostID) {
		if (hostID == null)
			throw new BadRequestException("Mora biti zadat ključ domaćina.");
		Collection<Apartment> apartments = apartmentRepository
				.getMatching(apartment -> hostID.equals(apartment.getHost().getID()));
		apartments.removeIf(apartment -> apartment.getStatus().equals(ApartmentStatus.DELETED));
		apartments.forEach(apartment -> apartment.getHost().setPassword(""));
		return apartments;
	}

	public Apartment getByID(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Apartment found = CollectionUtil.findFirst(getAll(), apartment -> id.equals(apartment.getID()));
		return restoreAvailableDates(found);
	}

	public Apartment create(Apartment apartment) {
		if (apartment == null)
			throw new BadRequestException("Mora biti zadat apartman koji se dodaje.");
		prepareDatesForWrite(apartment);
		validate(apartment);
		Host host = hostRepository.simpleGetByID(apartment.getHost().getID());
		if (host == null || host.getDeleted())
			throw new BadRequestException("Domaćin apartmana mora postojati.");
		else if (host.getBlocked())
			throw new BadRequestException("Ne može se kreirati apartman čiji je domaćin blokiran.");
		if (!apartment.getStatus().equals(ApartmentStatus.INACIVE))
			throw new BadRequestException("Novokreirani apartman mora imati status Neaktivan.");
		if (!apartment.getImageKeys().isEmpty())
			throw new BadRequestException("Novokreirani apartman ne može imati dodate slike.");
		Apartment created = apartmentRepository.create(apartment);
		created.getHost().setPassword("");
		return restoreAvailableDates(created);
	}

	public Apartment update(Integer id, Apartment apartment) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (apartment == null)
			throw new BadRequestException("Moraju biti zadate nove vrednosti polja apartmana.");
		Apartment current = apartmentRepository.fullGetByID(id);
		if (current == null || current.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");
		if (current.getHost().getDeleted())
			throw new BadRequestException("Ne može se menjati apartman čiji je vlasnik obrisao nalog.");
		if (!current.getHost().equals(apartment.getHost()))
			throw new BadRequestException("Vlasnik apartmana se ne može menjati.");
		if (!id.equals(apartment.getID()))
			throw new BadRequestException("Ključ se ne može menjati.");

		prepareDatesForWrite(apartment);
		validate(apartment);
		if (apartment.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Izmena apartmana se ne može koristiti za brisanje.");

		apartment.setImageKeys(current.getImageKeys());
		Apartment updated = apartmentRepository.update(apartment);
		updated.getHost().setPassword("");
		return restoreAvailableDates(updated);
	}

	public void delete(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Apartment apartment = apartmentRepository.simpleGetByID(id);
		if (apartment == null || apartment.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");
		apartment.setStatus(ApartmentStatus.DELETED);
		apartmentRepository.update(apartment);
	}

	public Collection<Base64Image> getImagesByApartmentID(Integer apartmentID) {
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat ključ apartmana.");
		Apartment apartment = apartmentRepository.simpleGetByID(apartmentID);
		if (apartment == null || apartment.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");

		Collection<Base64Image> images = new ArrayList<Base64Image>();
		for (String imageID : apartment.getImageKeys()) {
			Base64Image image = imageRepository.simpleGetByID(imageID);
			if (image != null)
				images.add(image);
		}
		return images;
	}

	public Base64Image addImage(Integer apartmentID, Base64Image image) {
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat ključ apartmana.");
		if (image == null || StringValidator.isNullOrEmpty(image.getData()))
			throw new BadRequestException("Sadržaj slike ne može biti prazan.");
		// TODO provjeri format sadržaja slike

		Apartment apartment = apartmentRepository.simpleGetByID(apartmentID);
		if (apartment == null || apartment.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");

		Base64Image saved = imageRepository.create(image);
		apartment.getImageKeys().add(saved.getID());
		apartmentRepository.update(apartment);
		return saved;
	}

	public void deleteImage(Integer apartmentID, String imageID) {
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat ključ apartmana.");
		if (StringValidator.isNullOrEmpty(imageID))
			throw new BadRequestException("Mora biti zadat ključ slike.");
		Apartment apartment = apartmentRepository.simpleGetByID(apartmentID);
		if (apartment == null || apartment.getStatus().equals(ApartmentStatus.DELETED))
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");
		if (!apartment.getImageKeys().contains(imageID))
			throw new BadRequestException("Ne postoji slika apartmana sa zadatim ključem.");
		apartment.getImageKeys().remove(imageID);
		apartmentRepository.update(apartment);
		imageRepository.deleteByID(imageID);
	}

	public Collection<Apartment> filterByAvailableDates(Collection<Apartment> apartments, Date startDate,
			Date endDate) {
		if (startDate == null && endDate == null)
			return apartments;
		if (startDate != null && endDate == null)
			throw new BadRequestException("Ukoliko je zadat datum početka, mora biti zadat i datum kraja.");
		if (startDate == null && endDate != null)
			throw new BadRequestException("Ukoliko je zadat datum kraja, mora biti zadat i datum početka.");
		Collection<Date> dates = DateUtil.makeList(startDate, endDate);
		return CollectionUtil.findAll(apartments,
				apartment -> restoreAvailableDates(apartment).getAvailableDates().containsAll(dates));
	}

	public Collection<Apartment> filterByCity(Collection<Apartment> apartments, String city) {
		if (StringValidator.isNullOrEmpty(city))
			return apartments;
		return CollectionUtil.findAll(apartments,
				apartment -> apartment.getLocation().getAddress().getCity().equals(city));
	}

	public Collection<Apartment> filterByCountry(Collection<Apartment> apartments, String country) {
		if (StringValidator.isNullOrEmpty(country))
			return apartments;
		return CollectionUtil.findAll(apartments,
				apartment -> apartment.getLocation().getAddress().getCountry().equals(country));
	}

	public Collection<Apartment> filterByPrice(Collection<Apartment> apartments, Double min, Double max) {
		Collection<Apartment> filtered = apartments;
		if (min != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getPricePerNight() >= min);
		if (max != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getPricePerNight() <= max);
		return filtered;
	}

	public Collection<Apartment> filterByNumberOfRooms(Collection<Apartment> apartments, Integer min, Integer max) {
		Collection<Apartment> filtered = apartments;
		if (min != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getNumberOfRooms() >= min);
		if (max != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getNumberOfRooms() <= max);
		return filtered;
	}

	public Collection<Apartment> filterByNumberOfGuests(Collection<Apartment> apartments, Integer min, Integer max) {
		Collection<Apartment> filtered = apartments;
		if (min != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getNumberOfGuests() >= min);
		if (max != null)
			filtered = CollectionUtil.findAll(apartments, apartment -> apartment.getNumberOfGuests() <= max);
		return filtered;
	}

	public Collection<Apartment> filterByAmenities(Collection<Apartment> apartments, Collection<Integer> include) {
		if (include == null || include.isEmpty())
			return apartments;
		final Collection<Amenity> amenities = new ArrayList<Amenity>();
		for (Integer id : include) {
			Amenity amenity = new Amenity();
			amenity.setID(id);
			amenities.add(amenity);
		}
		return CollectionUtil.findAll(apartments, apartment -> apartment.getAmenities().containsAll(amenities));
	}

	public Collection<Apartment> filterByType(Collection<Apartment> apartments, Collection<ApartmentType> include) {
		if (include == null || include.isEmpty())
			return apartments;
		return CollectionUtil.findAll(apartments, apartment -> include.contains(apartment.getApartmentType()));
	}

	public Collection<Apartment> filterByStatus(Collection<Apartment> apartments, Collection<ApartmentStatus> include) {
		if (include == null || include.isEmpty())
			return apartments;
		return CollectionUtil.findAll(apartments, apartment -> include.contains(apartment.getStatus()));
	}

	public Collection<Apartment> sortByPriceAscending(Collection<Apartment> apartments) {
		List<Apartment> sorted = new ArrayList<Apartment>(apartments);
		sorted.sort(Comparator.comparing(Apartment::getPricePerNight));
		return sorted;
	}

	public Collection<Apartment> sortByPriceDescending(Collection<Apartment> apartments) {
		List<Apartment> sorted = new ArrayList<Apartment>(apartments);
		sorted.sort(Collections.reverseOrder(Comparator.comparing(Apartment::getPricePerNight)));
		return sorted;
	}

	private void validate(Apartment apartment) {
		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(apartment.getName())) {
			valid = false;
			error.append("Naziv apartmana je obavezan. ");
		} else if (!StringValidator.isAlphanumericWithSpaceDash(apartment.getName())) {
			valid = false;
			error.append("Naziv apartmana smije sadržati samo slova, brojeve, razmake, i crtice. ");
		}

		if (apartment.getApartmentType() == null) {
			valid = false;
			error.append("Tip apartmana je obavezan. ");
		}

		if (apartment.getLocation() == null) {
			valid = false;
			error.append("Lokacija apartmana je obavezna. ");
		} else if (apartment.getLocation().getAddress() == null) {
			valid = false;
			error.append("Adresa apartmana je obavezna. ");
		} else {
			if (apartment.getLocation().getLatitude() == null || apartment.getLocation().getLongitude() == null) {
				valid = false;
				error.append("Koordinate lokacije su obavezne. ");
			}

			if (!StringValidator.isNullOrEmpty(apartment.getLocation().getAddress().getCity())
					&& !StringValidator.isAlphaWithSpaceDash(apartment.getLocation().getAddress().getCity())) {
				valid = false;
				error.append("Naziv grada smije sadržati samo slova, razmake, i crtice. ");
			}

			if (!StringValidator.isNullOrEmpty(apartment.getLocation().getAddress().getCountry())
					&& !StringValidator.isAlphaWithSpaceDash(apartment.getLocation().getAddress().getCountry())) {
				valid = false;
				error.append("Naziv države smije sadržati samo slova, razmake, i crtice. ");
			}

			if (!StringValidator.isNullOrEmpty(apartment.getLocation().getAddress().getStreet())
					&& !StringValidator.isAlphanumericWithSpaceDash(apartment.getLocation().getAddress().getStreet())) {
				valid = false;
				error.append("Naziv ulice smije sadržati samo slova, brojeve, razmake, i crtice. ");
			}

			if (apartment.getLocation().getAddress().getNumber() != null
					&& apartment.getLocation().getAddress().getNumber() <= 0) {
				valid = false;
				error.append("Broj u ulici mora biti pozitivan. ");
			}

			if (!StringValidator.isNullOrEmpty(apartment.getLocation().getAddress().getPostalCode()) && !StringValidator
					.isAlphanumericWithSpaceDash(apartment.getLocation().getAddress().getPostalCode())) {
				valid = false;
				error.append("Naziv ulice smije sadržati samo slova, brojeve, razmake, i crtice. ");
			}
		}

		if (apartment.getNumberOfRooms() == null) {
			valid = false;
			error.append("Broj soba je obavezan. ");
		} else if (apartment.getNumberOfRooms() <= 0) {
			valid = false;
			error.append("Broj soba mora biti pozitivan. ");
		}

		if (apartment.getNumberOfGuests() == null) {
			valid = false;
			error.append("Broj gostiju je obavezan. ");
		} else if (apartment.getNumberOfGuests() <= 0) {
			valid = false;
			error.append("Broj gostiju mora biti pozitivan. ");
		}

		if (apartment.getPricePerNight() == null) {
			valid = false;
			error.append("Cena je obavezna. ");
		} else if (apartment.getPricePerNight() <= 0) {
			valid = false;
			error.append("Cena mora biti pozitivna. ");
		}

		if (apartment.getCheckInTime() == null) {
			valid = false;
			error.append("Vreme za prijavu je obavezno. ");
		}

		if (apartment.getCheckOutTime() == null) {
			valid = false;
			error.append("Vreme za odjavu je obavezno. ");
		}

		if (apartment.getStatus() == null) {
			valid = false;
			error.append("Status apartmana je obavezan. ");
		}

		for (Amenity amenity : apartment.getAmenities()) {
			Amenity found = amenityRepository.simpleGetByID(amenity.getID());
			if (found == null || found.getDeleted()) {
				valid = false;
				error.append("Apartman ne može sadržati nepostojeći sadržaj apartmana. ");
				break;
			}
		}

		if (apartment.getHost() == null || apartment.getHost().getID() == null) {
			valid = false;
			error.append("Mora biti zadat domaćin apartmana. ");
		} else {
			Host host = hostRepository.simpleGetByID(apartment.getHost().getID());
			if (host == null) {
				valid = false;
				error.append("Domaćin apartmana mora postojati. ");
			}
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

	private Apartment restoreAvailableDates(Apartment apartment) {
		if (apartment == null)
			return null;
		Collection<Date> availableDates = new ArrayList<Date>(apartment.getDatesForRenting());
		for (Reservation reservation : apartment.getReservations()) {
			for (Date date : DateUtil.makeList(DateUtil.stripDate(reservation.getStartDate()),
					reservation.getNumberOfNights())) {
				availableDates.remove(date);
			}
		}
		apartment.setAvailableDates(availableDates);
		return apartment;
	}

	private void prepareDatesForWrite(Apartment apartment) {
		if (apartment.getCheckInTime() == null)
			apartment.setCheckInTime(DateUtil.stripTime(defaultCheckIn));
		else
			apartment.setCheckInTime(DateUtil.stripTime(apartment.getCheckInTime()));

		if (apartment.getCheckOutTime() == null)
			apartment.setCheckOutTime(DateUtil.stripTime(defaultCheckOut));
		else
			apartment.setCheckOutTime(DateUtil.stripTime(apartment.getCheckOutTime()));

		apartment.setDatesForRenting(DateUtil.removeDuplicateDates(apartment.getDatesForRenting()));
	}

}
