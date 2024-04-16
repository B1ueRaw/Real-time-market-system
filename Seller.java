import java.util.*;
import java.io.*;

public class Seller {
    private String name; // name of seller
    private ArrayList<Store> stores; // arraylist of all stores seller owns
    private String fileName; // name of file
    private int finalQuantity; // final quantity in int

    public Seller(String name) {
        this.name = name;
        this.stores = new ArrayList<>();
        this.fileName = "seller_" + name + ".txt";

        // Check if the seller has logged in before (account exist), retrieve info and create objects.
        boolean existed = false;
        File sellerFile = new File("seller_" + name + ".txt");
        existed = sellerFile.exists() && !sellerFile.isDirectory();
        // If the seller exists, create a file that has the seller's name as part of txt file name
        if (existed) {
            try (BufferedReader sellerReader = new BufferedReader(new FileReader(sellerFile))) {
                sellerReader.readLine(); // first line of the file is seller name
                String storeName;
                // reading and formatting seller_name.txt file
                while ((storeName = sellerReader.readLine()) != null) {
                    File storeFile = new File("store_" + storeName + ".txt");
                    BufferedReader storeReader = new BufferedReader(new FileReader(storeFile));
                    storeReader.readLine(); // first line is store name
                    String productInfo;
                    while ((productInfo = storeReader.readLine()) != null) {
                        String[] info = productInfo.split(", ");
                        int quantity = Integer.parseInt(info[3]); // parsing quantity from string to int
                        double price = Double.parseDouble(info[4]); // parsing price from price to int
                        createProduct(info[0], info[1], info[2], quantity, price, true);
                    }
                    storeReader.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else { // seller not exist, add to totalSellers.txt
            File totalSellers = new File("totalSellers.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(totalSellers))) {
                writer.write(name);
                writer.newLine();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        this.toFile();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.deleteFile(); // Delete the previous txt file.
        this.name = name; // Set the fields.
        this.fileName = "seller_" + name + ".txt";
        this.toFile(); // Generate the new txt file.
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.deleteFile();
        this.stores = stores;
        this.toFile();
    }

    public String getFileName() {
        return fileName;
    }

    public int getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(int finalQuantity) {
        this.deleteFile();
        this.finalQuantity = finalQuantity;
        this.toFile();
    }

    /**
     * Adds the store.
     */
    public void addStore(String storeName) {
        this.deleteFile();
        stores.add(new Store(storeName, name));
        this.toFile();
    }

    /**
     * Removes the store.
     */
    public void deleteStore(String storeName) {
        this.deleteFile();
        stores.removeIf(store -> store.getName().equals(storeName));
        this.toFile();
    }

    /**
     * Modifies the store.
     */
    public void modifyStore(String oldStoreName, String newStoreName) {
        this.deleteFile();
        // checks if store name equals any of the previous store name
        for (Store store : stores) {
            if (store.getName().equals(oldStoreName)) {
                store.setName(newStoreName);
            }
        }
        this.toFile();
    }

    /**
     * Checks if the store equals each store present in arraylist of all stores
     *
     * @return returns result of all the products in the store
     */
    public ArrayList<Product> getProducts() {
        ArrayList<Product> result = new ArrayList<>();
        for (Store store : stores) {
            result.addAll(store.getProducts());
        }
        return result;
    }

    /**
     * Creates new product for the seller's store.
     *
     * @param productName name of product
     * @param storeName   name of store
     * @param description description of product
     * @param quantity    quantity seller is selling for product
     * @param price       price per product
     * @param recreate    boolean to create a new product
     */
    public void createProduct(String productName, String storeName, String description, int quantity, double price,
                              boolean recreate) {
        deleteFile();
        boolean storeExist = false;
        for (Store store : stores) {
            // if store name user inputs is the same as a store in file, add the product information
            if (store.getName().equals(storeName)) {
                storeExist = true;
                store.addProduct(productName, description, quantity, price, recreate);
            }
        }
        // if the store does not exist
        if (!storeExist) {
            addStore(storeName);
            stores.get(stores.size() - 1).addProduct(productName, description, quantity, price, recreate);
        }
        toFile();
    }

    /**
     * Views all the sales the seller has made.
     *
     * @param storeName name of store
     * @return returns result in a String format
     */
    public String viewSales(String storeName) {
        StringBuilder result = new StringBuilder();
        // Check if the seller has opened a store.
        File totalStores = new File(name + "_totalStores.txt");
        if (totalStores.exists()) {
            // Check if the specified store exists (no matter currently or in the past)
            boolean storeExist = false;
            try (BufferedReader reader = new BufferedReader(new FileReader(totalStores))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(storeName)) {
                        storeExist = true;
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if (!storeExist) {
            } else { // store with storeName exists.
                // Get a list of all products that were sold in this store.
                File totalProducts = new File(storeName + "_totalProducts.txt");
                if (totalProducts.exists()) {
                    try (BufferedReader reader1 = new BufferedReader(new FileReader(totalProducts))) {
                        String productName;
                        while ((productName = reader1.readLine()) != null) {
                            File history = new File(storeName + "_" + productName + "_purchase.txt");
                            if (history.exists()) {
                                try (BufferedReader reader2 = new BufferedReader(new FileReader(history))) {
                                    String record;
                                    while ((record = reader2.readLine()) != null) {
                                        String[] recordElements = record.split(", ");
                                        String customerName = recordElements[0];
                                        String quantity = recordElements[1];
                                        String price = recordElements[2];
                                        String revenue = recordElements[3];
                                        result.append(String.format("%s purchased %s %s at %s each. Revenue: %s\n",
                                                customerName, quantity, productName, price, revenue));
                                    }
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            } else {
                                result.append("No history! Nothing sold in this store.\n");
                            }
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    result.append("No history! Nothing sold in this store.\n");
                }
            }
        } else {
            result.append("No history! No store has been created.\n");
        }
        return result.toString(); // return the result as a string
    }

    /**
     * Exports products of stores seller owns.
     *
     * @param path path of what name and where the user wants to export
     * @return boolean to export file or show error if file could not be exported
     */
    public boolean exportCSV(String path) {
        // writes all the products of stores seller owns in the path text file
        try (BufferedWriter writer = new BufferedWriter((new FileWriter(path)))) {
            for (Product product : getProducts()) {
                writer.write(product.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    /**
     * Imports products of stores seller owns.
     *
     * @param path path of what name and where the user wants to import
     * @return boolean to import file or show error if file could not be imported
     */
    public int importCSV(String path) {
        deleteFile();
        // reads all the products of stores seller owns in the path text file
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Retrieve elements of each line.
                // Each line represents a product, with the format same as the toString().
                String[] elements = line.split(", ");
                String nameOne = elements[0];
                String storeName = elements[1];
                String description = elements[2];
                int quantity = Integer.parseInt(elements[3]);
                double price = Double.parseDouble(elements[4]);
                // Add each product to the seller.
                boolean storeExist = false;
                for (Store store : stores) {
                    if (store.getName().equals(storeName)) {
                        storeExist = true;
                        store.addProduct(nameOne, description, quantity, price, false);
                    }
                }
                // when store does not exist
                if (!storeExist) {
                    addStore(storeName);
                    stores.get(stores.size() - 1).addProduct(nameOne, description, quantity, price, false);
                }
            }
            return 1;
            // invalid format when trying to import
        } catch (NumberFormatException formatException) {
            formatException.printStackTrace();
            return -1;
        } catch (IOException ioException) {
            // invalid file name or unable to find to file
            ioException.printStackTrace();
            return -2;
        } finally {
            toFile();
        }
    }

    /**
     * Creates dashboard of statistics for each of the store seller owns.
     *
     * @param reverseOrder different types of sorting options in dashboard
     * @return boolean to export file or show error if file could not be exported
     */
    public String createDashboard(boolean reverseOrder) {
        StringBuilder result = new StringBuilder();
        File totalStores = new File(name + "_totalStores.txt");
        // if there is no store existing
        if (!totalStores.exists()) {
            result.append("No history! No store exists.");
        } else { // At least one store belongs to the seller.
            try (BufferedReader sellerReader = new BufferedReader(new FileReader(totalStores))) {
                String storeName;
                while ((storeName = sellerReader.readLine()) != null) {
                    // Get a list of all products that were sold in this store.
                    File totalProducts = new File(storeName + "_totalProducts.txt");
                    if (!totalProducts.exists()) {
                        result.append("No products has been sold in the store: ").append(storeName);
                    } else {
                        // A list of customers with the number of items that they purchased in this store.
                        HashMap<String, Integer> customers = new HashMap<String, Integer>();
                        // A list of products with the number of sales in this store.
                        Map<String, Integer> products = new HashMap<String, Integer>();
                        BufferedReader storeReader = new BufferedReader(new FileReader(totalProducts));
                        String productName;
                        while ((productName = storeReader.readLine()) != null) {
                            // For each product from a certain store, check if there is a purchase history.
                            File history = new File(storeName + "_" + productName + "_purchase.txt");
                            if (history.exists()) {
                                BufferedReader reader = new BufferedReader(new FileReader(history));
                                String record;
                                while ((record = reader.readLine()) != null) {
                                    String[] recordElements = record.split(", ");
                                    String customerName = recordElements[0];
                                    int quantity = Integer.parseInt(recordElements[1]);
                                    // Put <customerName, quantity of purchase> into the hashmap.
                                    if (customers.containsKey(customerName)) {
                                        customers.put(customerName, customers.get(customerName) + quantity);
                                    } else {
                                        customers.put(customerName, quantity);
                                    }
                                    // Put <product, quantity of sales> into the hashmap.
                                    if (products.containsKey(productName)) {
                                        products.put(productName, products.get(productName) + quantity);
                                    } else {
                                        products.put(productName, quantity);
                                    }
                                }
                                reader.close();
                            }
                        }
                        storeReader.close();
                        // Sort the lists.
                        LinkedHashMap<String, Integer> sortedCustomers = new LinkedHashMap<>();
                        if (!reverseOrder) {
                            customers.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                                    sortedCustomers.put(x.getKey(), x.getValue()));
                        } else {
                            customers.entrySet().stream().sorted(Map.Entry.comparingByValue(
                                    Comparator.reverseOrder())).forEachOrdered(x ->
                                    sortedCustomers.put(x.getKey(), x.getValue()));
                        }
                        LinkedHashMap<String, Integer> sortedProducts = new LinkedHashMap<>();
                        if (!reverseOrder) {
                            products.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                                    sortedProducts.put(x.getKey(), x.getValue()));
                        } else {
                            products.entrySet().stream().sorted(
                                    Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x ->
                                    sortedProducts.put(x.getKey(), x.getValue()));
                        }
                        // Display the lists for each store.
                        result.append("Data for ").append(storeName).append("\n");
                        result.append("Customers by Purchase Quantity" + "\n");
                        for (String key : sortedCustomers.keySet()) {
                            result.append(String.format("%-10s%10d\n", key, sortedCustomers.get(key)));
                        }
                        result.append("Products by Number of Sales" + "\n");
                        for (String key : sortedProducts.keySet()) {
                            result.append(String.format("%-10s%10d\n", key, sortedProducts.get(key)));
                        }
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * Checks if the seller has a store. If seller does, return a String of all storeNames.
     *
     * @return null if the sellers does not have any store, or a String of all storeNames.
     */
    public String haveStore() {
        if (stores.size() == 0) {
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            for (Store store : stores) {
                builder.append(store.getName());
                builder.append('\n');
            }
            return builder.toString();
        }
    }

    public Store getStore(String storeName) {
        for (Store store : stores) {
            if (store.getName().equals(storeName)) {
                return store;
            }
        }
        return null;
    }

    /**
     * Method that is used for toString format in other methods
     *
     * @return returns StringBuilder in toString format that is used for other methods in this class.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        for (Store store : stores) {
            builder.append(store.getName());
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Create a txt file for the seller.
     */
    public void toFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // writes the text file for seller
            writer.write(this.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Deletes the original txt file for the seller.
     */
    private void deleteFile() {
        File original = new File(fileName);
        original.delete();
    }
}
