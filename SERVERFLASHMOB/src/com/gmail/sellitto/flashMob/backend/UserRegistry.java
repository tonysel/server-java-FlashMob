package com.gmail.sellitto.flashMob.backend;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.security.MemoryRealm;
import org.restlet.security.User;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.flashMob.commons.UserWrap;
import java.util.List;

public class UserRegistry {

	/*
	 * Classe UserRegistry, contenente le operazioni fondamentali per la gestione
	 * degli utenti. Inizializza il MemoryRealm, classe che ci permette di
	 * proteggere le risorse Web dell'applicazione con specifici vincoli e ci
	 * permette di definire quelle che sono le credenziali ed i ruoli dei singoli
	 * utenti.
	 */

	public UserRegistry() {
		realm = new MemoryRealm();
	}

	// quanti utenti sono registrati
	public int size() {
		return realm.getUsers().size();
	}

	// per restituire un particolare utente
	public User get(String username) throws InvalidException {
		User u = realm.findUser(username);
		if (u != null) {
			return u;}
		throw new InvalidException("Invalid username:" + username);
	}

	// Serve per restituire la lista di utenti
	public List<User> users() {
		return realm.getUsers();
	}

	public void add(User u) throws InvalidException {
		for (User user : realm.getUsers()) {
			if (u.getIdentifier().equalsIgnoreCase(user.getIdentifier()))
				throw new InvalidException("Duplicated Username: " + u.getIdentifier());
		}
		realm.getUsers().add(u);
	}

	// UPDATE di un oggetto utente nella lista degli utenti del Realm

	public void update(User u) {
		realm.getUsers().add(u);
	}

	// REMOVE di un oggetto utente dalla lista degli utenti del Realm

	public void remove(User u) throws InvalidException {
		if (!realm.getUsers().contains(u)) // Verifico se l'utente � presente nella lista degli utenti
			throw new InvalidException("Invalid Username: " + u.getIdentifier());
		realm.getUsers().remove(u);
	}

	// Salvataggio degli utenti nel caso in cui il server viene chiuso, senza di
	// questo si eliminerebbero

	/*
	 * Noi non abbiamo una classe "User"(in realt� l'abbiamo presa da RESTLET e non
	 * possiamo modificarla perch� � presente in una libreira ) la libreria
	 * "jackson" che ci permette con i suoi metodi(readValues, writeStringAs..) di
	 * convertire in JSON qualsiasi oggetto di qualsiasi classe di terze parti
	 */

	public void save(String fileOutName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileOutName);
		ObjectMapper mapper = new ObjectMapper();
		PrintStream ps = new PrintStream(fileOut);
		ArrayList<UserWrap> us = new ArrayList<UserWrap>();
		for (User user : realm.getUsers()) { // Gi� ho gli utenti voglio solo aggiungerne e salvarne uno nuovo perci�
												// "realm.getUsers" e non "us"
			us.add(new UserWrap(user.getIdentifier(), user.getSecret()));
		}
		String json = mapper.writeValueAsString(us);
		ps.print(json);
		ps.close();
		fileOut.close();
	}

	@SuppressWarnings("unchecked")
	public void load(String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(fileName);
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<UserWrap> us = new ArrayList<UserWrap>();
		us = mapper.readValue(fileIn, new TypeReference<ArrayList<UserWrap>>() {
		});// Creiamo un oggetto staticamente
		for (UserWrap user : us) {
			User u = new User(user.getIdentifier(), user.getSecret());
			realm.getUsers().add(u);
		}
		fileIn.close();
	}

	public MemoryRealm getRealm() {

		return realm;
	}

	// Var. per l'autenticazione che contiene tutte le info dell'utente
	private static MemoryRealm realm;

}