package service;

import java.util.Collection;
import java.util.Date;

import beans.Apartment;
import beans.ApartmentType;
import beans.Base64Image;
import repository.interfaces.AmenityRepository;
import repository.interfaces.HostRepository;
import repository.interfaces.ImageRepository;

public class ApartmentService {

	private HostRepository hostRepository;
	private ImageRepository imageRepository;
	private AmenityRepository amenityRepository;

	public ApartmentService(HostRepository hostRepository, ImageRepository imageRepository,
			AmenityRepository amenityRepository) {
		super();
		this.hostRepository = hostRepository;
		this.imageRepository = imageRepository;
		this.amenityRepository = amenityRepository;
	}

	public Collection<Apartment> getAll() {
		// TODO isprazniti password domaćinima
		// šta za slike ovde?
		// dostupne datume bih ostavila prazne
		return null;
	}

	public Collection<Apartment> getByHostID(Integer hostID) {
		// TODO isprazniti password domaćinima
		// šta za slike ovde?
		// dostupne datume bih ostavila prazne
		return null;
	}

	public Apartment getByID() {
		// TODO isprazniti password domaćinu
		// učitati slike ili ne?
		// popraviti datume kad je dostupan
		return null;
	}

	public Apartment create(Apartment apartment) {
		// TODO validirati i sačuvati
		return null;
	}

	public Apartment update(Integer id, Apartment apartment) {
		// TODO validirati i sačuvati
		// polje sa slikama ostaviti staro ovde
		return null;
	}

	public void delete(Integer id) {
		// TODO logičko brisanje
	}

	public Collection<Base64Image> getImagesByApartmentID(Integer apartmentID) {
		// TODO
		return null;
	}

	public Base64Image addImage(Integer apartmentID, Base64Image image) {
		// TODO sačuvati sliku
		// vraćati sliku ili apartman?
		return null;
	}

	public void deleteImage(Integer apartmentID, String imageID) {
		// TODO
	}

	public Collection<Apartment> filterByAvailableDates(Collection<Apartment> apartments, Date startDate,
			Date endDate) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByCity(Collection<Apartment> apartments, String city) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByCountry(Collection<Apartment> apartments, String country) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByPrice(Collection<Apartment> apartments, Double min, Double max) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByNumberOfRooms(Collection<Apartment> apartments, Integer min, Integer max) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByNumberOfGuests(Collection<Apartment> apartments, Integer min, Integer max) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByAmenities(Collection<Apartment> apartments, Collection<Integer> include) {
		// TODO
		return null;
	}

	public Collection<Apartment> filterByType(Collection<Apartment> apartments, Collection<ApartmentType> include) {
		// TODO
		return null;
	}

	public Collection<Apartment> sortByPriceAscending(Collection<Apartment> apartments) {
		// TODO
		return null;
	}

	public Collection<Apartment> sortByPriceDescending(Collection<Apartment> apartments) {
		// TODO
		return null;
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

	private void restoreAvailableDates(Apartment apartment) {
		// TODO postaviti dostupne datume
		// za neaktivan ostaviti praznu listu?
	}

}
