package ru.unn.webservice.storage;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class LoadStatDataResponse implements IDataResponse {
    public Statistic statistic;
    public String status;

    public LoadStatDataResponse(Statistic statistic, String status) {
        this.statistic = statistic;
        this.status = status;
    }
}
