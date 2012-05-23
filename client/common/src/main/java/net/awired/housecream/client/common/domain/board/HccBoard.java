package net.awired.housecream.client.common.domain.board;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "board")
public class HccBoard {

    private String name; // updatable
    private String description; // updatable
    private String notifyUrl; // updatable

    private String software;
    private String version;
    private String hardware;
    private String mac;
    private String ip;
    private Integer port;
    private List<Integer> pinIds;

    ////////////////////////////////////////////////////////

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getHardware() {
        return hardware;
    }

    public void setPinIds(List<Integer> pinIds) {
        this.pinIds = pinIds;
    }

    public List<Integer> getPinIds() {
        return pinIds;
    }

}
