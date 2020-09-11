package service;

import java.util.Collection;

import beans.Amenity;
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
		// TODO vratiti sve osim koji su logički obrisani?
		return null;
	}

	public Amenity getByID(Integer id) {
		// TODO ako je logički obrisan ne vraćati?
		return null;
	}

	public Amenity create(Amenity amenity) {
		// TODO validirati i sacuvati
		return null;
	}

	public Amenity update(Integer id, Amenity amenity) {
		// TODO validirati i sacuvati
		// prima id da rest kontroler samo proslijedi id i tijelo puta, ako se ne
		// poklapa id u putanji i u tijelu bacati ovde exception
		return null;
	}

	public void delete(Integer id) {
		// TODO logicko brisanje, ukloniti i iz apartmana
	}

	private void validate(Amenity amenity) {
		// TODO samo provjeriti da naziv nije prazan ili još nešto?
	}

}
