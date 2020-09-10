package tests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Address;
import beans.Amenity;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.ApartmentType;
import beans.Comment;
import beans.Guest;
import beans.Holiday;
import beans.Host;
import beans.Location;
import beans.Reservation;
import beans.ReservationStatus;
import beans.User;
import repository.file_repos.FileRepositoryContainer;
import repository.generics.Entity;
import repository.generics.Repository;
import repository.interfaces.AdminRepository;
import repository.interfaces.AmenityRepository;
import repository.interfaces.ApartmentRepository;
import repository.interfaces.CommentRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.HolidayRepository;
import repository.interfaces.HostRepository;
import repository.interfaces.ReservationRepository;

@Path("/repo")
public class RepoTester {

	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Reservation> getTest(@Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		repo.getAdminRepository().getAll();
		repo.getAmenityRepository().getAll();
		repo.getCommentRepository().getAll();
		repo.getGuestRepository().getAll();
		repo.getHolidayRepository().getAll();
		repo.getHostRepository().getAll();		
		repo.getApartmentRepository().getAll();
		return repo.getReservationRepository().getAll();
	}

	@DELETE
	@Path("/test")
	public void deleteTest(@Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		clearRepo(repo.getAdminRepository());
		clearRepo(repo.getAmenityRepository());
		clearRepo(repo.getApartmentRepository());
		clearRepo(repo.getCommentRepository());
		clearRepo(repo.getGuestRepository());
		clearRepo(repo.getHolidayRepository());
		clearRepo(repo.getHostRepository());
		clearRepo(repo.getReservationRepository());
	}

	@POST
	@Path("/test")
	public void createTest(@Context ServletContext context) {
		deleteTest(context);
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		createAdmin(repo.getAdminRepository());
		createAmenities(repo.getAmenityRepository());
		createHolidays(repo.getHolidayRepository());
		createHost(repo.getHostRepository());
		createGuest(repo.getGuestRepository());
		createApartment(repo.getApartmentRepository(), repo.getHostRepository());
		createReservationsAndComments(repo.getReservationRepository(), repo.getCommentRepository(),
				repo.getGuestRepository(), repo.getApartmentRepository());
	}

	private void createGuest(GuestRepository repo) {
		repo.create(new Guest("guest1", "guest", "Vladislav", "Maksimović", "muški")); // 0
		repo.create(new Guest("guest2", "guest", "Dragana", "Čarapić", "ženski")); // 1
		repo.create(new Guest("guest3", "guest", "Janko", "Janković", "muški")); // 2
		repo.create(new Guest("guest4", "guest", "Jelena", "Šurlan", "ženski")); // 3
		repo.create(new Guest("guest5", "guest", "Mladenka", "Petković-Perendić", "ženski")); // 4
	}

	private void createReservationsAndComments(ReservationRepository res, CommentRepository comm,
			GuestRepository guests, ApartmentRepository apartments) {
		Guest jelena = guests.fullGetByID(3);
		Guest dragana = guests.fullGetByID(1);
		Guest vladislav = guests.fullGetByID(0);

		Apartment a0 = apartments.fullGetByID(0);
		Apartment a1 = apartments.fullGetByID(1);
		Apartment a2 = apartments.fullGetByID(3);

		res.create(new Reservation(a0, new GregorianCalendar(2020, Calendar.SEPTEMBER, 3).getTime(), 3, 0.0, "",
				ReservationStatus.FINISHED, vladislav, false)); // 0
		vladislav.getRentedApartments().add(a0);
		guests.update(vladislav);
		comm.create(new Comment("Odličan apartman.", 10, true, vladislav, a0));

		res.create(new Reservation(a1, new GregorianCalendar(2020, Calendar.SEPTEMBER, 20).getTime(), 3, 0.0, "",
				ReservationStatus.ACCEPTED, vladislav, false)); // 1
		vladislav.getRentedApartments().add(a1);
		guests.update(vladislav);

		res.create(new Reservation(a0, new GregorianCalendar(2020, Calendar.SEPTEMBER, 20).getTime(), 3, 0.0, "",
				ReservationStatus.REJECTED, jelena, true)); // 2
		jelena.getRentedApartments().add(a0);
		guests.update(jelena);

		res.create(new Reservation(a2, new GregorianCalendar(2020, Calendar.SEPTEMBER, 25).getTime(), 5, 0.0, "",
				ReservationStatus.CREATED, dragana, false)); // 2
		dragana.getRentedApartments().add(a2);
		guests.update(dragana);
	}

	private void createHost(HostRepository repo) {
		repo.create(new Host("host1", "host", "Marko", "Marković", "muški")); // 0
		repo.create(new Host("host2", "host", "Marica", "Marković", "ženski")); // 1
		repo.create(new Host("host3", "host", "Maksim", "Maksimović", "muški")); // 2
		repo.create(new Host("host4", "host", "Aleksandar", "Martinović", "muški")); // 3
	}

	private void createApartment(ApartmentRepository repo, HostRepository hosts) {
		Address a1 = new Address("Marka Miljanova", 9, "Novi Sad", "Srbija", "21000");
		Location l1 = new Location(45.261730, 19.852170, a1);
		Address a2 = new Address("Stražilovska", 15, "Novi Sad", "Srbija", "21000");
		Location l2 = new Location(45.250660, 19.847730, a2);
		Address a3 = new Address("Maksima Gorkog", 15, "Novi Sad", "Srbija", "21000");
		Location l3 = new Location(45.250230, 19.847020, a3);
		Address a4 = new Address("Jevrejska", 15, "Novi Sad", "Srbija", "21000");
		Location l4 = new Location(45.2529, 19.839740, a4);

		Collection<Date> datumi = new ArrayList<Date>();
		for (int i = 1; i <= 30; i++) {
			datumi.add(new GregorianCalendar(2020, Calendar.SEPTEMBER, i).getTime());
		}
		for (int i = 1; i <= 30; i++) {
			datumi.add(new GregorianCalendar(2020, Calendar.OCTOBER, i).getTime());
		}

		Date checkIn = new Date(14 * 60 * 60 * 1000);
		Date checkOut = new Date(10 * 60 * 60 * 1000);

		repo.create(new Apartment("Apartman 1", ApartmentType.FULL_APARTMENT, 3, 3, l1, 100.0, checkIn, checkOut,
				ApartmentStatus.ACTIVE, hosts.simpleGetByID(3)));
		repo.create(new Apartment("Apartman 2", ApartmentType.ROOM, 1, 2, l2, 50.0, checkIn, checkOut,
				ApartmentStatus.ACTIVE, hosts.simpleGetByID(3)));
		repo.create(new Apartment("Apartman 3", ApartmentType.ROOM, 1, 2, l3, 30.0, checkIn, checkOut,
				ApartmentStatus.INACIVE, hosts.simpleGetByID(0)));
		repo.create(new Apartment("Apartman 4", ApartmentType.FULL_APARTMENT, 3, 5, l4, 90.0, checkIn, checkOut,
				ApartmentStatus.ACTIVE, hosts.simpleGetByID(1)));
	}

	private void createAdmin(AdminRepository repo) {
		repo.create(new User("milana", "admin", "Milana", "Todorović", "ženski"));
		repo.create(new User("ana", "admin", "Ana", "Perišić", "ženski"));
	}

	private void createAmenities(AmenityRepository repo) {
		repo.create(new Amenity("Parking"));
		repo.create(new Amenity("Kuhinja"));
		repo.create(new Amenity("Pegla"));
		repo.create(new Amenity("Veš mašina"));
	}

	private void createHolidays(HolidayRepository repo) {
		repo.create(new Holiday(new GregorianCalendar(2020, Calendar.SEPTEMBER, 12).getTime()));
		repo.create(new Holiday(new GregorianCalendar(2020, Calendar.SEPTEMBER, 20).getTime()));
		repo.create(new Holiday(new GregorianCalendar(2020, Calendar.SEPTEMBER, 18).getTime()));
		repo.create(new Holiday(new GregorianCalendar(2020, Calendar.SEPTEMBER, 25).getTime()));
	}

	private <T extends Entity<ID>, ID> void clearRepo(Repository<T, ID> repo) {
		for (T entity : repo.getAll()) {
			repo.delete(entity);
		}
	}

}
