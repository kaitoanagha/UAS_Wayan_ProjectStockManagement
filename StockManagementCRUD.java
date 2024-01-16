/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.stockmanagementcrud;

/**
 *
 * @author wayan
 */



import java.sql.*;
import java.util.Scanner;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.util.Scanner;

import java.sql.Connection;

import java.sql.SQLException;

public class StockManagementCRUD {

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/inventory";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Create table if not exists
            createTable(connection);

            // Menu untuk CRUD
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("1. Tambah Produk");
                System.out.println("2. Tampilkan Produk");
                System.out.println("3. Update Produk");
                System.out.println("4. Hapus Produk");
                System.out.println("0. Keluar");
                System.out.print("Pilih menu: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> createProduct(connection);
                    case 2 -> readProducts(connection);
                    case 3 -> updateProduct(connection);
                    case 4 -> deleteProduct(connection);
                }
            } while (choice != 0);

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "quantity INT," +
                "price DOUBLE)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createProduct(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama produk: ");
        String productName = scanner.nextLine();
        System.out.print("Masukkan jumlah produk: ");
        int productQuantity = scanner.nextInt();
        System.out.print("Masukkan harga produk: ");
        double productPrice = scanner.nextDouble();

        String insertSQL = "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, productQuantity);
            preparedStatement.setDouble(3, productPrice);
            preparedStatement.executeUpdate();
            System.out.println("Produk berhasil ditambahkan.");
        }
    }

    private static void readProducts(Connection connection) throws SQLException {
        String selectSQL = "SELECT * FROM products";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectSQL);

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Nama Produk: " + resultSet.getString("name") +
                        ", Jumlah: " + resultSet.getInt("quantity") +
                        ", Harga: " + resultSet.getDouble("price"));
            }
        }
    }

    private static void updateProduct(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID produk yang akan diupdate: ");
        int productId = scanner.nextInt();

        String selectSQL = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.print("Masukkan jumlah produk baru: ");
                int newQuantity = scanner.nextInt();
                System.out.print("Masukkan harga produk baru: ");
                double newPrice = scanner.nextDouble();

                String updateSQL = "UPDATE products SET quantity = ?, price = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                    updateStatement.setInt(1, newQuantity);
                    updateStatement.setDouble(2, newPrice);
                    updateStatement.setInt(3, productId);
                    updateStatement.executeUpdate();
                    System.out.println("Produk berhasil diupdate.");
                }
            } else {
                System.out.println("Produk dengan ID tersebut tidak ditemukan.");
            }
        }
    }

    private static void deleteProduct(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID produk yang akan dihapus: ");
        int productId = scanner.nextInt();

        String deleteSQL = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Produk berhasil dihapus.");
            } else {
                System.out.println("Produk dengan ID tersebut tidak ditemukan.");
            }
        }
    }
}

    

