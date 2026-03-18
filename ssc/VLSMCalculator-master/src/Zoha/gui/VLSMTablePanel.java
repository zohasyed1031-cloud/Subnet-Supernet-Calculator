package Zoha.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import Zoha.Calculator.VLSMReport;
import Zoha.Controller.LogUpdate;


public class VLSMTablePanel extends JPanel {

    private JTable table;

    private JPanel logPanel;
    private JLabel logTextLable;
    private VLSMReportTableModel tableModel;

    public JTableHeader getTableHeader(){
        return table.getTableHeader();
    }

    public VLSMTablePanel(){


        tableModel = new VLSMReportTableModel();
        logPanel   = new JPanel();
        logTextLable = new JLabel();

        /* Log panel at the bottom */
        logPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        logPanel.setBackground(Color.WHITE);
        Border outerBorder = BorderFactory.createLoweredBevelBorder();
        Border innerBorder = BorderFactory.createEmptyBorder(14, 14, 14, 14);
        logPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        logPanel.add(logTextLable);


        table = new JTable(tableModel);
        table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font(table.getFont().getName(), Font.PLAIN, 13));

        setLayout(new BorderLayout());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        add(table, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);
    }


    public void setData(VLSMReport report){
        this.tableModel.setData(report.getReportSubnets());
    }

    public void saveCSVFile (File file){
        tableModel.saveCSVFile(file);
    }


    public void setLog(LogUpdate log){
        log.updateLogLabel(logTextLable);
        logTextLable.revalidate();
    }

    public void refresh(){

        resizeColumnWidth(table);
        tableModel.fireTableDataChanged();
        table.revalidate();

    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}
