import java.io.*;

public class Product {
    private String name; // name of product
    private String storeName; // name of store
    private String description; // product description
    private int quantity; // quantity of product
    private double price; // price of product
    private String fileName; // file name

    public Product(String name, String storeName, String description, int quantity, double price) {
        this.name = name;
        this.storeName = storeName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.fileName = storeName + "_" + name + ".txt";
        this.toFile();
        // Write to a history file contains all products sold in the store.
        String historyFile = storeName + "_totalProducts.txt";
        File file = new File(historyFile);
        // Check if product already existed in the history file.
        boolean existed = false;
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String productName;
                while ((productName = reader.readLine()) != null) {
                    if (productName.equals(name)) {
                        existed = true;
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (!existed) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(name);
                writer.newLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Deletes previous text file and generates a new text file for product name.
     *
     * @param name name of product
     */
    public void setName(String name) {
        this.deleteFile();
        this.name = name;
        this.fileName = storeName + "_" + name + ".txt";
        this.toFile();
    }

    public String getStoreName() {
        return storeName;
    }

    /**
     * Deletes previous text file and generates a new text file for name of store.
     *
     * @param storeName name of store
     */
    public void setStoreName(String storeName) {
        this.deleteFile();
        this.storeName = storeName;
        this.fileName = "product_" + storeName + "_" + name + ".txt";
        this.toFile();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.deleteFile();
        this.description = description;
        this.toFile();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.deleteFile();
        this.quantity = quantity;
        this.toFile();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.deleteFile();
        this.price = price;
        this.toFile();
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Determines whether the object given as a parameter is equal to this product. true is returned if the
     * specified object is an instance of Product and the name of product, store name, description, quantity, and price
     * are equal to the product.
     *
     * @param obj Another Object.
     * @return Whether the object given as a parameter is equal to this product.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Product) &&
                ((Product) obj).getName().equals(name) &&
                ((Product) obj).getStoreName().equals(storeName) &&
                ((Product) obj).getDescription().equals(description) &&
                ((Product) obj).getQuantity() == quantity &&
                ((Product) obj).getPrice() == price;
    }

    /**
     * @return a string representation of the product, with ", " separating each field.
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %d, %.2f", name, storeName, description, quantity, price);
    }

    /**
     * Create a txt file for the product.
     */
    public void toFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(name);
            writer.newLine();
            writer.write(storeName);
            writer.newLine();
            writer.write(description);
            writer.newLine();
            writer.write(String.valueOf(quantity));
            writer.newLine();
            writer.write(String.valueOf(price));
            writer.newLine();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Delete the original txt file for the product.
     */
    public void deleteFile() {
        File original = new File(fileName);
        original.delete();
    }
}
