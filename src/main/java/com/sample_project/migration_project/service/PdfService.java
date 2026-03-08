package com.sample_project.migration_project.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.sample_project.migration_project.model.Migrant;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateMigrantCard(Migrant migrant) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("Migrant Identity Card", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Blank line
        document.add(new Paragraph("Name: " + migrant.getFullName()));
        document.add(new Paragraph("Government ID: " + migrant.getGovernmentId()));
        document.add(new Paragraph("Origin State: " + migrant.getOriginState()));
        document.add(new Paragraph("Destination State: " + migrant.getDestinationState()));
        document.add(new Paragraph("Status: " + migrant.getStatus()));

        document.close();
        return out.toByteArray();
    }
}