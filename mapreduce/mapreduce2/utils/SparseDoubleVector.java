package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class SparseDoubleVector implements Serializable {

    protected int[] keys;
    protected double[] values;
    protected int size = 0;

    private double norm = -1.0;
    private int total_size = 1;

    private double sq_gamma = Double.MAX_VALUE;

    public SparseDoubleVector(int size) {
        keys = new int[size];
        values = new double[size];
    }

    public SparseDoubleVector() {
        this(20);
    }

    public SparseDoubleVector(HashMap<Integer, Double> hashmap) {
        this(hashmap.size());
        SortedSet<Integer> sorted_keys = new TreeSet<Integer>(hashmap.keySet());
        for (int key : sorted_keys) {
            keys[size] = key;
            values[size] = hashmap.get(key);
            size++;
        }
    }

    public SparseDoubleVector(double[] array) {

        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                size++;
            }
        }

        keys = new int[size];
        values = new double[size];
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                keys[j] = i;
                values[j] = array[i];
                j++;
            }
        }
    }

    public double dotProduct(SparseDoubleVector other) {
        double agg = 0;
        int i = 0;
        int j = 0;
        while (i < this.keys.length && j < other.keys.length) {
            int k1 = this.keys[i];
            int k2 = other.keys[j];

            if (k1 == k2) {
                agg += this.values[i] * other.values[j];
                i++;
                j++;

            } else if (k1 < k2) {
                i++;
            } else {
                j++;
            }
        }
        return agg;
    }

    public double dotProduct(double[] other) {
        double agg = 0;
        for (int i = 0; i < keys.length; i++) {
            agg += other[keys[i]] * values[i];
        }
        return agg;
    }

    public double jaccard(SparseDoubleVector other) {
        int intersection = this.intersection(other);
        return (double) intersection / (this.size + other.size - intersection);
    }

    public int union(SparseDoubleVector other) {
        return this.size + other.size - this.intersection(other);
    }

    public int intersection(SparseDoubleVector other) {
        int agg = 0;
        int i = 0;
        int j = 0;
        while (i < this.keys.length && j < other.keys.length) {
            int k1 = this.keys[i];
            int k2 = other.keys[j];

            if (k1 == k2) {
                agg++;
                i++;
                j++;

            } else if (k1 < k2) {
                i++;

            } else {
                j++;
            }
        }
        return agg;
    }

    @Override
    public String toString() {
        String r = "";
        for (int i = 0; i < size; i++) {
            r += keys[i] + ":" + values[i] + " ";
        }
        return r;
    }

    public double qgram(SparseDoubleVector other) {
        double agg = 0;
        int i = 0, j = 0;
        int k1, k2;

        while (i < this.keys.length && j < other.keys.length) {
            k1 = this.keys[i];
            k2 = other.keys[j];

            if (k1 == k2) {
                agg += Math.abs(this.values[i] - other.values[j]);
                i++;
                j++;

            } else if (k1 < k2) {
                agg += Math.abs(this.values[i]);
                i++;

            } else {
                agg += Math.abs(other.values[j]);
                j++;
            }
        }

        while (i < this.keys.length) {
            agg += Math.abs(this.values[i]);
            i++;
        }

        while (j < other.keys.length) {
            agg += Math.abs(other.values[j]);
            j++;
        }
        return agg;
    }

    public int size() {
        return this.size;
    }

    public double norm() {
        if (norm >= 0) {
            return norm;
        }

        double agg = 0;
        for (int i = 0; i < values.length; i++) {
            agg += values[i] * values[i];
        }
        norm = Math.sqrt(agg);
        return norm;
    }

    public double cosineSimilarity(SparseDoubleVector other) {

        double den
                = Math.min(this.sq_gamma, this.norm())
                * Math.min(other.sq_gamma, other.norm());

        double agg = 0;
        int i = 0;
        int j = 0;
        while (i < this.keys.length && j < other.keys.length) {
            int k1 = this.keys[i];
            int k2 = other.keys[j];

            if (k1 == k2) {
                agg += this.values[i] * other.values[j];// / den;
                i++;
                j++;

            } else if (k1 < k2) {
                i++;
            } else {
                j++;
            }
        }
        return agg / den;
    }

    public void sampleDIMSUM(double threshold, int count, int size) {
        this.total_size = size; 
        double gamma = 10 * Math.log(count) / threshold;
        this.sq_gamma = Math.sqrt(gamma);

        this.norm();

         
        double probability = sq_gamma / this.norm();

        if (probability >= 1.0) {
            return;
        }

        Random r = new Random();
        ArrayList<Integer> new_keys = new ArrayList<Integer>();
        ArrayList<Double> new_values = new ArrayList<Double>();

        for (int i = 0; i < keys.length; i++) {

            if (r.nextDouble() < probability) {
                new_keys.add(keys[i]);
                new_values.add(values[i]);
            }
        }

        this.keys = new int[new_keys.size()];
        this.values = new double[new_values.size()];
        this.size = new_keys.size();
        for (int i = 0; i < keys.length; i++) {
            this.keys[i] = new_keys.get(i);
            this.values[i] = new_values.get(i);
        }
    }

    public double[] toArray(final int size) {

        double[] array = new double[size];
        for (int i = 0; i < keys.length; i++) {
            array[keys[i]] = values[i];
        }
        return array;
    }
}
