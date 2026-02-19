module es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.prefs;
    requires eu.hansolo.tilesfx;
    requires javafx.web;
    requires flexmark;
    requires flexmark.util.ast;
    requires PDFViewerFX;

    opens es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro to javafx.fxml;
    opens es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.model;
    opens es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.persistence;
    opens es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.view;
    opens es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro.util;
    exports es.damdi.alvaro.comp_despl_p01_padressapp_martinezfloresalvaro;
}