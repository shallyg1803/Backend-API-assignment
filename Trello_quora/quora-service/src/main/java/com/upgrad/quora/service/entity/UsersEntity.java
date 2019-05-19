package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="users",uniqueConstraints={@UniqueConstraint(columnNames={"username","email"})})
@NamedQueries(
        {
                @NamedQuery(name = "userByUsername", query = "select u from UsersEntity u where u.username = :username"),
                @NamedQuery(name = "userByEmail", query = "select u from UsersEntity u where u.email = :email"),
                @NamedQuery(name = "userByPassword", query = "select u from UsersEntity u where u.password = :password"),
                @NamedQuery(name = "userByUserId", query = "select u from UsersEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "getUserByID", query = "select u from UsersEntity u where u.id = :id"),
        }
)

public class UsersEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "firstname")
    @Size(max= 200)
    @NotNull
    private String firstname;

    @Column(name = "lastname")
    @Size(max=200)
    @NotNull
    private String lastname;

    @Column(name = "username")
    @Size(max=200)
    @NotNull
    private String username;

    @Column(name = "email")
    @NotNull
    @Size(max = 200)
    private String email;

    @ToStringExclude
    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "salt")
    @Size(max = 200)
    @NotNull
    @ToStringExclude
    private String salt;

    @Column(name = "country")
    @Size(max=200)
    private String country;

    @Column(name = "aboutme")
    @Size(max = 200)
    private String aboutme;

    @Column(name = "dob")
    @Size(max = 50)
    private String dob;

    @Column(name = "role")
    @Size(max = 50)
    private String role;

    @Column(name = "contactnumber")
    @Size(max = 50)
    private String contactnumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutme;
    }

    public void setAboutMe(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactnumber;
    }

    public void setContactNumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }



    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
