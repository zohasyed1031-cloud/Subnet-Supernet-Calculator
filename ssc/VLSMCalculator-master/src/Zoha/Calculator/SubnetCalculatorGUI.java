package Zoha.Calculator;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SubnetCalculatorGUI extends JFrame {

  
    private JTextField ip1, ip2, ip3, ip4, cidr;
    private JTextField subnetMask, networkAddress, broadcastAddress, networkClass, numSubnets, hostsPerSubnet;

    
    private JTextField superIP1, superIP2;
    private JTextArea superResult;

    public SubnetCalculatorGUI() {
        setTitle("Subnet & Supernet Calculator");
        setSize(520, 430);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Subnetting", createSubnetPanel());
        tabs.add("Supernetting", createSupernetPanel());
        add(tabs);
    }

    
    private JPanel createSubnetPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 230, 230));

        JLabel title = new JLabel("Subnet Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setBounds(100, 10, 300, 40);
        panel.add(title);

        JLabel ipLabel = new JLabel("IP Address");
        ipLabel.setBounds(50, 70, 100, 25);
        panel.add(ipLabel);

        ip1 = new JTextField(3);
        ip2 = new JTextField(3);
        ip3 = new JTextField(3);
        ip4 = new JTextField(3);
        cidr = new JTextField(2);

        ip1.setBounds(150, 70, 40, 25);
        ip2.setBounds(200, 70, 40, 25);
        ip3.setBounds(250, 70, 40, 25);
        ip4.setBounds(300, 70, 40, 25);
        JLabel slash = new JLabel("/");
        slash.setBounds(355, 70, 20, 25);
        panel.add(slash);
        cidr.setBounds(380, 70, 40, 25);

        panel.add(ip1); panel.add(ip2); panel.add(ip3); panel.add(ip4); panel.add(cidr);

        subnetMask = makeField(panel, "Subnet Mask:", 110);
        networkAddress = makeField(panel, "Network Address:", 150);
        broadcastAddress = makeField(panel, "Broadcast Address:", 190);
        networkClass = makeField(panel, "Network Class:", 230);
        numSubnets = makeField(panel, "Number of Subnets:", 270);
        hostsPerSubnet = makeField(panel, "Hosts per Subnet:", 310);

        JButton calc = new JButton("Calculate");
        calc.setBounds(350, 150, 100, 30);
        calc.addActionListener(e -> calculateSubnet());
        panel.add(calc);

        JButton reset = new JButton("Reset");
        reset.setBounds(350, 190, 100, 30);
        reset.addActionListener(e -> resetSubnet());
        panel.add(reset);

        return panel;
    }

    private JTextField makeField(JPanel p, String label, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(50, y, 130, 25);
        p.add(l);
        JTextField f = new JTextField();
        f.setBounds(180, y, 150, 25);
        f.setEditable(false);
        p.add(f);
        return f;
    }

    private void calculateSubnet() {
        try {
            int a = Integer.parseInt(ip1.getText());
            int b = Integer.parseInt(ip2.getText());
            int c = Integer.parseInt(ip3.getText());
            int d = Integer.parseInt(ip4.getText());
            int prefix = Integer.parseInt(cidr.getText());

            if (prefix < 0 || prefix > 32) throw new IllegalArgumentException();

            long ip = ((a << 24) | (b << 16) | (c << 8) | d) & 0xffffffffL;
            long mask = (prefix == 0) ? 0 : (0xffffffffL << (32 - prefix)) & 0xffffffffL;
            long network = ip & mask;
            long broadcast = network | ~mask & 0xffffffffL;

            subnetMask.setText(longToIP(mask));
            networkAddress.setText(longToIP(network));
            broadcastAddress.setText(longToIP(broadcast));
            networkClass.setText((a < 128) ? "A" : (a < 192) ? "B" : "C");

            long hosts = (prefix == 32) ? 1 : (long) Math.pow(2, 32 - prefix) - 2;
            hostsPerSubnet.setText(String.valueOf(hosts));
            numSubnets.setText(String.valueOf((int) Math.pow(2, prefix % 8)));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSubnet() {
        for (JTextField f : new JTextField[]{ip1, ip2, ip3, ip4, cidr,
                subnetMask, networkAddress, broadcastAddress, networkClass, numSubnets, hostsPerSubnet})
            f.setText("");
    }

   
    private JPanel createSupernetPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 230, 230));

        JLabel title = new JLabel("Supernet Calculator", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setBounds(100, 10, 300, 40);
        panel.add(title);

        JLabel ip1Lbl = new JLabel("First Network (e.g. 192.168.0.0/24):");
        ip1Lbl.setBounds(50, 80, 250, 25);
        panel.add(ip1Lbl);
        superIP1 = new JTextField();
        superIP1.setBounds(300, 80, 130, 25);
        panel.add(superIP1);

        JLabel ip2Lbl = new JLabel("Second Network:");
        ip2Lbl.setBounds(50, 120, 250, 25);
        panel.add(ip2Lbl);
        superIP2 = new JTextField();
        superIP2.setBounds(300, 120, 130, 25);
        panel.add(superIP2);

        JButton calc = new JButton("Summarize");
        calc.setBounds(180, 160, 140, 30);
        calc.addActionListener(e -> calculateSupernet());
        panel.add(calc);

        superResult = new JTextArea();
        superResult.setEditable(false);
        JScrollPane scroll = new JScrollPane(superResult);
        scroll.setBounds(50, 210, 380, 120);
        panel.add(scroll);

        return panel;
    }

    private void calculateSupernet() {
        try {
            String net1 = superIP1.getText().trim();
            String net2 = superIP2.getText().trim();
            if (!net1.contains("/") || !net2.contains("/"))
                throw new IllegalArgumentException("CIDR missing");

            long[] a = parseCIDR(net1);
            long[] b = parseCIDR(net2);

            long ip1 = a[0], ip2 = b[0];
            int p1 = (int) a[1], p2 = (int) b[1];

            long minIP = Math.min(ip1, ip2);
            long maxIP = Math.max(ip1 | (0xffffffffL >>> p1), ip2 | (0xffffffffL >>> p2));

            int prefix = 0;
            while (prefix < 32 && ((minIP >> (31 - prefix)) & 1) == ((maxIP >> (31 - prefix)) & 1))
                prefix++;

            long mask = (prefix == 0) ? 0 : (0xffffffffL << (32 - prefix)) & 0xffffffffL;
            long network = minIP & mask;
            long broadcast = network | ~mask & 0xffffffffL;

            superResult.setText("""
                    Supernet Summary:
                    -----------------------
                    Combined Network: %s/%d
                    Network Address:  %s
                    Broadcast:        %s
                    Subnet Mask:      %s
                    """.formatted(longToIP(network), prefix,
                    longToIP(network), longToIP(broadcast), longToIP(mask)));

        } catch (Exception ex) {
            superResult.setText("❌ Invalid input! Example: 192.168.1.0/24");
        }
    }

    private long[] parseCIDR(String cidr) {
        String[] parts = cidr.split("/");
        String[] bytes = parts[0].split("\\.");
        long ip = 0;
        for (String b : bytes) ip = (ip << 8) + Integer.parseInt(b);
        return new long[]{ip, Integer.parseInt(parts[1])};
    }

    
    private String longToIP(long ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xff,
                (ip >> 16) & 0xff,
                (ip >> 8) & 0xff,
                ip & 0xff);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubnetCalculatorGUI().setVisible(true));
    }
}
