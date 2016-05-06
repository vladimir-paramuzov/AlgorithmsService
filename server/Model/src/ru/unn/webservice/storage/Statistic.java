package ru.unn.webservice.storage;

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

    public int getDownloadsCount(Date from, Date to) {
        if (from == null || to == null) {
            return downloads.size();
        }

        int count = 0;
        for (Date date : downloads) {
            if (date.after(from) && date.before(to)) {
                count++;
            }
        }
        return count;
    }

    public int getPurchasesCount(Date from, Date to) {
        if (from == null || to == null) {
            return purchases.size();
        }

        int count = 0;
        for (Date date : purchases) {
            if (date.after(from) && date.before(to)) {
                count++;
            }
        }
        return count;
    }
}
