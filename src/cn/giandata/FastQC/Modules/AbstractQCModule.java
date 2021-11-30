/**
 * Copyright Copyright 2010-12 Pierre Lindenbaum
 * <p>
 * This file is part of FastQC.
 * <p>
 * FastQC is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * FastQC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with FastQC; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package cn.giandata.FastQC.Modules;

import cn.giandata.FastQC.Report.HTMLReportArchive;
import cn.giandata.FastQC.Utilities.ImageToBase64;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class AbstractQCModule implements QCModule {

    protected void simpleXhtmlReport(HTMLReportArchive report, BufferedImage img, String alt) throws XMLStreamException {
        XMLStreamWriter xhtml = report.xhtmlStream();
        xhtml.writeStartElement("p");
        xhtml.writeEmptyElement("img");
        xhtml.writeAttribute("class", "indented");
        xhtml.writeAttribute("src", ImageToBase64.imageToBase64(img));
        xhtml.writeAttribute("alt", alt);

        if (img != null) {
            xhtml.writeAttribute("width", String.valueOf(img.getWidth()));
            xhtml.writeAttribute("height", String.valueOf(img.getHeight()));
        }

        xhtml.writeEndElement();//p
    }

    protected void writeDefaultImage(HTMLReportArchive report, String fileName, String imageTitle, int width, int height) throws IOException, XMLStreamException {
        //OutputStream zip = new FileOutputStream(report.folderName()+"/Images/"+fileName);
        //zip.putNextEntry(new ZipEntry(report.folderName()+"/Images/"+fileName));
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = b.createGraphics();

        JPanel resultsPanel = getResultsPanel();
        resultsPanel.setDoubleBuffered(false);
        resultsPanel.setSize(width, height);
        resultsPanel.addNotify();
        resultsPanel.validate();

        resultsPanel.print(g);

        g.dispose();

        //ImageIO.write(b, "PNG", zip);
        //zip.closeEntry();

        simpleXhtmlReport(report, b, imageTitle);

    }

    protected void writeSpecificImage(HTMLReportArchive report, JPanel resultsPanel, String fileName, String imageTitle, int width, int height) throws IOException, XMLStreamException {
//		ZipOutputStream zip = report.zipFile();
//		zip.putNextEntry(new ZipEntry(report.folderName()+"/Images/"+fileName));
        //OutputStream zip = new FileOutputStream(report.folderName()+"/Images/"+fileName);
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = b.createGraphics();

        resultsPanel.setDoubleBuffered(false);
        resultsPanel.setSize(width, height);
        resultsPanel.addNotify();
        resultsPanel.validate();

        resultsPanel.print(g);

        g.dispose();

        //ImageIO.write(b, "PNG", zip);
//		zip.closeEntry();

        simpleXhtmlReport(report, b, imageTitle);

    }


    protected void writeTable(HTMLReportArchive report, TableModel table,String s1) throws IOException, XMLStreamException {
        writeXhtmlTable(report, table,s1);
        writeTextTable(report, table);
    }

    protected void writeXhtmlTable(HTMLReportArchive report, TableModel table,String s1) throws IOException, XMLStreamException {
        XMLStreamWriter w = report.xhtmlStream();
        w.writeStartElement("table");
        if (s1.equals("s1")) {
            w.writeAttribute("id", s1);
        }
        w.writeStartElement("thead");
        w.writeStartElement("tr");

        for (int c = 0; c < table.getColumnCount(); c++) {
            w.writeStartElement("th");
            w.writeCharacters(table.getColumnName(c));
            w.writeEndElement();
        }

        w.writeEndElement();//tr
        w.writeEndElement();//thead
        w.writeStartElement("tbody");

        for (int r = 0; r < table.getRowCount(); r++) {
            w.writeStartElement("tr");
            for (int c = 0; c < table.getColumnCount(); c++) {
                w.writeStartElement("td");
                w.writeCharacters(String.valueOf(table.getValueAt(r, c)));
                w.writeEndElement();//td
            }
            w.writeEndElement();//tr
        }
        w.writeEndElement();//tbody
        w.writeEndElement();
    }


    protected void writeTextTable(HTMLReportArchive report, TableModel table) throws IOException {
        StringBuffer d = report.dataDocument();
        d.append("#");

        for (int c = 0; c < table.getColumnCount(); c++) {
            if (c != 0) d.append("\t");
            d.append(table.getColumnName(c));
        }

        d.append("\n");

        // Do the rows
        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                if (c != 0) d.append("\t");
                d.append(table.getValueAt(r, c));
            }
            d.append("\n");
        }

    }

}
