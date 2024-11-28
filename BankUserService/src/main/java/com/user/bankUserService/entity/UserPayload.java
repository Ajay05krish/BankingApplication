package com.user.bankUserService.entity;

public class UserPayload {

	private Profile profile;
	private Credentials credentials;

	public UserPayload() {
	}

	public UserPayload(Profile profile, Credentials credentials) {
		this.profile = profile;
		this.credentials = credentials;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public static class Profile {
		private String firstName;
		private String lastName;
		private String email;
		private String login;
		private String mobilePhone;

		public Profile() {
		}

		public Profile(String firstName, String lastName, String email, String login, String mobilePhone) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.login = login;
			this.mobilePhone = mobilePhone;
		}

		// Getters and setters...
	}

	public static class Credentials {
		private Password password;

		public Credentials() {
		}

		public Credentials(Password password) {
			this.password = password;
		}

		public Password getPassword() {
			return password;
		}

		public void setPassword(Password password) {
			this.password = password;
		}

		public static class Password {
			private String value;

			public Password() {
			}

			public Password(String value) {
				this.value = value;
			}

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}
		}
	}
}
