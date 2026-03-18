package Zoha.gui;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import Zoha.Calculator.Subnet;



public class VLSMCalcEvent extends EventObject implements Iterable<Subnet> {

     private String  networkIp;
     private List<Subnet> subnets = new ArrayList<>();

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public VLSMCalcEvent(Object source) {
        super(source);
    }

    public VLSMCalcEvent(Object source, String networkIp) {
        super(source);
        this.networkIp = networkIp;
    }

    public void add(String  name, Long numOfHost, Integer  mode ){

        subnets.add(new Subnet(name, numOfHost, mode));
    }


    @Override
    public String toString() {
        return "Zoha.gui.VLSMCalcEvent{" +
                "networkIp='" + networkIp + '\'' +
                ", subnets=" + subnets +
                '}';
    }

    @Override
    public Iterator<Subnet> iterator() {
        return subnets.iterator();
    }

    public List<Subnet> getSubnets() {
        return subnets;
    }

    public String getNetworkIp() {
        return networkIp;
    }
}
