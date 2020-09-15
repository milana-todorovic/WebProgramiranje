package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import beans.Amenity;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.ApartmentType;
import beans.Base64Image;
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
			this.defaultCheckIn = new SimpleDateFormat("HH:mm:ss").parse("14:00:00");
			this.defaultCheckOut = new SimpleDateFormat("HH:mm:ss").parse("10:00:00");
		} catch (ParseException e) {
			this.defaultCheckIn = new Date();
			this.defaultCheckOut = new Date();
			e.printStackTrace();
		}
	}

	public Collection<Apartment> getAll() {
		Collection<Apartment> apartments = apartmentRepository.getAll();
		apartments.removeIf(apartment -> apartment.getStatus().equals(ApartmentStatus.DELETED));
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
		prepareDatesForWrite(apartment);
		validate(apartment);
		if (!apartment.getStatus().equals(ApartmentStatus.INACIVE))
			throw new BadRequestException("Novokreirani apartman mora imati status Neaktivan.");
		if (!apartment.getImageKeys().isEmpty())
			throw new BadRequestException("Novokreirani apartman ne može imati dodate slike.");
		return apartmentRepository.create(apartment);
	}

	public Apartment update(Integer id, Apartment apartment) {
		prepareDatesForWrite(apartment);
		validate(apartment);
		// TODO validirati i sačuvati
		// polje sa slikama ostaviti staro ovde
		return null;
	}

	public void delete(Integer id) {
		// TODO logičko brisanje
	}

	public Collection<Base64Image> getImagesByApartmentID(Integer apartmentID) {
		if (apartmentID == null)
			throw new BadRequestException("Mora biti zadat ključ apartmana.");
		Apartment apartment = apartmentRepository.simpleGetByID(apartmentID);
		if (apartment == null)
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
		if (apartment == null)
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
		if (apartment == null)
			throw new BadRequestException("Ne postoji apartman sa zadatim ključem.");
		if (!apartment.getImageKeys().contains(imageID))
			throw new BadRequestException("Ne postoji slika apartmana sa zadatim ključem.");
		apartment.getImageKeys().remove(imageID);
		apartmentRepository.update(apartment);
		imageRepository.deleteByID(imageID);
	}

	public Collection<Apartment> filterByAvailableDates(Collection<Apartment> apartments, Date startDate,
			Date endDate) {
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
		// TODO
		// host postoji i nije obrisan / blokiran
		// broj soba, broj gostiju, cena > 0
		// amenities postoje
		// vrijeme za prijavu veće od vremena za odjavu?
		// za lokaciju da je uneseno sve
		// imaju li koordinate neka ograničenja?
	}

	private Apartment restoreAvailableDates(Apartment apartment) {
		if (apartment == null)
			return null;
		Set<Date> availableDates = new HashSet<Date>(apartment.getDatesForRenting());
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
			apartment.setCheckInTime(defaultCheckIn);
		else
			apartment.setCheckInTime(DateUtil.stripTime(apartment.getCheckInTime()));

		if (apartment.getCheckOutTime() == null)
			apartment.setCheckOutTime(defaultCheckOut);
		else
			apartment.setCheckOutTime(apartment.getCheckOutTime());

		apartment.setDatesForRenting(DateUtil.removeDuplicateDates(apartment.getDatesForRenting()));
	}

}
