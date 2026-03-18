package Zoha.Calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Subnetting and Supernetting Calculator ===");
        System.out.print("Enter the base IP (e.g. 192.168.1.0): ");
        String ip = sc.nextLine();

        System.out.print("Enter the subnet mask (CIDR notation, e.g. 24): ");
        int prefix = sc.nextInt();

        try {
         
            String[] ipParts = ip.split("\\.");
            if (ipParts.length != 4) {
                throw new InvalidIPv4Exception("Invalid IP address format!");
            }

            int mask = 0xffffffff << (32 - prefix);
            int ipNum = 0;
            for (String part : ipParts) {
                ipNum = (ipNum << 8) | Integer.parseInt(part);
            }

            int network = ipNum & mask;
            int broadcast = network | ~mask;

            System.out.println("\n--- Calculation Result ---");
            System.out.println("Network Address: " + intToIp(network));
            System.out.println("Broadcast Address: " + intToIp(broadcast));
            System.out.println("Number of Hosts: " + (int) (Math.pow(2, 32 - prefix) - 2));
            System.out.println("---------------------------");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }

    private static String intToIp(int ip) {
        return String.format("%d.%d.%d.%d",
                (ip >>> 24) & 0xFF,
                (ip >>> 16) & 0xFF,
                (ip >>> 8) & 0xFF,
                ip & 0xFF);
    }
}
