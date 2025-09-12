package com.eplugger.common.entity;

import lombok.Data;

@Data
public class EntityImpl implements Entity {

    protected String id;


    @Override
    public String getId() {
        return "".equals(this.id) ? null : this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }
        EntityImpl e = (EntityImpl) obj;
        if (getId() != null && e.getId() != null && getId().equals(e.getId())) {
            return true;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (this.getId() == null) {
            return super.hashCode();
        }
        return this.getId().hashCode();
    }
}
