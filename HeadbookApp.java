package java271final;

import java.util.*;

class Profile {

	Name profileName;
	private String status;
	public List<Profile> friends;

	// initializes profile/status
	public Profile(Name profileName, String status) {
		this.profileName = profileName;
		this.status = status;
		this.friends = new ArrayList<>();
	}

	public void addFriend(Profile friend) {
		// checks to make sure profile doesn't already contain the other profile as a
		// friend
		if (!friends.contains(friend)) {
			friends.add(friend);
		}
	}

	public void removeFriend(Profile friend) {
		// checks to make sure that the profile contains the other profile as a friend
		if (friends.contains(friend)) {
			friends.remove(friend);
		}
	}

	// method to display profile's info
	public void display() {
		System.out.println("Name: " + profileName);
		System.out.println("\tStatus: " + status);
		System.out.println("\t# of friends: " + friends.size());
		System.out.println("Friends:");
		for (Profile friend : friends) {
			System.out.println("\t" + friend.profileName);
		}
		System.out.println("\n");
	}

	// method to set profile status
	public void setStatus(String status) {
		this.status = status;
	}
}

class Name {

	private String firstName;
	private String lastName;

	// initialize a name
	public Name(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	// getter for first name
	public String getFirstName() {
		return firstName;
	}

	// getter for last name
	public String getLastName() {
		return lastName;
	}

	// setter for first name
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	// setter for last name
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// override method to cast full name as string
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}

class ProfileManager {
	private List<Profile> allProfiles;

	// Initialize profile manager w/ an empty list
	public ProfileManager() {
		allProfiles = new ArrayList<>();
	}

	// add profile to profile manager
	public void addProfile(Profile profile) {
		allProfiles.add(profile);
	}

	// remove profile from profile manager
	public void removeProfile(Profile profile) {
		allProfiles.remove(profile);

		// removes the removed profile from other profiles friends list
		for (Profile otherProfile : allProfiles) {
			otherProfile.removeFriend(profile);
		}
	}

	// checks if profile list is empty
	public boolean emptyCheck() {
		return allProfiles.isEmpty();
	}

	// creates friendship between two profiles
	// as long as they both exist and are not
	// already friends
	public void createFriendship(Profile profile1, Profile profile2) {
	    if (profile1 == null || profile2 == null) {
	        System.out.println("The profiles were not found.");
	    } else if (profile1.friends.contains(profile2)) {
	        System.out.println("These profiles are already friends.");
	    } else {
	    	// Check if profile2 has already friended profile1
	        boolean alreadyFriends = profile2.friends.contains(profile1);

	        profile1.addFriend(profile2);
	        if (!alreadyFriends) {
	            profile2.addFriend(profile1);
	            System.out.println("\"" + profile2.profileName + "\"" + " has been added to " + "\"" + profile1.profileName
				+ "'s\" friends list.");
	            
	            
	        }
	    }
	}

	// removes friendship between two profiles
	// as long as they both exist and are
	// already friends
	public boolean endFriendship(Profile profile1, Profile profile2) {
		if (profile1 == null || profile2 == null) {
			System.out.println("The profiles were not found.");
			return false;
		} else if (!profile1.friends.contains(profile2)) {
			System.out.println("These profiles are not friends.");
			return false;
		} else {
			profile1.removeFriend(profile2);
			profile2.removeFriend(profile1);
			return true;
		}

	}

	// displays all profiles in the list along with their statuses
	public void displayAllProfiles() {
		for (Profile profile : allProfiles) {
			profile.display();
		}
	}

	// creates a new profile
	public Profile createProfile(String firstName, String lastName, String status) {
		Profile newProfile = new Profile(new Name(firstName, lastName), status);
		addProfile(newProfile);
		return newProfile;
	}

	// locates the profiles for adding/removing in various situations
	// also makes profile names case-insensitive to prevent accidental duplicate
	// profiles
	public Profile findProfile(String firstName, String lastName) {
		for (Profile profile : allProfiles) {
			if (profile.profileName.getFirstName().equalsIgnoreCase(firstName)
					&& profile.profileName.getLastName().equalsIgnoreCase(lastName)) {
				return profile;
			}
		}
		return null;
	}
}

public class HeadbookApp {
	public static void main(String[] args) {

		ProfileManager manager = new ProfileManager();
		Scanner scnr = new Scanner(System.in);

		// main loop to keep program going until user decides to exit
		while (true) {
			System.out.println("\nChoose an option: ");
			System.out.println("1. Create profile");
			System.out.println("2. Remove profile");
			System.out.println("3. Add friend");
			System.out.println("4. Remove friend");
			System.out.println("5. Change status");
			System.out.println("6. Display all profiles");
			System.out.println("7. Exit\n");

			// takes user's input choice
			int usrOption = scnr.nextInt();
			scnr.nextLine(); // consumes leftover \n from the above .nextInt()

			// if user chooses to create a profile
			if (usrOption == 1) {

				System.out.println("\nEnter first name, last name, and status for the new profile:");

				String firstName = scnr.nextLine();
				String lastName = scnr.nextLine();
				String status = scnr.nextLine();

				// incomplete initial input exception handling
				if (firstName.isEmpty() || lastName.isEmpty() || status.isEmpty()) {
					System.out
							.println("Invalid input. All fields (first name, last name, and status) must be entered.");
					continue; // Restart loop and prompt user again
				}

				Profile existingProfile = manager.findProfile(firstName, lastName);
				if (existingProfile != null) {
					System.out.println("This profile already exists. Please choose a different name.");
					continue; // Restart the loop to prompt the user again
				}

				manager.createProfile(firstName, lastName, status); // creates a profile from the users given inputs

				// if user chooses to delete a profile
			} else if (usrOption == 2) {

				if (manager.emptyCheck()) {
					System.out.println("There are no profiles currently avalible");
				} else {
					System.out.println("\nEnter the first and last name of the profile to be removed:");

					String firstName = scnr.nextLine();
					String lastName = scnr.nextLine();
					String fullName = firstName + " " + lastName;
					Profile profileToRemove = manager.findProfile(firstName, lastName); // attempts to find a
																						// profile
																						// matching the user's input

					// if the users input leads to a valid profile...
					if (profileToRemove != null) {

						System.out.println("Are you sure you want to remove \"" + fullName + "'s\" profile? (Y/N)");

						String confirmation = scnr.nextLine();
						if (!confirmation.equalsIgnoreCase("y")) {
							System.out.println("Profile removal canceled.");
							continue; // Restart loop and prompt user again
						}

						manager.removeProfile(profileToRemove); // ...removes the profile
						System.out.println("Profile " + "\"" + fullName + "\"" + " removed.");
					} else {
						// if user's input does not lead to a valid profile, asks the user to try again
						System.out.println("Profile " + "\"" + fullName + "\"" + " not found. Try again.");
					}
				}
				// if user chooses to add a friend to a profile
			} else if (usrOption == 3) {

				// asks the user which profile is being worked with
				System.out.println("\nWho are you?");

				String firstName = scnr.nextLine();
				String lastName = scnr.nextLine();
				String fullName = firstName + " " + lastName;
				// attempts to find a profile matching the user's input
				Profile currentProfile = manager.findProfile(firstName, lastName);

				if (currentProfile != null) {
					System.out.println("\nEnter the first and last name of the profile to befriend:");

					String friendFirstName = scnr.nextLine();
					String friendLastName = scnr.nextLine();
					String friendFullName = friendFirstName + " " + friendLastName;
					// attempts to find another profile matching the user's input
					Profile profileAddFriend = manager.findProfile(friendFirstName, friendLastName);

					// prevents user from adding the current profile as it's own friend
					if (profileAddFriend != null && !profileAddFriend.equals(currentProfile)) {
						manager.createFriendship(currentProfile, profileAddFriend);
					} else {
						System.out.println("\nProfile " + "\"" + friendFullName + "\"" + " not found. Try again.");
					}

				} else {
					System.out.println("\nProfile " + "\"" + fullName + "\"" + " not found. Try again.");
				}

				// if user chooses to remove a friend from a profile
			} else if (usrOption == 4) {

				if (manager.emptyCheck()) {
					System.out.println("There are no profiles currently avalible");
				} else {

					System.out.println("\nWho are you?");

					String firstName = scnr.nextLine();
					String lastName = scnr.nextLine();
					String fullName = firstName + " " + lastName;
					Profile currentProfile = manager.findProfile(firstName, lastName);

					if (currentProfile != null) {
						System.out
								.println("\nEnter the first and last name of the profile to remove from friends list:");

						String friendFirstName = scnr.nextLine();
						String friendLastName = scnr.nextLine();
						String friendFullName = friendFirstName + " " + friendLastName;
						Profile profileRemoveFriend = manager.findProfile(friendFirstName, friendLastName);

						if (profileRemoveFriend != null) {
							boolean friendshipEnded = manager.endFriendship(currentProfile, profileRemoveFriend);
							if (friendshipEnded) {
								System.out.println("\"" + friendFullName + "\"" + " has been removed from " + "\""
										+ fullName + "'s\" friends list.");
							} else {
								System.out.println("\"" + friendFullName + "\"" + " is not in " + "\"" + fullName
										+ "'s\" friends list.");
							}
						} else {
							System.out.println("\nProfile " + "\"" + friendFullName + "\"" + " not found. Try again.");
						}

					} else {
						System.out.println("\nProfile " + "\"" + fullName + "\"" + " not found. Try again.");
					}
				}

				// if user chooses to change a profile's status
			} else if (usrOption == 5) {
				if (manager.emptyCheck()) {
					System.out.println("There are no profiles currently avalible");
				} else {

					System.out.println("\nEnter the first and last name of the profile whose status is to be changed:");

					String firstName = scnr.nextLine();
					String lastName = scnr.nextLine();
					String fullName = firstName + " " + lastName;
					Profile profileChangeStatus = manager.findProfile(firstName, lastName);

					if (profileChangeStatus != null) {
						System.out.println("\nEnter your status update:");
						String newStat = scnr.nextLine();
						profileChangeStatus.setStatus(newStat);

						if (newStat.isEmpty()) {
							System.out.println("Invalid input. Status update cannot be empty.");
							continue; // Restart loop and prompt user again
						}

						System.out.println("\"" + fullName + "'s\"" + " new status is: " + newStat);
					} else {
						System.out.println("\nProfile " + "\"" + fullName + "\"" + " not found. Try again.");
					}
				}
				// if user wants to display all profiles and the profiles' friends
			} else if (usrOption == 6) {

				if (manager.emptyCheck()) {
					System.out.println("There are no profiles currently avalible");
				} else {
					// displays all current profiles
					manager.displayAllProfiles();
				}
				// if the user wants to end the program
			} else if (usrOption == 7) {
				System.out.println("Goodbye!");
				break;// ends program
			} else {
				System.out.println("\nInviald entry. Try again.");
			}

		}
		scnr.close();
	}
}