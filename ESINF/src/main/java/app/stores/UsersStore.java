package app.stores;

import app.domain.model.AppUser;
import app.domain.shared.UserAttributes;

import java.util.HashSet;
import java.util.Set;

public class UsersStore {

    private Set<AppUser> appUserSet;

    public UsersStore(){
        this.appUserSet = new HashSet<>();
    }

    public AppUser createNewUser(String name, String email, String role) {
        return new AppUser(name, email, role);
    }

    public boolean validateUser(AppUser appUser) {
        if (appUser == null) {
            return false;
        }
        if (this.appUserSet.isEmpty()) {
            return true;
        }

        if (UserAttributes.containsEmail(appUser.getEmail())){
            return false;
        }

        return !this.appUserSet.contains(appUser);
    }


    public boolean saveUser(AppUser appUser) {
        return this.appUserSet.add(appUser);
    }


    public int getLength(){
        return this.appUserSet.size();
    }

    public boolean isEmpty() {
        return this.appUserSet.isEmpty();
    }

    public Set<AppUser> getUsers(){
        return this.appUserSet;
    }


}

