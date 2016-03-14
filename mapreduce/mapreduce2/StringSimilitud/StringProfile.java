package StringSimilitud;

import utils.SparseIntegerVector;

public class StringProfile {

    private final SparseIntegerVector vector;
    private final KShingling ks;

    public StringProfile(SparseIntegerVector vector, KShingling ks) {
        this.vector = vector;
        this.ks = ks;
    }

    public double cosineSimilarity(StringProfile other) throws Exception {
        if (this.ks != other.ks) {
            throw new Exception("Profiles were not created using the same kshingling object!");
        }

        return this.vector.cosineSimilarity(other.vector);
    }

    public double qgramDistance(StringProfile other) throws Exception {
        if (this.ks != other.ks) {
            throw new Exception("Profiles were not created using the same kshingling object!");
        }

        return this.vector.qgram(other.vector);
    }

    public SparseIntegerVector getSparseVector() {
        return this.vector;
    }

    public String[] getMostFrequentNGrams(int number) {
        String[] strings = new String[number];
        int[] frequencies = new int[number];

        int position_smallest_frequency = 0;

        for (int i = 0; i < vector.size(); i++) {
            int key = vector.getKey(i);
            int frequency = vector.getValue(i);
            String ngram = ks.getNGram(key);

            if (frequency > frequencies[position_smallest_frequency]) {
                strings[position_smallest_frequency] = ngram;
                frequencies[position_smallest_frequency] = frequency;

                int smallest_frequency = Integer.MAX_VALUE;
                for (int j = 0; j < frequencies.length; j++) {
                    if (frequencies[j] < smallest_frequency) {
                        position_smallest_frequency = j;
                        smallest_frequency = frequencies[j];
                    }
                }
            }

        }
        return strings;
    }
}
