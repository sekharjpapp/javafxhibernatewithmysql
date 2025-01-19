package com.pixel.javafxdemo;


import com.pixel.javafxdemo.entity.Product;
import com.pixel.javafxdemo.util.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;

import java.util.List;

public class MainController {

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    public void initialize() {
        // Configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Load initial product list
        loadProducts();
    }

    @FXML
    private void addProduct() {
        String name = productName.getText();
        String priceText = productPrice.getText();

        // Input validation
        if (name == null || name.trim().isEmpty()) {
            showAlert("Validation Error", "Product name cannot be empty!");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                showAlert("Validation Error", "Price cannot be negative!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid price. Please enter a valid number.");
            return;
        }

        // Create a new product
        Product product = new Product(name, price);

        // Save the product to the database
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            showAlert("Database Error", "Failed to save the product: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Reload the product table and clear input fields
        loadProducts();
        productName.clear();
        productPrice.clear();
        showAlert("Success", "Product added successfully!");
    }

    private void loadProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("from Product", Product.class).list();
            productTable.getItems().setAll(products);
        } catch (Exception e) {
            showAlert("Database Error", "Failed to load products: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}




