//=====================================================================================================================
// $HeadURL$
// Checked in by: $Author$
// $Date$
// $Revision$
//=====================================================================================================================

package com.bdayapp.contacts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import android.net.Uri;

import com.bdayapp.Utils;

public class ContactInfo implements Comparable<ContactInfo>, Serializable {
	private static final long serialVersionUID = -2903391245741553194L;

	private String contactId;
	private String contactName;
	private Date dateOfBirth;
	private int numOfDaysToNextBday;
	private Uri contactPhotoUri;
	private String phoneNumber;

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

	public void setContactPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
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

	public String getContactPhoneNumber() {
		return phoneNumber;
	}
	public int compareTo(ContactInfo another) {
		return numOfDaysToNextBday - another.numOfDaysToNextBday;
	}

	public static byte[] toBytes(ContactInfo info) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ObjectOutputStream objectOut = new ObjectOutputStream(out);

		objectOut.writeObject(info);

		objectOut.close();

		return out.toByteArray();
	}

	public static ContactInfo fromBytes(byte[] bytes) throws Exception
	{
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);

		ObjectInputStream oIn = new ObjectInputStream(in);

		ContactInfo info = (ContactInfo) oIn.readObject();

		return info;
	}

	public static void main(String[] args) throws Exception {

		ContactInfo info = new ContactInfo();
		info.setContactId("id");
		info.setContactName("name123");

		byte[] bytes = toBytes(info);

		info = fromBytes(bytes);

		System.out.println(info.getContactId());
		System.out.println(info.getContactName());
	}

}
