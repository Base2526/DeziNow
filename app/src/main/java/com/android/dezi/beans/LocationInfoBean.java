/*
 *
 *
 *  * Copyright © 2016, Mobilyte Inc. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright
 *  *    notice, this list of conditions and the following disclaimer.
 *  *
 *  * - Redistributions in binary form must reproduce the above copyright
 *  * notice, this list of conditions and the following disclaimer in the
 *  * documentation and/or other materials provided with the distribution.
 *
 * /
 */

package com.android.dezi.beans;

import java.util.List;

/**
 * Created by Mobilyte Inc. on 5/9/2016.
 */
public class LocationInfoBean {

    /**
     * results : [{"address_components":[{"long_name":"819","short_name":"819","types":["premise"]}],"formatted_address":"819, Pancheel Park, Sector 4, Panchkula, Haryana 134108, India","geometry":{"location":{"lat":30.694129,"lng":76.86067299999999},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":30.6954779802915,"lng":76.8620219802915},"southwest":{"lat":30.6927800197085,"lng":76.85932401970848}}},"place_id":"ChIJPUbrqnqTDzkRYBwbs51UBZs","types":["street_address"]},{"address_components":[{"long_name":"Pancheel Park","short_name":"Pancheel Park","types":["neighborhood","political"]},{"long_name":"Budanpur","short_name":"Budanpur","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Panchkula Urban Estate","short_name":"Panchkula Urban Estate","types":["locality","political"]},{"long_name":"Panchkula","short_name":"Panchkula","types":["administrative_area_level_2","political"]},{"long_name":"Haryana","short_name":"HR","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]}],"formatted_address":"Pancheel Park, Budanpur, Panchkula Urban Estate, Haryana, India","geometry":{"bounds":{"northeast":{"lat":30.694822,"lng":76.8623602},"southwest":{"lat":30.6933661,"lng":76.85952}},"location":{"lat":30.6942322,"lng":76.8605516},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.6954430302915,"lng":76.8623602},"southwest":{"lat":30.6927450697085,"lng":76.85952}}},"place_id":"ChIJs82GqXqTDzkReuYPrASLrMA","types":["neighborhood","political"]},{"address_components":[{"long_name":"Sector 4","short_name":"Sector 4","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Panchkula","short_name":"Panchkula","types":["locality","political"]},{"long_name":"Panchkula","short_name":"Panchkula","types":["administrative_area_level_2","political"]},{"long_name":"Haryana","short_name":"HR","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]}],"formatted_address":"Sector 4, Panchkula, Haryana, India","geometry":{"bounds":{"northeast":{"lat":30.69519,"lng":76.86807},"southwest":{"lat":30.683037,"lng":76.853366}},"location":{"lat":30.6903348,"lng":76.8609575},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.69519,"lng":76.86807},"southwest":{"lat":30.683037,"lng":76.853366}}},"place_id":"ChIJaQucUHuTDzkRB6cdtgUTvZQ","types":["sublocality_level_1","sublocality","political"]},{"address_components":[{"long_name":"Budanpur","short_name":"Budanpur","types":["sublocality_level_1","sublocality","political"]},{"long_name":"Panchkula Urban Estate","short_name":"Panchkula Urban Estate","types":["locality","political"]},{"long_name":"Panchkula","short_name":"Panchkula","types":["administrative_area_level_2","political"]},{"long_name":"Haryana","short_name":"HR","types":["administrative_area_level_1","political"]},{"long_name":"India","short_name":"IN","types":["country","political"]}],"formatted_address":"Budanpur, Panchkula Urban Estate, Haryana, India","geometry":{"bounds":{"northeast":{"lat":30.7129828,"lng":76.8770554},"southwest":{"lat":30.6679353,"lng":76.8220496}},"location":{"lat":30.6907767,"lng":76.8285271},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":30.7129828,"lng":76.8770554},"southwest":{"lat":30.6679353,"lng":76.8220496}}},"place_id":"ChIJfzfk4UKTDzkRKw2LsX7Ssuk","types":["sublocality_level_1","sublocality","political"]}]
     * status : OK
     */

    private String status;
    /**
     * address_components : [{"long_name":"819","short_name":"819","types":["premise"]}]
     * formatted_address : 819, Pancheel Park, Sector 4, Panchkula, Haryana 134108, India
     * geometry : {"location":{"lat":30.694129,"lng":76.86067299999999},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":30.6954779802915,"lng":76.8620219802915},"southwest":{"lat":30.6927800197085,"lng":76.85932401970848}}}
     * place_id : ChIJPUbrqnqTDzkRYBwbs51UBZs
     * types : ["street_address"]
     */

    private List<ResultsEntity> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public static class ResultsEntity {
        private String formatted_address;
        /**
         * location : {"lat":30.694129,"lng":76.86067299999999}
         * location_type : ROOFTOP
         * viewport : {"northeast":{"lat":30.6954779802915,"lng":76.8620219802915},"southwest":{"lat":30.6927800197085,"lng":76.85932401970848}}
         */

        private GeometryEntity geometry;
        private String place_id;
        /**
         * long_name : 819
         * short_name : 819
         * types : ["premise"]
         */

        private List<AddressComponentsEntity> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryEntity getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryEntity geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<AddressComponentsEntity> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsEntity> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryEntity {
            /**
             * lat : 30.694129
             * lng : 76.86067299999999
             */

            private LocationEntity location;
            private String location_type;
            /**
             * northeast : {"lat":30.6954779802915,"lng":76.8620219802915}
             * southwest : {"lat":30.6927800197085,"lng":76.85932401970848}
             */

            private ViewportEntity viewport;

            public LocationEntity getLocation() {
                return location;
            }

            public void setLocation(LocationEntity location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportEntity getViewport() {
                return viewport;
            }

            public void setViewport(ViewportEntity viewport) {
                this.viewport = viewport;
            }

            public static class LocationEntity {
                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportEntity {
                /**
                 * lat : 30.6954779802915
                 * lng : 76.8620219802915
                 */

                private NortheastEntity northeast;
                /**
                 * lat : 30.6927800197085
                 * lng : 76.85932401970848
                 */

                private SouthwestEntity southwest;

                public NortheastEntity getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastEntity northeast) {
                    this.northeast = northeast;
                }

                public SouthwestEntity getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestEntity southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastEntity {
                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestEntity {
                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class AddressComponentsEntity {
            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}
