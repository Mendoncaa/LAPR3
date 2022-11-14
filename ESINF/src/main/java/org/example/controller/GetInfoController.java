package org.example.controller;


import org.example.domain.model.AppUser;
import org.example.domain.model.Company;

import java.util.HashSet;
import java.util.Set;

public class GetInfoController {

    private Company company;
    private Set<AppUser> appUsers = new HashSet<>();

    public GetInfoController() {
        this(App.getInstance().getCompany());
    }

    public GetInfoController(Company company) {
        this.company = company;
    }

    public boolean existUsers() {
        return !this.company.getUserStore().isEmpty();
    }

    public Set<AppUser> userList(String role) {

        if (!role.equalsIgnoreCase("all")) {
            specificRole(appUsers, role);
        }
        if (role.equalsIgnoreCase("all")) {
            allRoles(appUsers);
        }
        return appUsers;
    }

    private void specificRole(Set<AppUser> list, String role) {

        for (AppUser u1 : this.company.getUserStore().getUsers()) {
            if (u1.getRole().equalsIgnoreCase(role)) {
                list.add(u1);
            }
        }
    }


    private void allRoles(Set<AppUser> list) {
        list.addAll(this.company.getUserStore().getUsers());
    }
}