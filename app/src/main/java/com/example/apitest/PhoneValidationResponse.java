package com.example.apitest;

import com.google.gson.annotations.SerializedName;

public class PhoneValidationResponse {
    @SerializedName("valid")
    public boolean valid;

    @SerializedName("number")
    public String number;

    @SerializedName("international_format")
    public String internationalFormat;

    @SerializedName("country_name")
    public String countryName;

    @SerializedName("carrier")
    public String carrier;

    @SerializedName("line_type")
    public String lineType;

    @SerializedName("location")
    public String location;
}
