package ru.unn.webservice.storage;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class GetStatDataResponse implements IDataResponse {
    public int downloads;
    public int purchases;
    public String status;

    public GetStatDataResponse(int downloads, int purchases, String status) {
        this.downloads = downloads;
        this.purchases = purchases;
        this.status = status;
    }

    void print() {
        System.out.println("Downloads: " + downloads + " Purchases: " + purchases);
    }
}
