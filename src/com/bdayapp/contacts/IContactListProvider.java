package com.bdayapp.contacts;

import java.util.ArrayList;

public interface IContactListProvider {

	ArrayList<ContactInfo> getContactList();

	void setContactList(ArrayList<ContactInfo> contactList);
}
