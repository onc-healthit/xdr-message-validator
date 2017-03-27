package gov.onc.xdrtesttool.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Brian on 3/9/2017.
 */
@Entity
@Table(name = "MESSAGELOG")
public class MessageLog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "REQUEST", columnDefinition = "CLOB")
    private String request;
    @Column(name = "RESPONSE", columnDefinition = "CLOB")
    private String response;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "FROM_ADDRESS")
    private String fromAddress;
    @Column(name = "DATE_LOGGED")
    private String dateLogged;
    @Version
    @Column(name = "CREATED")
    private Date created;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDateLogged() {
        return dateLogged.toString();
    }

    public void setDateLogged(String dateLogged) {
        this.dateLogged = dateLogged;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
