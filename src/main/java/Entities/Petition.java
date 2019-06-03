package Entities;

import java.util.Date;

import javax.jdo.annotations.Persistent;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Petition {
    @Id
    Long id;
    @Index
    String name;
    @Index
    Date date;
    @Persistent
    long nombreVotant;

    public Petition() {

    }
    
    public Long getId() {
    	return id;
    }
    
    public void setId(long id) {
    	this.id = id;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public Date getDate() {
    	return date;
    }
    
    public void setDate(Date date) {
    	this.date = date;
    }
    
    public long getNombreVotant() {
    	return nombreVotant;
    }
    
    public void setNombreVotant(long nombreVotant) {
    	this.nombreVotant = nombreVotant;
    }

}