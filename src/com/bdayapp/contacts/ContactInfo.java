package com.bdayapp.contacts;

import java.util.Date;

import android.net.Uri;

import com.bdayapp.Utils;

public class ContactInfo implements Comparable<ContactInfo> {
	private String contactId;
	private String contactName;
	private Date dateOfBirth;
	private int numOfDaysToNextBday;
	private Uri contactPhotoUri;

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;

		numOfDaysToNextBday = Utils.numberOfDaysToBday(dateOfBirth);
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public int getNumOfDaysToNextBday() {
		return numOfDaysToNextBday;
	}

	public void setContactPhotoUri(Uri contactPhotoUri) {
		this.contactPhotoUri = contactPhotoUri;
	}

	public Uri getContactPhotoUri() {
		return contactPhotoUri;
	}

	public int compareTo(ContactInfo another) {
		return numOfDaysToNextBday - another.numOfDaysToNextBday;
	}

}
