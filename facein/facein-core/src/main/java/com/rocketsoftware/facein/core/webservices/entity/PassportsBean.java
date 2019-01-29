package com.rocketsoftware.facein.core.webservices.entity;
 
/**
 * Created by sxc on 2019/1/18.
 */
public class PassportsBean
{
    private int ID;
 
    private String PassportID;
 
    private String PhotoPath;
 
    private String CreatedDate;
    
    private String ModifiedDate;
    

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getPassportID() {
		return PassportID;
	}

	public void setPassportID(String passportID) {
		PassportID = passportID;
	}

	public String getPhotoPath() {
		return PhotoPath;
	}

	public void setPhotoPath(String photoPath) {
		PhotoPath = photoPath;
	}

	public String getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(String createdDate) {
		CreatedDate = createdDate;
	}

	public String getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		ModifiedDate = modifiedDate;
	}

}
