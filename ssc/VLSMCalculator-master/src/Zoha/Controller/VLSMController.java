package Zoha.Controller;



import Zoha.Calculator.InvalidIPv4Exception;
import Zoha.Calculator.VLSMCalculator;
import Zoha.Calculator.VLSMReport;
import Zoha.gui.VLSMCalcEvent;

import javax.swing.*;
import java.awt.*;





public class VLSMController {

    VLSMCalculator vlsmCalculator = new VLSMCalculator();
    VLSMReport vlsmReport = new VLSMReport();
    LogUpdate logUpdate;

    public VLSMReport getReport(){
        return vlsmReport;
    }

    public LogUpdate getLogText(){
        return logUpdate;
    }


    public void calculateVLSM(VLSMCalcEvent e){
        try {
            vlsmCalculator.addNetwork(e.getNetworkIp());
            vlsmCalculator.setSubnets(e.getSubnets());

            vlsmReport.getReportSubnets().clear();
            VLSMReport report = (vlsmCalculator.calculateVLSM());

            vlsmReport.getReportSubnets().addAll(report.getReportSubnets());
            vlsmReport.setHostUsed(report.getHostUsed());
            vlsmReport.setHostsAvailable(report.getHostsAvailable());
            vlsmReport.setHostsNeeded(report.getHostsNeeded());

            logUpdate = (JLabel lbl)-> {
                    if (vlsmReport.getHostsAvailable() < vlsmReport.getHostUsed() ||
                            vlsmReport.getHostsAvailable()  < vlsmReport.getHostsNeeded() ) {
                        lbl.setForeground(new Color(255, 79, 74));
                        lbl.setText("Subnetting failed : \nNumber of IP available :" + vlsmReport.getHostsAvailable()
                                + "\n- Hosts used : " + vlsmReport.getHostUsed()
                                + "\n- Hosts needed: "+ vlsmReport.getHostsNeeded());
                    } else {
                        lbl.setForeground(Color.BLACK);
                        lbl.setText("Number of IP available :" + vlsmReport.getHostsAvailable()
                                + "\n- Ip used : "    + vlsmReport.getHostUsed()
                                + "\n- Hosts needed: "+ vlsmReport.getHostsNeeded());
                    }
            };


        } catch (InvalidIPv4Exception ex) {
            logUpdate = (JLabel lbl)->{
                lbl.setForeground(new Color(255, 79, 74));
                lbl.setText("Subnetting failed : " + e.getNetworkIp() + " is invalid IPv4 address");
            };

        }
    }



}
