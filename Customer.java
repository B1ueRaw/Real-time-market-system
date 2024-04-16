import java.io.*;
import java.util.*;

public class Customer {
    private String name; // name of customer

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' file to show the product number count.
     *
     * @return returns 0 for count product number after try catch
     */
    public int countProductNumber() {
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
            }
            return productList.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' file to show the default list.
     *
     * @return returns null after try catch in showing default list in txt file
     */
    public String showDefaultList() {
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();
        StringBuilder tt = new StringBuilder("");
        int i = 0;

        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
                tt.append(i + 1).append(". ").append(productList.get(i)).append("\n");
                i++;
            }
            return tt.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }

    /**
     * Sort all products in market by price.
     *
     * @param reverse a boolean indicating whether to reverse the order of the sort (default from low to high)
     */
    public void sortMarketByPrice(boolean reverse) {
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        HashMap<String, Double> products = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(totalProducts));
            String line;
            while ((line = reader.readLine()) != null) {
                // Retrieve product price as Double for each line.
                String priceString = line.split(" with ")[1].split(", Available: ")[0];
                Double price = Double.parseDouble(priceString);
                products.put(line, price);
            }
            reader.close();
            // Sort the list.
            ArrayList<String> productsSorted = new ArrayList<>();
            if (!reverse) {
                products.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                        productsSorted.add(x.getKey()));
            } else {
                products.entrySet().stream().sorted(
                        Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x ->
                        productsSorted.add(x.getKey()));
            }
            // Rewrite totalProductsByStoreAndPrice.txt.
            BufferedWriter writer = new BufferedWriter(new FileWriter(totalProducts));
            for (String productInfo : productsSorted) {
                writer.write(productInfo);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Sort all products in market by quantity.
     *
     * @param reverse a boolean indicating whether to reverse the order of the sort (default from low to high)
     */
    public void sortMarketByQuantity(boolean reverse) {
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        HashMap<String, Integer> products = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(totalProducts));
            String line;
            while ((line = reader.readLine()) != null) {
                // Retrieve product price as Double for each line.
                String priceString = line.split(", Available: ")[1];
                Integer quantity = Integer.parseInt(priceString);
                products.put(line, quantity);
            }
            reader.close();
            // Sort the list.
            ArrayList<String> productsSorted = new ArrayList<>();
            if (!reverse) {
                products.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                        productsSorted.add(x.getKey()));
            } else {
                products.entrySet().stream().sorted(
                        Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x ->
                        productsSorted.add(x.getKey()));
            }
            // Rewrite totalProductsByStoreAndPrice.txt.
            BufferedWriter writer = new BufferedWriter(new FileWriter(totalProducts));
            for (String productInfo : productsSorted) {
                writer.write(productInfo);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' file to show the product information.
     *
     * @param number number of product
     * @return returns StringBuilder in toString format
     */
    public String showProductInfo(int number) {
        StringBuilder build = new StringBuilder();
        number = number - 1;
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();
        // reading the 'totalProductsByStoreAndPrice.txt'
        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
            }
            //Product1 by Store_Name with Price, Quantity
            // Bike by BikeStore with 9888.00, 3
            String tt = productList.get((number));
            String nameOne = tt.substring(0, tt.indexOf(" "));
            tt = tt.substring(tt.indexOf(" ") + 4);
            String storeName = tt.substring(0, tt.indexOf(" "));

            String path = storeName + "_" + nameOne + ".txt";
            File product = new File(path);
            try (BufferedReader bf = new BufferedReader(new FileReader(product))) {
                // reading the product file and appending build
                line = bf.readLine();
                while (line != null) {
                    for (int i = 0; i < 5; i++) {
                        if (i == 0) {
                            build.append("Product: ").append(line).append("\n");
                            line = bf.readLine();
                        } else if (i == 1) {
                            build.append("Store: ").append(line).append("\n");
                            line = bf.readLine();
                        } else if (i == 2) {
                            build.append("Description: ").append(line).append("\n");
                            line = bf.readLine();
                        } else if (i == 3) {
                            build.append("Quantity: ").append(line).append("\n");
                            line = bf.readLine();
                        } else {
                            build.append("Price: ").append(line).append("\n");
                            line = bf.readLine();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return build.toString();
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' file to check quantity of products.
     *
     * @param number   name of product
     * @param quantity amount of product available
     * @return A boolean returning false to check quantity.
     */
    public boolean checkQuantity(int number, int quantity) {
        number = number - 1;
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
            }
            //Product1 by Store_Name with Price, Quantity
            // Bike by BikeStore with 9888.00, 3
            String tt = productList.get((number));
            String nameOne = tt.substring(0, tt.indexOf(" "));
            tt = tt.substring(tt.indexOf(" ") + 4);
            String storeName = tt.substring(0, tt.indexOf(" "));

            String path = storeName + "_" + nameOne + ".txt";
            File product = new File(path);
            ArrayList<String> productInfo = new ArrayList<>();
            try (BufferedReader bf = new BufferedReader(new FileReader(product))) {
                line = bf.readLine();
                while (line != null) {
                    productInfo.add(line);
                    line = bf.readLine();
                }

                if (quantity <= Integer.parseInt(productInfo.get(3)) && quantity > 0) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' file to check quantity of products.
     *
     * @param number   name of product
     * @param quantity amount of product available
     */
    public void purchaseItem(int number, int quantity) {
        String pathOne = getName() + ".txt";

        number = number - 1;
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
            }
            //Product1 by Store_Name with Price, Quantity
            // Bike by BikeStore with 9888.00, 3
            String tt = productList.get((number));
            String nameOne = tt.substring(0, tt.indexOf(" "));
            tt = tt.substring(tt.indexOf(" ") + 4);
            String storeName = tt.substring(0, tt.indexOf(" "));

            String path = storeName + "_" + nameOne + ".txt";
            File product = new File(path);
            ArrayList<String> productInfo = new ArrayList<>();
            try (BufferedReader bf = new BufferedReader(new FileReader(product))) {
                line = bf.readLine();
                while (line != null) {
                    productInfo.add(line);
                    line = bf.readLine();
                }

                // write purchase history
                try (PrintWriter pw = new PrintWriter(new FileOutputStream(pathOne, true))) {
                    pw.println(productInfo.get(0) + ", " + quantity + ", " + productInfo.get(4));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // decrease quantity purchased in Store_Name_Product_Name.txt
                try (PrintWriter pw = new PrintWriter(new FileOutputStream(path))) {
                    int newQuantity = Integer.parseInt(productInfo.get(3)) - quantity;
                    for (int i = 0; i < productInfo.size(); i++) {
                        if (i != 3) {
                            pw.println(productInfo.get(i));
                        } else {
                            pw.println(newQuantity);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // decrease quantity in totalProducts.txt
                try (PrintWriter pwo = new PrintWriter(new FileOutputStream(totalProducts))) {
                    int newQuantity = Integer.parseInt(productInfo.get(3)) - quantity;
                    for (int i = 0; i < productList.size(); i++) {
                        if (i == number) {
                            pwo.println(productList.get(number).substring(0, productList.get(number).length() - 1)
                                    + newQuantity);
                        } else {
                            pwo.println(productList.get(i));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // write to seller's record
                String pathTwo = productInfo.get(1) + "_" + productInfo.get(0) + "_Purchase.txt";
                try (PrintWriter pw = new PrintWriter(new FileOutputStream(pathTwo, true))) {
                    double revenue = Double.parseDouble(productInfo.get(4)) * quantity;
                    pw.println(getName() + ", " + quantity + ", " + productInfo.get(4) + ", " + revenue);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the text file to allow customers to export a csv file with their purchase history.
     *
     * @param view   option to view the purchase history
     * @param export option to export purchase history
     */
    public String exportHistory(boolean view, boolean export) {
        String inputPath = getName() + ".txt";
        String outputPath = getName() + "_PurchasedHistory.csv";
        ArrayList<String> purchaseHistory = new ArrayList<>();
        String viewHistory = "";

        try (BufferedReader bfr = new BufferedReader(new FileReader(inputPath))) {
            String line = bfr.readLine();
            while (line != null) {
                purchaseHistory.add(line);
                line = bfr.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (view) {
            for (String s : purchaseHistory) {
                viewHistory += s + "\n";
            }
        }

        if (export) {
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(outputPath))) {
                for (String s : purchaseHistory) {
                    pw.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return viewHistory;
    }

    /**
     * Reads the 'totalProducts.txt' to search by product name.
     *
     * @param productName name of product
     * @return A boolean returning whether the item is found or not found.
     */
    public String searchByProductName(String productName) {
        String path = "totalProducts.txt";

        ArrayList<String> item = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(path))) {
            String line = bfr.readLine();
            while (line != null) {
                item.add(line);
                if (line.equals(productName)) {
                    return "Item Found! We have that item in our market!";
                }
                line = bfr.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Item not Found!";
    }

    /**
     * Reads the 'totalStores.txt' to search by store name.
     *
     * @param storeName name of store
     * @return A boolean returning whether the store is found or not found.
     */
    public String searchByStoreName(String storeName) {
        String path = "totalStores.txt";

        ArrayList<String> item = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(path))) {
            String line = bfr.readLine();
            while (line != null) {
                item.add(line);
                if (line.equals(storeName)) {
                    return "Store Found!";
                }
                line = bfr.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No such store Found!";
    }

    /**
     * Reads the 'totalProductsByStoreAndPrice.txt' to search by description.
     *
     * @param description name of store
     * @return A boolean returning whether the item is found or not found.
     */
    public String searchByDescription(String description) {
        File totalProducts = new File("totalProductsByStoreAndPrice.txt");
        ArrayList<String> productList = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader(totalProducts))) {
            String line = bfr.readLine();
            while (line != null) {
                productList.add(line);
                line = bfr.readLine();
            }

            for (int i = 0; i < productList.size(); i++) {

                String tt = productList.get((i));
                String nameOne = tt.substring(0, tt.indexOf(" "));
                tt = tt.substring(tt.indexOf(" ") + 4);
                String storeName = tt.substring(0, tt.indexOf(" "));

                String path = storeName + "_" + nameOne + ".txt";
                File product = new File(path);
                ArrayList<String> productInfo = new ArrayList<>();
                try (BufferedReader bf = new BufferedReader(new FileReader(product))) {
                    line = bf.readLine();
                    while (line != null) {
                        productInfo.add(line);
                        if (line.contains(description)) {
                            return "Item Found! We have that item with such description in our market!";
                        }
                        line = bf.readLine();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Item not Found!";
    }

    /**
     * Dashboard method to allow customer to view store and seller information. Customer has the option to view stores
     * in different sorting methods.
     */
    public String dashBoard(boolean reverse) {
        StringBuilder result = new StringBuilder();
        File totalSellers = new File("totalSellers.txt");
        if (!totalSellers.exists()) { // no sellers yet
            result.append("No history! No seller exists.");
        } else { // at lease one seller
            HashMap<String, Integer> storesBySold = new HashMap<>();
            HashMap<String, Integer> storesByPurchase = new HashMap<>();
            try (BufferedReader readTotalSellers = new BufferedReader(new FileReader(totalSellers))) {
                String seller; // each line is a seller name
                while ((seller = readTotalSellers.readLine()) != null) {
                    File sellerStores = new File(seller + "_totalStores.txt");
                    if (sellerStores.exists()) {
                        BufferedReader readSellerTotalStores = new BufferedReader(new FileReader(sellerStores));
                        String store; // each line is a store name
                        while ((store = readSellerTotalStores.readLine()) != null) {
                            File storeProducts = new File(store + "_totalProducts.txt");
                            if (storeProducts.exists()) {
                                BufferedReader readStoreTotalProducts =
                                        new BufferedReader(new FileReader(storeProducts));
                                String product; // Each line is a product name.
                                while ((product = readStoreTotalProducts.readLine()) != null) {
                                    File history = new File(store + "_" + product + "_purchase.txt");
                                    if (history.exists()) {
                                        BufferedReader readHistory = new BufferedReader(new FileReader(history));
                                        String purchase; // Each line is a purchase history.
                                        while ((purchase = readHistory.readLine()) != null) {
                                            String[] purchaseElements = purchase.split(", ");
                                            String customer = purchaseElements[0];
                                            Integer quantity = Integer.parseInt(purchaseElements[1]);
                                            String storeBySeller = store + " by " + seller;
                                            if (customer.equals(name)) { // This customer made the purchase.
                                                if (storesByPurchase.containsKey(storeBySeller)) {
                                                    storesByPurchase.put(storeBySeller,
                                                            storesByPurchase.get(storeBySeller) + quantity);
                                                } else {
                                                    storesByPurchase.put(storeBySeller, quantity);
                                                }
                                            }
                                            if (storesBySold.containsKey(storeBySeller)) {
                                                storesBySold.put(storeBySeller,
                                                        storesBySold.get(storeBySeller) + quantity);
                                            } else {
                                                storesBySold.put(storeBySeller, quantity);
                                            }
                                        }
                                        readHistory.close();
                                    }
                                }
                                readStoreTotalProducts.close();
                            }
                        }
                        readSellerTotalStores.close();
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            // Sort the lists.
            LinkedHashMap<String, Integer> sortedStoresBySold = new LinkedHashMap<>();
            if (!reverse) {
                storesBySold.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                        sortedStoresBySold.put(x.getKey(), x.getValue()));
            } else {
                storesBySold.entrySet().stream().sorted(Map.Entry.comparingByValue(
                        Comparator.reverseOrder())).forEachOrdered(x ->
                        sortedStoresBySold.put(x.getKey(), x.getValue()));
            }
            LinkedHashMap<String, Integer> sortedStoresByPurchase = new LinkedHashMap<>();
            if (!reverse) {
                storesByPurchase.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x ->
                        sortedStoresByPurchase.put(x.getKey(), x.getValue()));
            } else {
                storesByPurchase.entrySet().stream().sorted(
                        Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x ->
                        sortedStoresByPurchase.put(x.getKey(), x.getValue()));
            }
            // Display the lists.
            result.append("Stores by number of sales:\n");
            for (String key : sortedStoresBySold.keySet()) {
                result.append(String.format("%s sold %d products.\n", key, sortedStoresBySold.get(key)));
            }
            result.append("Stores by number of purchases you made:\n");
            for (String key : sortedStoresByPurchase.keySet()) {
                result.append(String.format("At %s, you purchased %d products.\n", key,
                        sortedStoresByPurchase.get(key)));
            }
        }
        return result.toString();
    }
}
