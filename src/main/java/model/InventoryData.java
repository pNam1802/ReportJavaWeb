package model;



public class InventoryData {
    public static class Inventory {
        private String category;
        private int stock;

        public Inventory(String category, int stock) {
            this.category = category;
            this.stock = stock;
        }

        public String getCategory() {
            return category;
        }

        public int getStock() {
            return stock;
        }
    }
}