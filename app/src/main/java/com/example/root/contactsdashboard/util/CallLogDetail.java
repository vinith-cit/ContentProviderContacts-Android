package com.example.root.contactsdashboard.util;

import android.net.Uri;

import java.util.Date;

/**
 * Created by root on 25/10/16.
 * CallLogDetail Pojo class for list items
 */
public class CallLogDetail {
    public String contactName;
    public String contactPhoneNumber;
    public String contactEmail;
    public String contactType;
    public Date contactLastContactTime;
    public int totalCallDuration;
    public Uri contactImage;
    public long contactId;


    public CallLogDetail(long contactId,String contactName,String contactPhoneNumber,String contactEmail,
                         String contactType,Date contactLastContactTime,int totalCallDuration,Uri contactImage ) {
        this.contactId=contactId;
        this.contactName = contactName;
        this.contactPhoneNumber=contactPhoneNumber;
        this.contactEmail=contactEmail;
        this.contactType=contactType;
        this.contactLastContactTime=contactLastContactTime;
        this.totalCallDuration=totalCallDuration;
        this.contactImage=contactImage;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {

        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String conntactPhoneNumber) {
        this.contactPhoneNumber = conntactPhoneNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public Date getContactLastContactTime() {
        return contactLastContactTime;
    }

    public void setContactLastContactTime(Date contactLastContactTime) {
        this.contactLastContactTime = contactLastContactTime;
    }

    public int getTotalCallDuration() {
        return totalCallDuration;
    }

    public void setTotalCallDuration(int totalCallDuration) {
        this.totalCallDuration = totalCallDuration;
    }

    public Uri getContactImage() {
        return contactImage;
    }

    public void setContactImage(Uri contactImage) {
        this.contactImage = contactImage;
    }
}
