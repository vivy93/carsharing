package hu.uniobuda.nik.carsharing.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

/**
 * Created by pat on 2017.03.27..
 */

@IgnoreExtraProperties
public class User {

    private String name;
    private String email;
    private String password;
    private Date birthDate;
    private Boolean sex;            // 0: female, 1: male
    private String telephone;
    private Integer rating;
    // private String profileImageUrl;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    // a profilban való kilistázáshoz kellenek ezek: (aktív hirdetések)
    private List<String> acceptedAdIds;     // elfogadott hirdetések (amire acceptet nyomtam)
    private List<String> ownAdIds;          // saját hirdetések


    public User(String name, String email, String password, Date birthDate, Boolean sex, String telephone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.sex = sex;
        this.telephone = telephone;
        this.rating = 3;
    }

    // for testing
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setValue(User u) {
        name = u.name;
        email = u.email;
        password = u.password;
        birthDate = u.birthDate;
        sex = u.sex;
        telephone = u.telephone;
        rating = u.rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public List<String> getAcceptedAdIds() {
        return acceptedAdIds;
    }

    public void setAcceptedAdIds(List<String> acceptedAdIds) {
        this.acceptedAdIds = acceptedAdIds;
    }

    public List<String> getOwnAdIds() {
        return ownAdIds;
    }

    public void setOwnAdIds(List<String> ownAdIds) {
        this.ownAdIds = ownAdIds;
    }

}