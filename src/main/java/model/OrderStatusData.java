package model;

public class OrderStatusData {
    public static class Status {
        private String status;
        private double percentage;

        public Status(String status, double percentage) {
            this.status = status;
            this.percentage = percentage;
        }

        public String getStatus() {
            return status;
        }

        public double getPercentage() {
            return percentage;
        }
    }
}