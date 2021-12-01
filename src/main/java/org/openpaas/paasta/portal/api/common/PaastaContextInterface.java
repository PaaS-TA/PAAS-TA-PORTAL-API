package org.openpaas.paasta.portal.api.common;

import java.util.Date;

public abstract class PaastaContextInterface {

    private Date create_time;

    public Date getCreate_time() {
        return create_time == null ? null : new Date(create_time.getTime());
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time == null ? null : new Date(create_time.getTime());
    }
}
