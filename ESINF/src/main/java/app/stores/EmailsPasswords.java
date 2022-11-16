package app.stores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmailsPasswords implements Serializable {

    private List<String> emails;
    private List<String> passwords;

    public EmailsPasswords(){
        this.emails = new ArrayList<>();
        this.passwords = new ArrayList<>();
    }


    public void add(String email, String password){
        this.emails.add(email);
        this.passwords.add(password);
    }

    public String getEmailByID(int i){
        return this.emails.get(i);
    }



    public int size(){
        return emails.size();
    }


}