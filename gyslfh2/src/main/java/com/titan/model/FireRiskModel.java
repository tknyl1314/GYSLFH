package com.titan.model;

import java.util.List;

/**
 * Created by whs on 2017/5/24
 */

public class FireRiskModel {

    /**
     * geometryType : esriGeometryPolygon
     * spatialReference : {"wkid":102012}
     * features : [{"attributes":{"DATE":"20170524","HOUR":"08","LEVEL":2},"geometry":{"rings":[[[204374.899194398,3176247.79461951],[203955.932770038,3175958.19536409],[201050.579905594,3177502.05305312]]]}}]
     */

    private String geometryType;
    private SpatialReferenceBean spatialReference;
    private List<FeaturesBean> features;

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }

    public SpatialReferenceBean getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(SpatialReferenceBean spatialReference) {
        this.spatialReference = spatialReference;
    }

    public List<FeaturesBean> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeaturesBean> features) {
        this.features = features;
    }

    public static class SpatialReferenceBean {
        /**
         * wkid : 102012
         */

        private int wkid;

        public int getWkid() {
            return wkid;
        }

        public void setWkid(int wkid) {
            this.wkid = wkid;
        }
    }

    public static class FeaturesBean {
        /**
         * attributes : {"DATE":"20170524","HOUR":"08","LEVEL":2}
         * geometry : {"rings":[[[204374.899194398,3176247.79461951],[203955.932770038,3175958.19536409],[201050.579905594,3177502.05305312]]]}
         */

        private AttributesBean attributes;
        private GeometryBean geometry;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public static class AttributesBean {
            /**
             * DATE : 20170524
             * HOUR : 08
             * LEVEL : 2
             */

            private String DATE;
            private String HOUR;
            private int LEVEL;

            public String getDATE() {
                return DATE;
            }

            public void setDATE(String DATE) {
                this.DATE = DATE;
            }

            public String getHOUR() {
                return HOUR;
            }

            public void setHOUR(String HOUR) {
                this.HOUR = HOUR;
            }

            public int getLEVEL() {
                return LEVEL;
            }

            public void setLEVEL(int LEVEL) {
                this.LEVEL = LEVEL;
            }
        }

        public static class GeometryBean {
            private List<List<List<Double>>> rings;

            public List<List<List<Double>>> getRings() {
                return rings;
            }

            public void setRings(List<List<List<Double>>> rings) {
                this.rings = rings;
            }
        }
    }
}
