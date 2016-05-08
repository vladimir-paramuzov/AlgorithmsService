package ru.unn.webservice.infrastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Statistic implements Serializable {
    private ArrayList<Date> downloads = new ArrayList<>();
    private ArrayList<Date> purchases = new ArrayList<>();

    public Statistic() { }

    public Statistic(ArrayList<Date> downloads, ArrayList<Date> purchases) {
        this.downloads = downloads;
        this.purchases = purchases;
    }

    public void addDownload(Date date) {
        downloads.add(date);
    }
    public void addPurchase(Date date) {
        purchases.add(date);
    }

    public ArrayList<Date> getDownloadsList(Date from, Date to) {
        if (from == null || to == null) {
            return downloads;
        }

        ArrayList<Date> result = new ArrayList<>();
        for (Date date : downloads) {
            if (date.after(from) && date.before(to)) {
                result.add(date);
            }
        }
        return result;
    }

    public ArrayList<Date> getPurchasesList(Date from, Date to) {
        if (from == null || to == null) {
            return purchases;
        }

        ArrayList<Date> result = new ArrayList<>();
        for (Date date : purchases) {
            if (date.after(from) && date.before(to)) {
                result.add(date);
            }
        }
        return result;
    }

    public ArrayList<Date> getDownloadsList() {
        return getDownloadsList(null, null);
    }

    public ArrayList<Date> getPurchasesList() {
        return getPurchasesList(null, null);
    }

    public int getDownloadsCount() {
        return downloads.size();
    }

    public int getPurchasesCount() {
        return purchases.size();
    }
}
