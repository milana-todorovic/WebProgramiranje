package service;

import java.util.ArrayList;
import java.util.Collection;

import beans.Amenity;
import beans.Apartment;
import custom_exception.BadRequestException;
import repository.interfaces.AmenityRepository;
import repository.interfaces.ApartmentRepository;

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

		if (!amenityRepository.simpleGetByID(id).getIsDeleted()) {
			return amenityRepository.simpleGetByID(id);
		}
		return null;
	}

	public Amenity create(Amenity amenity) {
		// TODO validirati,naziv prazan

		return amenityRepository.create(amenity);
	}

	public Amenity update(Integer id, Amenity amenity) {
		// TODO validirati
		if (!amenity.getID().equals(id))
			throw new BadRequestException();

		return amenityRepository.update(amenity);
	}

	public void delete(Integer id) {

		amenityRepository.simpleGetByID(id).setIsDeleted(true);

		for (Apartment apartment : apartmentRepository.getAll()) {
			for (Amenity amenity : apartment.getAmenities()) {
				if (amenity.getID().equals(id)) {
					apartment.getAmenities().remove(amenity);
					break;
				}
			}
		}

		amenityRepository.deleteByID(id);

	}

	private void validate(Amenity amenity) {

		if (amenity.getName().isEmpty())
			throw new BadRequestException();
	}

}
