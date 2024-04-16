import java.io.*;
import java.util.ArrayList;

public class Store {
    private String name; // name of store
    private ArrayList<Product> products; // arraylist of all products in store
    private String fileName; // name of file

    public Store(String name, String sellerName) {
        this.name = name;
        this.products = new ArrayList<>();
        this.fileName = "store_" + name + ".txt";
        this.toFile();
        // Writes to sellerName_totalStores.txt when a new store created
        String historyFile = sellerName + "_totalStores.txt";
        File file = new File(historyFile);
        // Checks if store already existed in the history file
        boolean existed = false;
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                // reading file to check if there is a store name that matches the store name user inputs
                String storeName;
                while ((storeName = reader.readLine()) != null) {
                    if (storeName.equals(name)) {
                        existed = true;
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        // if it does not exist
        if (!existed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                // write new name to file
                writer.write(name);
                writer.newLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        // update totalStores.txt
        File totalStores = new File("totalStores.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(totalStores, true))) {
            writer.write(name);
            writer.newLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Store(String name) {
        this.name = name;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.deleteFile(); // Delete the previous txt file.
        this.name = name; // Set the fields.
        this.fileName = "store_" + name + ".txt";
        this.toFile(); // Generate the new txt file.
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.deleteFile();
        this.products = products;
        this.toFile();
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Adds product to store.
     *
     * @param name name of store
     * @param description product description
     * @param quantity quantity of product
     * @param price price of product
     * @param recreate boolean to add product or not
     */
    public void addProduct(String name, String description, int quantity, double price, boolean recreate) {
        this.deleteFile();
        products.add(new Product(name, this.name, description, quantity, price));
        this.toFile();
        if (!recreate) {
            // Create file for customer class.
            // Write to totalProducts.txt
            File totalProducts = new File("totalProducts.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(totalProducts, true))) {
                writer.write(name);
                writer.newLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // Write to totalProductsByStoreAndPrice.txt
            File totalProductsByStoreAndPrice = new File("totalProductsByStoreAndPrice.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(totalProductsByStoreAndPrice,
                    true))) {
                writer.write(String.format("%s by %s with %.2f, Available: %d", name, this.name, price, quantity));
                writer.newLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Modifies the product in store.
     *
     * @param productName name of product
     * @param description product description
     * @param quantity quantity of product
     * @param price price of product
     * @return boolean modified to modify product in store
     */
    public boolean modifyProduct(String productName, String description, int quantity, double price) {
        // Retrieve previous description, quantity, price.
        String descriptionOld = "";
        int quantityOld = -1;
        double priceOld = -1;
        // Modify the object.
        boolean modified = true;
        this.deleteFile();
        boolean productNotExist = true;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                productNotExist = false;
                descriptionOld = product.getDescription();
                quantityOld = product.getQuantity();
                priceOld = product.getPrice();
                product.setDescription(description);
                product.setQuantity(quantity);
                product.setPrice(price);
            }
        }
        // if the product does not exist
        if (productNotExist) {
            System.out.println("Failed to edit product. Product does not exist!");
            modified = false;
        } else {
            // Modify totalProductsByStoreAndPrice.txt
            File file = new File("totalProductsByStoreAndPrice.txt");
            String target = String.format("%s by %s with %.2f, Available: %d", productName, this.name, priceOld,
                    quantityOld);
            StringBuilder builder = new StringBuilder();
            // reading the file and displaying modified product in a string format
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(target)) {
                        builder.append(String.format("%s by %s with %.2f, Available: %d", productName, this.name,
                                price, quantity));
                    } else {
                        builder.append(line);
                    }
                    builder.append('\n');
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // writes the modified product
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(builder.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        this.toFile();
        return modified;
    }

    /**
     * Deletes the product from store
     *
     * @param productName name of product
     * @return boolean deleted to delete product from store
     */
    public boolean deleteProduct(String productName) {
        boolean deleted = true;
        this.deleteFile();
        int previous = products.size();
        // checks if the product name user inputs matches the name of product in store
        products.removeIf(product -> product.getName().equals(productName));
        if (previous == products.size()) {
            // displays error if product user inputs does not exist
            System.out.println("Failed to delete product. Product does not exist!");
            deleted = false;
        } else {
            // Modify totalProductsByStoreAndPrice.txt.
            File file = new File("totalProductsByStoreAndPrice.txt");
            String target = String.format("%s by %s", productName, this.name);
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lineElements = line.split(" with ");
                    if (!lineElements[0].equals(target)) {
                        builder.append(line);
                        builder.append('\n');
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(builder.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // Update totalProducts.txt.
            File totalProducts = new File("totalProducts.txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(productName)) {
                        builder.append(line);
                        builder.append('\n');
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(builder.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        this.toFile();
        return deleted;
    }

    /**
     * Method that is used for toString format in modify and delete product.
     *
     * @return returns StringBuilder in toString format that is used for modifying and deleting product.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        for (Product product : products) {
            builder.append(product.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * File method that writes to fileName.
     */
    public void toFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(this.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Deletes original file.
     */
    private void deleteFile() {
        File original = new File(fileName);
        original.delete();
    }

    /**
     * Determines whether the object given as a parameter is equal to this store.
     *
     * @param obj another object
     * @return whether the object given as a parameter is equal to this store
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Store) &&
                ((Store) obj).getName().equals(name);
    }

    /**
     * Checks if there is any products in the store.
     *
     * @return true if there is a product, false otherwise.
     */
    public boolean isEmpty() {
        return products.isEmpty();
    }
}
