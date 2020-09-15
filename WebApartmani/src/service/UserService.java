package service;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import beans.Apartment;
import beans.ApartmentStatus;
import beans.Guest;
import beans.Host;
import beans.User;
import beans.UserRole;
import custom_exception.BadRequestException;
import custom_exception.InternalException;
import dto.PersonalInformationDTO;
import repository.interfaces.AdminRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.HostRepository;
import repository.util.PersistentSequencer;
import util.CollectionUtil;
import util.StringValidator;

public class UserService {

	private AdminRepository adminRepository;
	private HostRepository hostRepository;
	private GuestRepository guestRepository;
	private PersistentSequencer sequencer;

	protected UserService(AdminRepository adminRepository, HostRepository hostRepository,
			GuestRepository guestRepository, String repoPath) {
		super();
		this.adminRepository = adminRepository;
		this.hostRepository = hostRepository;
		this.guestRepository = guestRepository;
		this.sequencer = new PersistentSequencer(repoPath + File.separator + "userid.json");
		adminInit();
	}

	public Collection<User> getAll() {
		Collection<User> users = internalGetAll();
		users.forEach(user -> user.setPassword(""));
		return users;
	}

	public Collection<User> getGuestsByHostID(Integer hostID) {
		if (hostID == null)
			throw new BadRequestException("Mora biti zadat ključ domaćina.");
		Collection<Guest> guests = guestRepository.getAll();
		guests.removeIf(guest -> !guest.getDeleted());
		Collection<Guest> filtered = CollectionUtil.findAll(guests, guest -> hasGuestVisitedHost(guest, hostID));
		filtered.forEach(guest -> guest.setPassword(""));
		return new ArrayList<User>(filtered);
	}

	private Boolean hasGuestVisitedHost(Guest guest, Integer hostID) {
		return CollectionUtil.contains(guest.getRentedApartments(),
				apartment -> hostID.equals(apartment.getHost().getID()));
	}

	public User getByID(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		return CollectionUtil.findFirst(getAll(), user -> id.equals(user.getID()));
	}

	public User getByUsernameAndPassword(String username, String password) {
		if (StringValidator.isNullOrEmpty(username) || StringValidator.isNullOrEmpty(password))
			throw new BadRequestException("Obavezno je uneti korisničko ime i lozinku.");
		String hashPassword = hashPassword(password);
		User found = CollectionUtil.findFirst(internalGetAll(),
				user -> username.equals(user.getUsername()) && hashPassword.equals(user.getPassword()));
		return clearPassword(found);
	}

	public User create(User user) {
		if (user == null)
			throw new BadRequestException("Mora biti zadat korisnik koji se dodaje.");
		user.setID(null);
		validate(user);
		if (user.getRole().equals(UserRole.ADMIN))
			throw new BadRequestException("Ne može se kreirati administratorski nalog.");
		user.setID(sequencer.generateID());
		user.setPassword(hashPassword(user.getPassword()));
		user.setBlocked(false);
		user.setDeleted(false);
		if (user.getRole().equals(UserRole.GUEST))
			return clearPassword(guestRepository.create(toGuest(user)));
		else
			return clearPassword(hostRepository.create(toHost(user)));
	}

	public User updatePersonalInfo(Integer id, PersonalInformationDTO info) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (info == null)
			throw new BadRequestException("Moraju biti zadate nove vrednosti polja korisnika.");
		User user = internalGetByID(id);
		if (user.getBlocked())
			throw new BadRequestException("Nije dozvoljeno menjati podatke blokiranog korisnika.");
		if (!StringValidator.isNullOrEmpty(info.getName()))
			user.setName(info.getName());
		if (!StringValidator.isNullOrEmpty(info.getSurname()))
			user.setSurname(info.getSurname());
		if (!StringValidator.isNullOrEmpty(info.getUsername()))
			user.setUsername(info.getUsername());
		if (!StringValidator.isNullOrEmpty(info.getGender()))
			user.setGender(info.getGender());
		validate(user);
		return dispatchUpdate(user);
	}

	public void changePassword(Integer id, String oldPassword, String newPassword) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (StringValidator.isNullOrEmpty(oldPassword) || StringValidator.isNullOrEmpty(newPassword))
			throw new BadRequestException("Obavezno je uneti novu i staru lozinku.");
		User user = internalGetByID(id);
		if (user.getBlocked())
			throw new BadRequestException("Nije dozvoljeno menjati lozinku blokiranog korisnika.");
		String hashPassword = hashPassword(oldPassword);
		if (!user.getPassword().equals(hashPassword))
			throw new BadRequestException("Unesena stara lozinka nije ispravna.");
		user.setPassword(hashPassword(newPassword));
		dispatchUpdate(user);
	}

	public void changeBlockStatus(Integer id, Boolean blocked) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		if (blocked == null)
			throw new BadRequestException("Mora biti zadat novi status blokiranja.");
		User user = internalGetByID(id);
		if (user.getRole().equals(UserRole.ADMIN))
			throw new BadRequestException("Ne može se blokirati administratorski nalog.");
		user.setBlocked(blocked);
		dispatchUpdate(user);
	}

	public void delete(Integer id) {
		if (id == null)
			throw new BadRequestException("Mora biti zadat ključ.");
		User user = internalGetByID(id);
		if (user.getRole().equals(UserRole.ADMIN))
			throw new BadRequestException("Ne može se obrisati administratorski nalog.");
		else if (user.getRole().equals(UserRole.HOST)) {
			Host host = hostRepository.fullGetByID(id);
			for (Apartment apartment : host.getApartments()) {
				if (apartment.getStatus().equals(ApartmentStatus.ACTIVE))
					throw new BadRequestException("Ne može se obrisati nalog domaćina dok ima aktivne apartmane.");
			}
		}
		user.setDeleted(true);
		dispatchUpdate(user);
	}

	public Collection<User> filterByRole(Collection<User> users, UserRole role) {
		if (role == null)
			return users;
		return CollectionUtil.findAll(users, user -> user.getRole().equals(role));
	}

	public Collection<User> filterByGenderInclude(Collection<User> users, String genderToInclude) {
		if (StringValidator.isNullOrEmpty(genderToInclude))
			return users;
		return CollectionUtil.findAll(users, user -> user.getGender().equals(genderToInclude));
	}

	public Collection<User> filterByGenderExclude(Collection<User> users, Collection<String> gendersToExclude) {
		return CollectionUtil.findAll(users, user -> !gendersToExclude.contains(user.getGender()));
	}

	public Collection<User> filterByUsername(Collection<User> users, String username) {
		if (StringValidator.isNullOrEmpty(username))
			return users;
		return CollectionUtil.findAll(users, user -> user.getUsername().contains(username));
	}

	private void validate(User user) {
		if (user == null)
			throw new BadRequestException("Mora biti zadat korisnik.");

		Boolean valid = true;
		StringBuilder error = new StringBuilder();

		if (StringValidator.isNullOrEmpty(user.getUsername())) {
			valid = false;
			error.append("Korisničko ime je obavezno.");
		} else if (!StringValidator.isAlphanumeric(user.getUsername())) {
			valid = false;
			error.append("Korisničko ime smije sadržati samo slova i brojeve.");
		} else {
			User sameUsername = internalGetByUsername(user.getUsername());
			if (sameUsername != null && !sameUsername.getID().equals(user.getID())) {
				valid = false;
				error.append("Korisničko ime mora biti jedinstveno.");
			}
		}

		if (StringValidator.isNullOrEmpty(user.getPassword())) {
			valid = false;
			error.append("Lozinka je obavezna.");
		}

		if (StringValidator.isNullOrEmpty(user.getName())) {
			valid = false;
			error.append("Ime je obavezno.");
		} else if (!StringValidator.isAlphaWithSpaceDash(user.getName())) {
			valid = false;
			error.append("Ime smije sadržati samo slova, razmake, i crtice.");
		}

		if (StringValidator.isNullOrEmpty(user.getSurname())) {
			valid = false;
			error.append("Prezime je obavezno.");
		} else if (!StringValidator.isAlphaWithSpaceDash(user.getSurname())) {
			valid = false;
			error.append("Prezime smije sadržati samo slova, razmake, i crtice.");
		}

		if (StringValidator.isNullOrEmpty(user.getGender())) {
			valid = false;
			error.append("Pol je obavezan.");
		} else if (!StringValidator.isAlphaWithSpaceDash(user.getGender())) {
			valid = false;
			error.append("Pol smije sadržati samo slova, razmake, i crtice.");
		}

		if (user.getRole() == null) {
			valid = false;
			error.append("Uloga je obavezna.");
		}

		if (!valid)
			throw new BadRequestException(error.toString());
	}

	private Collection<User> internalGetAll() {
		Collection<User> users = new ArrayList<User>();
		CollectionUtil.addIf(users, adminRepository.getAll(), user -> !user.getDeleted());
		CollectionUtil.addIf(users, guestRepository.getAll(), user -> !user.getDeleted());
		CollectionUtil.addIf(users, hostRepository.getAll(), user -> !user.getDeleted());
		return users;
	}

	private User internalGetByID(Integer id) {
		User found = CollectionUtil.findFirst(internalGetAll(), user -> id.equals(user.getID()));
		if (found == null)
			throw new BadRequestException("Nije pronađen korisnik sa zadatim ključem.");
		return found;
	}

	private User internalGetByUsername(String username) {
		return CollectionUtil.findFirst(internalGetAll(), user -> username.equals(user.getUsername()));
	}

	private User dispatchUpdate(User user) {
		if (user.getRole().equals(UserRole.ADMIN))
			return clearPassword(adminRepository.update(user));
		else if (user.getRole().equals(UserRole.HOST))
			return clearPassword(hostRepository.update(toHost(user)));
		else
			return clearPassword(guestRepository.update(toGuest(user)));
	}

	private Guest toGuest(User user) {
		Guest guest = new Guest();
		guest.setUsername(user.getUsername());
		guest.setPassword(user.getPassword());
		guest.setName(user.getName());
		guest.setSurname(user.getSurname());
		guest.setGender(user.getGender());
		guest.setBlocked(user.getBlocked());
		guest.setDeleted(user.getDeleted());
		guest.setID(user.getID());
		guest.setRole(UserRole.GUEST);
		return guest;
	}

	private Host toHost(User user) {
		Host host = new Host();
		host.setUsername(user.getUsername());
		host.setPassword(user.getPassword());
		host.setName(user.getName());
		host.setSurname(user.getSurname());
		host.setGender(user.getGender());
		host.setBlocked(user.getBlocked());
		host.setDeleted(user.getDeleted());
		host.setID(user.getID());
		host.setRole(UserRole.HOST);
		return host;
	}

	private String hashPassword(String password) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new InternalException("RIP");
		}
		digest.reset();
		byte[] digested = digest.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		for (byte b : digested) {
			sb.append(Integer.toHexString(0xff & b));
		}
		return sb.toString();
	}

	private User clearPassword(User user) {
		if (user != null)
			user.setPassword("");
		return user;
	}

	private void adminInit() {
		if (adminRepository.getAll().isEmpty()) {
			// TODO zamjeniti ovo učitavanjem iz fajla
			User admin = new User("admin", "admin", "Admin", "Admin", "ženski");
			admin.setID(sequencer.generateID());
			admin.setPassword(hashPassword(admin.getPassword()));
			adminRepository.create(admin);
		}
	}

}
