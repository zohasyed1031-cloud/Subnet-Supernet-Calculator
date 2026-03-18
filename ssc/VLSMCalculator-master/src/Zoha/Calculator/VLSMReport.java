package Zoha.Calculator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class VLSMReport implements Iterable<VLSMReportSubnet>{

    private  long   hostsAvailable;
    private  long   hostUsed;
    private  long   hostsNeeded;

    private List<VLSMReportSubnet> reportSubnets = new ArrayList<>();

    public long getHostsAvailable() {
        return hostsAvailable;
    }

    public void setHostsAvailable(long hostsAvailable) {
        this.hostsAvailable = hostsAvailable;
    }

    public long getHostUsed() {
        return hostUsed;
    }

    public void setHostUsed(long hostUsed) {
        this.hostUsed = hostUsed;
    }

    @Override
    public Iterator<VLSMReportSubnet> iterator() {
        return reportSubnets.iterator();
    }

    public List<VLSMReportSubnet> getReportSubnets() {
        return reportSubnets;
    }

    public void setReportSubnets(List<VLSMReportSubnet> reportSubnets) {
        this.reportSubnets = reportSubnets;
    }

    public long getHostsNeeded() {
        return hostsNeeded;
    }

    public void setHostsNeeded(long hostsNeeded) {
        this.hostsNeeded = hostsNeeded;
    }
}

