/**
 * Copyright Copyright 2010-17 Simon Andrews
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
package cn.giandata.FastQC.Report;

import cn.giandata.FastQC.FastQCApplication;
import cn.giandata.FastQC.FastQCConfig;
import cn.giandata.FastQC.Modules.QCModule;
import cn.giandata.FastQC.Sequence.SequenceFile;
import cn.giandata.FastQC.Utilities.ImageToBase64;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLReportArchive {
    private XMLStreamWriter xhtml;
    private StringBuffer data = new StringBuffer();
    private QCModule[] modules;
    private SequenceFile sequenceFile;
    private File file;

    public HTMLReportArchive(SequenceFile sequenceFile, QCModule[] modules, String htmlFile, String filename) throws IOException, XMLStreamException {
        this.sequenceFile = sequenceFile;
        this.modules = modules;
        this.file = new File(htmlFile);
        StringWriter htmlStr = new StringWriter();
        XMLOutputFactory xmlfactory = XMLOutputFactory.newInstance();
        this.xhtml = xmlfactory.createXMLStreamWriter(htmlStr);
        Files.createDirectories(Paths.get(file.getPath()));
        startDocument();
        for (int m = 0; m < modules.length; m++) {

            if (modules[m].ignoreInReport()) continue;

            xhtml.writeStartElement("div");
            xhtml.writeAttribute("class", "module");
            xhtml.writeStartElement("h2");
            xhtml.writeAttribute("id", "M" + m);


            // Add an icon before the module name
            if (modules[m].raisesError()) {
                xhtml.writeEmptyElement("img");
                xhtml.writeAttribute("src", base64ForIcon("Icons/error.png"));
                xhtml.writeAttribute("alt", "[FAIL]");
            } else if (modules[m].raisesWarning()) {
                xhtml.writeEmptyElement("img");
                xhtml.writeAttribute("src", base64ForIcon("Icons/warning.png"));
                xhtml.writeAttribute("alt", "[WARN]");
            } else {
                xhtml.writeEmptyElement("img");
                xhtml.writeAttribute("src", base64ForIcon("Icons/tick.png"));
                xhtml.writeAttribute("alt", "[OK]");
            }


            xhtml.writeCharacters(modules[m].name());
//            data.append(">>");
//            data.append(modules[m].name());
//            data.append("\t");
//            if (modules[m].raisesError()) {
//                data.append("fail");
//            } else if (modules[m].raisesWarning()) {
//                data.append("warn");
//            } else {
//                data.append("pass");
//            }
//            data.append("\n");
            xhtml.writeEndElement();
            modules[m].makeReport(this);
            //data.append(">>END_MODULE\n");

            xhtml.writeEndElement();
        }
        closeDocument();
        BufferedWriter rp = new BufferedWriter(new FileWriter(file.getParent() + "/" + folderName() + "/" + filename + ".html"));
        xhtml.flush();
        xhtml.close();
        rp.write(htmlStr.toString());
        rp.close();
        BufferedWriter fdt = new BufferedWriter(new FileWriter(file.getParent() + "/" + folderName() + "/" + filename + ".txt"));
        fdt.write(data.toString());
        fdt.close();
        //XSL-FO
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(false);
            InputStream rsrc = ClassLoader.getSystemResource("cn/giandata/FastQC/Resources/Templates/fastqc2fo.xsl").openStream();
            if (rsrc != null) {
                domFactory.setNamespaceAware(true);
                rsrc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public XMLStreamWriter xhtmlStream() {
        return this.xhtml;
    }

    public StringBuffer dataDocument() {
        return data;
    }

    public String folderName() {
        return file.getName();
    }


    private void startDocument() throws IOException, XMLStreamException {

        // Just put the fastQC version at the start of the text report
        data.append("##FastQC\t");
        data.append(FastQCApplication.VERSION);
        data.append("\n");


        xhtml.writeStartElement("html");
        xhtml.writeStartElement("head");

        xhtml.writeStartElement("title");
        xhtml.writeCharacters(sequenceFile.name());
        xhtml.writeCharacters(" FastQC Report");
        xhtml.writeEndElement();//title

        InputStream rsrc = ClassLoader.getSystemResource("cn/giandata/FastQC/Resources/Templates/header_template.html").openStream();
        if (rsrc != null) {
            xhtml.writeStartElement("style");
            xhtml.writeAttribute("type", "text/css");

            byte[] array = new byte[128];
            int nRead;
            while ((nRead = rsrc.read(array)) != -1) {
                xhtml.writeCharacters(new String(array, 0, nRead));
            }
            rsrc.close();
            xhtml.writeEndElement();//style
        }


        xhtml.writeEndElement();//head

        xhtml.writeStartElement("body");

        xhtml.writeStartElement("div");
        xhtml.writeAttribute("class", "header");

        xhtml.writeStartElement("div");
        xhtml.writeAttribute("id", "header_title");

        xhtml.writeEmptyElement("img");
        xhtml.writeAttribute("src", base64ForIcon("Icons/fastqc_icon.png"));
        xhtml.writeAttribute("alt", "cn/giandata/FastQC");
        xhtml.writeCharacters("Data Quality Report");
        xhtml.writeEndElement();//div

        xhtml.writeEndElement();//div


//        xhtml.writeStartElement("div");
//        xhtml.writeAttribute("class", "summary");
//
//        xhtml.writeStartElement("h2");
//        xhtml.writeCharacters("Summary");
//        xhtml.writeEndElement();//h2


//        xhtml.writeStartElement("ul");
//
//        StringBuilder summaryText = new StringBuilder();
//
//        for (int m = 0; m < modules.length; m++) {
//
//            if (modules[m].ignoreInReport()) {
//                continue;
//            }
//            xhtml.writeStartElement("li");
//            xhtml.writeEmptyElement("img");
//            if (modules[m].raisesError()) {
//                xhtml.writeAttribute("src", base64ForIcon("Icons/error.png"));
//                xhtml.writeAttribute("alt", "[FAIL]");
//                summaryText.append("FAIL");
//            } else if (modules[m].raisesWarning()) {
//                xhtml.writeAttribute("src", base64ForIcon("Icons/warning.png"));
//                xhtml.writeAttribute("alt", "[WARNING]");
//                summaryText.append("WARN");
//            } else {
//                xhtml.writeAttribute("src", base64ForIcon("Icons/tick.png"));
//                xhtml.writeAttribute("alt", "[PASS]");
//                summaryText.append("PASS");
//            }
//            summaryText.append("\t");
//            summaryText.append(modules[m].name());
//            summaryText.append("\t");
//            summaryText.append(sequenceFile.name());
//            summaryText.append(FastQCConfig.getInstance().lineSeparator);
//
//            xhtml.writeStartElement("a");
//            xhtml.writeAttribute("href", "#M" + m);
//            xhtml.writeCharacters(modules[m].name());
//            xhtml.writeEndElement();//a
//            xhtml.writeEndElement();//li
//
//
//        }
//        xhtml.writeEndElement();//ul
//        xhtml.writeEndElement();//div

        xhtml.writeStartElement("div");
        xhtml.writeAttribute("class", "main");
//        Path paths = Paths.get(file.getParent() + "/" + folderName() + "/summary.txt");
//        if (Files.exists(paths)) {
//            Files.delete(paths);
//        }
//        Files.createFile(paths);
//        BufferedWriter rp = new BufferedWriter(new FileWriter(file.getParent() + "/" + folderName() + "/summary.txt"));
//        rp.write(summaryText.toString());
//        rp.close();

    }

    private String base64ForIcon(String path) {
        try {
            BufferedImage b = ImageIO.read(ClassLoader.getSystemResource("cn/giandata/FastQC/Resources/Templates/" + path));
            return (ImageToBase64.imageToBase64(b));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "Failed";
        }
    }

    private void closeDocument() throws XMLStreamException {
        xhtml.writeEndElement();//div
        xhtml.writeStartElement("div");
        xhtml.writeAttribute("class", "footer");
        xhtml.writeCharacters("Produced by ");
        xhtml.writeStartElement("a");
        xhtml.writeAttribute("href", "https://gene.giandata.cn/");
        xhtml.writeCharacters("cn/giandata/FastQC");
        xhtml.writeEndElement();//a
        xhtml.writeCharacters("  (version " + FastQCApplication.VERSION + ")");
        xhtml.writeEndElement();//div

        xhtml.writeEndElement();//body
        xhtml.writeEndElement();//html
    }


}
