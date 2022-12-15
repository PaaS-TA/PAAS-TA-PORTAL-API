package org.openpaas.paasta.portal.api.model;

import java.util.List;

public class Batch {
    List<Envelope> batch;

    public List<Envelope> getBatch() {
        return batch;
    }

    public void setBatch(List<Envelope> batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batch=" + batch +
                '}';
    }
}
