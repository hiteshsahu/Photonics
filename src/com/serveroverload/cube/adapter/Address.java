package com.serveroverload.cube.adapter;

public class Address {

	private String addressId;

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	private String addressLine1;
	private String addressLine2;
	private String landMark;

	private String pinCode;

	/**
	 * @param addressId
	 * @param addressLine1
	 * @param addressLine2
	 * @param landMark
	 * @param pinCode
	 */
	public Address(String addressId, String addressLine1, String addressLine2,
			String landMark, String pinCode) {
		this.addressId = addressId;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.landMark = landMark;
		this.pinCode = pinCode;
	}

	public String getAddValue() {
		return addressLine1 + "\n" + addressLine2 + "\n" + landMark + " - "
				+ pinCode;
	}

	// TODO use this while scaling
	// private String city;
	// private String state;
	// private String country;

}
