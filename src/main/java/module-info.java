module com.pixel.javafxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;


    opens com.pixel.javafxdemo to javafx.fxml, org.hibernate.orm.core;
    exports com.pixel.javafxdemo;
    exports com.pixel.javafxdemo.entity;
    exports com.pixel.javafxdemo.util;
}