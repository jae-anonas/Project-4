package com.ga.roosevelt.mapboxapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Neighborhoods {

    @SerializedName("features")
    @Expose
    private List<Feature> features = new ArrayList<>();

    /**
     *
     * @return
     * The features
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     *
     * @param features
     * The features
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public class Feature {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("properties")
        @Expose
        private Properties properties;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;

        /**
         *
         * @return
         * The type
         */
        public String getType() {
            return type;
        }

        /**
         *
         * @param type
         * The type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         *
         * @return
         * The properties
         */
        public Properties getProperties() {
            return properties;
        }

        /**
         *
         * @param properties
         * The properties
         */
        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        /**
         *
         * @return
         * The geometry
         */
        public Geometry getGeometry() {
            return geometry;
        }

        /**
         *
         * @param geometry
         * The geometry
         */
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }


        public class Geometry {

            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("coordinates")
            @Expose
            private List<List<List<Double>>> coordinates = new ArrayList<List<List<Double>>>();

            /**
             *
             * @return
             * The type
             */
            public String getType() {
                return type;
            }

            /**
             *
             * @param type
             * The type
             */
            public void setType(String type) {
                this.type = type;
            }

            /**
             *
             * @return
             * The coordinates
             */
            public List<List<List<Double>>> getCoordinates() {
                return coordinates;
            }

            /**
             *
             * @param coordinates
             * The coordinates
             */
            public void setCoordinates(List<List<List<Double>>> coordinates) {
                this.coordinates = coordinates;
            }

        }
        public class Properties {

            @SerializedName("neighborhood")
            @Expose
            private String neighborhood;
            @SerializedName("boroughCode")
            @Expose
            private String boroughCode;
            @SerializedName("borough")
            @Expose
            private String borough;
            @SerializedName("@id")
            @Expose
            private String id;

            /**
             *
             * @return
             * The neighborhood
             */
            public String getNeighborhood() {
                return neighborhood;
            }

            /**
             *
             * @param neighborhood
             * The neighborhood
             */
            public void setNeighborhood(String neighborhood) {
                this.neighborhood = neighborhood;
            }

            /**
             *
             * @return
             * The boroughCode
             */
            public String getBoroughCode() {
                return boroughCode;
            }

            /**
             *
             * @param boroughCode
             * The boroughCode
             */
            public void setBoroughCode(String boroughCode) {
                this.boroughCode = boroughCode;
            }

            /**
             *
             * @return
             * The borough
             */
            public String getBorough() {
                return borough;
            }

            /**
             *
             * @param borough
             * The borough
             */
            public void setBorough(String borough) {
                this.borough = borough;
            }

            /**
             *
             * @return
             * The id
             */
            public String getId() {
                return id;
            }

            /**
             *
             * @param id
             * The @id
             */
            public void setId(String id) {
                this.id = id;
            }


        }
    }
}

