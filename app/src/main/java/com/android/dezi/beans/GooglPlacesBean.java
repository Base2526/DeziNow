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
 * Created by Mobilyte on 2/19/2016.
 */
public class GooglPlacesBean {

    private String next_page_token;
    private String status;
    private List<?> html_attributions;
    /**
     * geometry : {"location":{"lat":51.5054913,"lng":-0.0909134}}
     * icon : https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png
     * id : 9422f4f847edde2681bbda2b1e8d924833e35926
     * name : Borough Market
     * opening_hours : {"open_now":false,"weekday_text":[]}
     * photos : [{"height":1152,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/111333382820326522574/photos\">Diego Viscardini<\/a>"],"photo_reference":"CmRdAAAA_zu8YxmLSpX7hqgEEromK1WKXYhZwGb5B869yqin3qyExS-pVWjVRjx6FL6zbacNHnGzESx5bFKQV8hjVdAd126eAC482ZjNDJYDTQwPKrVgZo6i7qk16LQfHL9VR5MmEhCCTA7VtrGVXoFgrNyqnE-wGhSH_9BCINRA1VAXcoAaahq-eK_RCA","width":2048}]
     * place_id : ChIJ1eu1vwgIdkgRwjsifpZERQc
     * price_level : 2
     * rating : 4.6
     * reference : CnRhAAAAE5oeXX0P_J550ORnz_aLlrCuoMDsbJmbyClLBhKa_-_YlIhIdGDP6tzMUHVRu2ZHbNtr6eetx_u9Uk9zBrPVwanq3uq9EBR_OKQjoiHgjQY__8gyaRfUM0KnNFFKMYllRIy4VZ15IjIw-MOJWacOuBIQy15WZhQHkjuhi5qxxk96whoU6UBBA9ZGFOkHvnv2RIaXhNEQafU
     * scope : GOOGLE
     * types : ["food","point_of_interest","establishment"]
     * vicinity : 8 Southwark Street, London
     */

    private List<ResultsEntity> results;

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHtml_attributions(List<?> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public String getStatus() {
        return status;
    }

    public List<?> getHtml_attributions() {
        return html_attributions;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        /**
         * location : {"lat":51.5054913,"lng":-0.0909134}
         */

        private GeometryEntity geometry;
        private String icon;
        private String id;
        private String name;
        /**
         * open_now : false
         * weekday_text : []
         */

        private OpeningHoursEntity opening_hours;
        private String place_id;
        private int price_level;
        private double rating;
        private String reference;
        private String scope;
        private String vicinity;
        /**
         * height : 1152
         * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/111333382820326522574/photos\">Diego Viscardini<\/a>"]
         * photo_reference : CmRdAAAA_zu8YxmLSpX7hqgEEromK1WKXYhZwGb5B869yqin3qyExS-pVWjVRjx6FL6zbacNHnGzESx5bFKQV8hjVdAd126eAC482ZjNDJYDTQwPKrVgZo6i7qk16LQfHL9VR5MmEhCCTA7VtrGVXoFgrNyqnE-wGhSH_9BCINRA1VAXcoAaahq-eK_RCA
         * width : 2048
         */

        private List<PhotosEntity> photos;
        private List<String> types;

        public void setGeometry(GeometryEntity geometry) {
            this.geometry = geometry;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOpening_hours(OpeningHoursEntity opening_hours) {
            this.opening_hours = opening_hours;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public void setPrice_level(int price_level) {
            this.price_level = price_level;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public void setPhotos(List<PhotosEntity> photos) {
            this.photos = photos;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public GeometryEntity getGeometry() {
            return geometry;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public OpeningHoursEntity getOpening_hours() {
            return opening_hours;
        }

        public String getPlace_id() {
            return place_id;
        }

        public int getPrice_level() {
            return price_level;
        }

        public double getRating() {
            return rating;
        }

        public String getReference() {
            return reference;
        }

        public String getScope() {
            return scope;
        }

        public String getVicinity() {
            return vicinity;
        }

        public List<PhotosEntity> getPhotos() {
            return photos;
        }

        public List<String> getTypes() {
            return types;
        }

        public static class GeometryEntity {
            /**
             * lat : 51.5054913
             * lng : -0.0909134
             */

            private LocationEntity location;

            public void setLocation(LocationEntity location) {
                this.location = location;
            }

            public LocationEntity getLocation() {
                return location;
            }

            public static class LocationEntity {
                private double lat;
                private double lng;

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }
        }

        public static class OpeningHoursEntity {
            private boolean open_now;
            private List<?> weekday_text;

            public void setOpen_now(boolean open_now) {
                this.open_now = open_now;
            }

            public void setWeekday_text(List<?> weekday_text) {
                this.weekday_text = weekday_text;
            }

            public boolean isOpen_now() {
                return open_now;
            }

            public List<?> getWeekday_text() {
                return weekday_text;
            }
        }

        public static class PhotosEntity {
            private int height;
            private String photo_reference;
            private int width;
            private List<String> html_attributions;

            public void setHeight(int height) {
                this.height = height;
            }

            public void setPhoto_reference(String photo_reference) {
                this.photo_reference = photo_reference;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setHtml_attributions(List<String> html_attributions) {
                this.html_attributions = html_attributions;
            }

            public int getHeight() {
                return height;
            }

            public String getPhoto_reference() {
                return photo_reference;
            }

            public int getWidth() {
                return width;
            }

            public List<String> getHtml_attributions() {
                return html_attributions;
            }
        }
    }
}
