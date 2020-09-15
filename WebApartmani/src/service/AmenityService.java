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
			if (!amenity.getIsDeleted()) {
				amenities.add(amenity);
			}
		}

		return amenities;
	}

	public Amenity getByID(Integer id) {

		if (id == null)
			throw new BadRequestException("Mora biti zadat kljuè.");

		if (!amenityRepository.simpleGetByID(id).getIsDeleted()) {
			return amenityRepository.simpleGetByID(id);
		}
		return null;
	}

	public Amenity create(Amenity amenity) {
		validate(amenity);

		return amenityRepository.create(amenity);
	}

	public Amenity update(Integer id, Amenity amenity) {

		validate(amenity);

		if (id == null)
			throw new BadRequestException("Mora biti zadat kljuè.");

		if (!amenity.getID().equals(id))
			throw new BadRequestException();

		return amenityRepository.update(amenity);
	}

	public void delete(Integer id) {
		
		if(id==null)
			throw new BadRequestException("Mora biti zadat kljuè.");

		amenityRepository.simpleGetByID(id).setIsDeleted(true);
		

		for (Apartment apartment : apartmentRepository.getAll()) {
			for (Amenity amenity : apartment.getAmenities()) {
				if (amenity.getID().equals(id)) {
					apartment.getAmenities().remove(amenity);
					break;
				}
			}
		}

	}

	private void validate(Amenity amenity) {

		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(amenity.getName())) {
			valid = false;
			error.append("Naziv sadržaja je obavezan.");
		} else if (!StringValidator.isAlphaWithSpaceDash(amenity.getName())) {
			valid = false;
			error.append("Naziv sadržaja sme sadržati samo slova,razmake i crtice.");
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

}
