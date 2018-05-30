package com.gmail.sellitto.flashMob.frontend;

import org.restlet.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;

import com.gmail.sellitto.flashMob.backend.wrapper.GroupRegistryAPI;
import com.gmail.sellitto.flashMob.backend.wrapper.UserRegistryAPI;
import com.gmail.sellitto.flashMob.commons.InvalidException;
import com.gmail.sellitto.resources.json.GroupRegJSON;
import com.gmail.sellitto.resources.json.PhotosForGroupJSON;
import com.gmail.sellitto.resources.json.SubscribersForGroupJSON;
import com.gmail.sellitto.resources.json.TakeGroupsJSON;
import com.gmail.sellitto.resources.json.UserRegJSON;
import com.gmail.sellitto.resources.json.AlbumResource;

import com.google.gson.Gson;

public class GroupsRegistryWebApplication extends Application {
	//public static final String ROLE_H = "high"; 
	//public static final String ROLE_L = "low";

	public static MapVerifier verifier = new MapVerifier();
	public static MapVerifier verifierAuth = new MapVerifier();
	public static MemoryRealm realm;
	public static MemoryRealm realmAuth;


	private static String rootDirForPublicWebStaticFiles;
	/* Create a class for settings of server */

	private class Settings {
		public int port; 
		public String web_base_dir;
		public String groups_storage_base_dir; 
		public String groups_storage_base_file; 
		public String users_storage_base_dir; 
		public String users_storage_base_file; 
	}

	public Restlet createInboundRoot() {
		Router router = new Router(getContext()); 	

		/*
		 * Protezioni per l'accesso ai mess e agli utenti. Automaticamente, queste
		 * classi ti fanno autenticazione e registrazione
		 */
		
		for (User u : realmAuth.getUsers()) {
	      verifierAuth.getLocalSecrets().put(u.getIdentifier(), u.getSecret());
	    }
		
		for (User u : realm.getUsers()) {
		      verifier.getLocalSecrets().put(u.getIdentifier(), u.getSecret());
		    }

		// create ChallengeAuthenticator for protected access to resources
		
		Directory publicDir = new Directory(getContext(), rootDirForPublicWebStaticFiles);
	
		ChallengeAuthenticator guardObjectReg = null;
		try {
			guardObjectReg = createAuthenticator();
		} catch (IOException | InvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		guardObjectReg.setNext(publicDir);
		
		ChallengeAuthenticator guardObjectReg2 = null;
		try {
			guardObjectReg2 = createAuthenticator();
		} catch (IOException | InvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		guardObjectReg2.setNext(GroupRegJSON.class);
   
        
        publicDir.setListingAllowed(true);
        publicDir.setDeeplyAccessible(true);
        
        
        router.attach("/groupsRegApplication/web/", guardObjectReg);
        router.attach("/groupsRegApplication/web", guardObjectReg);
		
        router.attach("/groupsRegApplication/groups", guardObjectReg2);
		
		router.attach("/groupsRegApplication/groups/{fnameGroup}/{fdateGroup}/{fnameFile}", PhotosForGroupJSON.class);
		
		router.attach("/groupsRegApplication/listGroups", TakeGroupsJSON.class);
		
		router.attach("/groupsRegApplication/groups/{fnameGroup}/{fdateGroup}", AlbumResource.class);
		
		router.attach("/groupsRegApplication/subscribers/{fnameGroup}/{fdateGroup}", SubscribersForGroupJSON.class);
		
		router.attach("/groupsRegApplication/users", UserRegJSON.class);

		return router;
	
	}

	private ChallengeAuthenticator createAuthenticator() throws IOException, InvalidException {
		ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "SampleRealm"); // Creo
																														// il
																														// ChallengeAuthenticator
																											// specificando
																														// il
																														// Basic
																														// Authentication
																														// Scheme
																														// offerto
																														// da
																														// HTTP
	
			
			// Imposta il verifier (per la verifica delle credenziali) e l'enroler (per i
			// permessi del ruolo) al particolare ChallengeAuthenticator
			guard.setVerifier(realmAuth.getVerifier());
			guard.setEnroler(realmAuth.getEnroler());
			//guard.setOwner("high");
			return guard;
	}

	public static void main(String[] args) throws IOException, InvalidException {

		Gson gson = new Gson();
		Settings settings = null;

		// Inizializzo le impostazioni del server basandomi sulle informazioni contenute
		// nel file settings.json

		try {
			Scanner scanner = new Scanner(new File("settings.json"));
			settings = gson.fromJson(scanner.nextLine(), Settings.class);
			scanner.close();
			System.err.println("Loading settings from file");
		} catch (FileNotFoundException e1) {
			System.err.println("Settings file not found");
			System.exit(-1);
		}
		
		
		rootDirForPublicWebStaticFiles="file://"+System.getProperty("user.dir")+"/"+settings.web_base_dir;
		System.err.println("Web Public Directory: " + rootDirForPublicWebStaticFiles);
		

		// Richiamo le singole istanze (sono singleton) delle API per i registri di
		// oggetti e utenti ed effettuo il restore

		GroupRegistryAPI grapi = GroupRegistryAPI.instance();
		grapi.setStorageFiles(System.getProperty("user.dir") + "/" + settings.groups_storage_base_dir + "/",
				settings.groups_storage_base_file); // Imposto i file di storage
		grapi.restore();

		
		UserRegistryAPI urapi = UserRegistryAPI.instance();
		urapi.setStorageFiles(System.getProperty("user.dir") + "/" + settings.users_storage_base_dir + "/",
				settings.users_storage_base_file); // Imposto i file di storage
		
		urapi.restore();
		realm = urapi.getRealm();
		realmAuth = new MemoryRealm();
		FileReader f;
	    f=new FileReader("authentication.txt");
	    
	    BufferedReader b =new BufferedReader(f);
	
	    while(true) {
		String s1 = b.readLine();
		String s2 = b.readLine();
		if (s1 ==null) break;
	
		User u1 = new User(s1, s2);
		
		if (realmAuth.findUser(s1) == null) {
	
		   realmAuth.getUsers().add(u1);
		   realmAuth.map(u1, null, "high");
		   
		     }
		}
	    b.close();
	    

		try {
			Component component = new Component(); // Creo la componente Restlet
			component.getServers().add(Protocol.HTTP, settings.port); // Imposto le informazioni relative al Server
			component.getClients().add(Protocol.FILE); // Imposto le informazioni relative al Client
			component.getDefaultHost().attach(new GroupsRegistryWebApplication()); // Allego l'Application al
																					// DefaultHost

			// Avvio la componente Restlet
			component.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
