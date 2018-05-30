package com.gmail.sellitto.flashMob.commons;

import java.io.Serializable;
/* 
 * Classe wrapper (o di "rivestimento") che fa da contenitore per le informazioni relative all'utente.
 * Utilizzata per permettere la conversione toJSON e fromJSON nel trasferimento tra client/server.
 * (Pu� essere vista come una entry della lista di utenti del realm, con relativo ruolo, ovvero identifier + secret + role)
 * (Realm o User risultano non serializzabili, propriet� utile per la persistenza su file)
 */

public class UserWrap implements Serializable {
	

	// Costruttore oggetto UserWrap

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserWrap(String identifier, char[] secret) {
		this.identifier = identifier;
		this.secret = secret;
		
	}


	// Costruttore vuoto per persistenza (richiesto dalla libreria JACKSON e
	// dall'ObjectMapper)

	public UserWrap() {
	}

	// GET dell'identifier

	public String getIdentifier() {
		return identifier;
	}

	// GET della secret

	public char[] getSecret() {
		return secret;
	}
	
	

	private char[] secret;
	private String identifier;
	
}
