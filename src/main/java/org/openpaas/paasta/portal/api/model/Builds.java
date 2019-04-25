package org.openpaas.paasta.portal.api.model;

public class Builds {
    private String packageId;
    private String buildPack;
    private String stack;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getBuildPack() {
        return buildPack;
    }

    public void setBuildPack(String buildPack) {
        this.buildPack = buildPack;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    @Override
    public String toString() {
        return "Builds{" +
                "packageId='" + packageId + '\'' +
                ", buildPack='" + buildPack + '\'' +
                ", stack='" + stack + '\'' +
                '}';
    }
}
