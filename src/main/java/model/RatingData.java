package model;


public class RatingData {
    public static class Rating {
        private String rating;
        private int count;

        public Rating(String rating, int count) {
            this.rating = rating;
            this.count = count;
        }

        public String getRating() {
            return rating;
        }

        public int getCount() {
            return count;
        }
    }
}