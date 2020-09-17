package service;

import java.util.ArrayList;
import java.util.Collection;

import beans.Amenity;
import beans.Apartment;
import custom_exception.BadRequestException;
import repository.interfaces.AmenityRepository;
import repository.interfaces.ApartmentRepository;
import util.StringValidator;

public class AmenityService {

	private AmenityRepository amenityRepository;
	private ApartmentRepository apartmentRepository;

	protected AmenityService(AmenityRepository amenityRepository, ApartmentRepository apartmentRepository) {
		super();
		this.amenityRepository = amenityRepository;
		this.apartmentRepository = apartmentRepository;
	}

	public Collection<Amenity> getAll() {
		Collection<Amenity> amenities = new ArrayList<Amenity>();
		for (Amenity amenity : amenityRepository.getAll()) {
			if (!amenity.getDeleted()) {
				amenities.add(amenity);
			}
		}
		return amenities;
	}

	public Amenity getByID(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Amenity amenity = amenityRepository.simpleGetByID(id);
		if (amenity != null && !amenity.getDeleted()) {
			return amenity;
		}
		return null;
	}

	public Amenity create(Amenity amenity) {
		if (amenity == null)
			throw new BadRequestException("Mora biti zadat sadržaj apartmana koji se dodaje.");
		validate(amenity);
		amenity.setDeleted(false);
		return amenityRepository.create(amenity);
	}

	public Amenity update(Integer id, Amenity amenity) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (amenity == null)
			throw new BadRequestException("Mora biti zadat sadržaj apartmana koji se menja.");		
		Amenity current = amenityRepository.simpleGetByID(id);
		if (current == null || current.getDeleted())
			throw new BadRequestException("Ne postoji sadržaj apartmana sa zadatim ključem.");
		if (!id.equals(amenity.getID()))
			throw new BadRequestException("Ključ se ne može menjati.");
		validate(amenity);
		current.setName(amenity.getName());
		return amenityRepository.update(current);
	}

	public void delete(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		Amenity amenity = amenityRepository.simpleGetByID(id);
		if (amenity == null || amenity.getDeleted())
			throw new BadRequestException("Ne postoji sadržaj apartmana sa zadatim ključem.");
		amenity.setDeleted(true);
		amenityRepository.update(amenity);
		for (Apartment apartment : apartmentRepository.getAll()) {
			apartment.getAmenities().remove(amenity);
			apartmentRepository.update(apartment);
		}
	}

	private void validate(Amenity amenity) {
		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(amenity.getName())) {
			valid = false;
			error.append("Naziv sadržaja je obavezan. ");
		} else if (!StringValidator.isAlphanumericWithSpaceDash(amenity.getName())) {
			valid = false;
			error.append("Naziv sadržaja sme sadržati samo slova, brojeve, razmake i crtice. ");
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

}
