package service;

import java.io.File;
import java.util.Collection;

import beans.User;
import beans.UserRole;
import repository.interfaces.AdminRepository;
import repository.interfaces.GuestRepository;
import repository.interfaces.HostRepository;
import repository.util.PersistentSequencer;

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
	}

	public Collection<User> getAll() {
		// TODO obavezno isprazniti password
		return null;
	}

	public User getByID(Integer id) {
		// TODO obavezno isprazniti password
		return null;
	}

	public User getByUsernameAndPassword(String username, String password) {
		// TODO obavezno isprazniti password
		// da li ovde bacati bad request za prazne parametre?
		return null;
	}

	public User create(User user) {
		// TODO validirati i upisati
		// mislim da ne moraju odvojene metode za čuvanje gosta i domaćina jer će
		// se isto validirati?
		// postaviti id ovde da bi svi tipovi korisnika imali zajednički
		return null;
	}

	public User update(Integer id, User user) {
		// TODO validirati i sačuvati
		// ovde paziti na to da se dobije user bez passworda i da ga treba ubaciti nazad
		// osim generalno validacije entiteta provjeriti da nije promjenjeno polje za
		// blokiranje / logičko brisanje
		return null;
	}

	public void changePassword(Integer id, String oldPassword, String newPassword) {
		// TODO vratiti void ili User? kontam da je bolje void
		// validirati da nisu prazni parametri i da je stari pass tačan
	}

	public void changeBlockStatus(Integer id, Boolean blocked) {
		// TODO vratiti User ili ne? dodati blokiranje kao atribut
	}

	public void delete(Integer id) {
		// TODO napraviti logičko brisanje
	}

	public Collection<User> filterByRole(Collection<User> users, UserRole role) {
		// TODO
		return null;
	}

	public Collection<User> filterByGender(Collection<User> users, String gender) {
		// TODO vidjeti kako ovo tačno raditi pošto nam je pol prilično fleksibilno
		// definisan
		// možda osim ove metode dodati i verziju koja ostavi sve osim proslijeđenog?
		// pa da onda možemo na frontu da ponudimo tipa da pretraži kao muški, ženski, i
		// ostali
		// da ne mora baš da kuca
		return null;
	}

	public Collection<User> filterByUsername(Collection<User> users, String username) {
		// TODO ovde raditi equals ili contains?
		return null;
	}

	private void validate(User user) {
		// TODO
		// username, ime, prezime, pol ne smiju biti prazni
		// username mora biti jedinstven
		// još nešto?
	}

}
