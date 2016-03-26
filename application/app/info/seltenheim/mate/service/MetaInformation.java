package info.seltenheim.mate.service;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MetaInformation {

	private final int dbVersion;
	private final int bottlesAvailable;
	private final int currentBottlePrice;

	public MetaInformation(int dbVersion, int bottlesAvailable, int currentBottlePrice) {
		this.dbVersion = dbVersion;
		this.bottlesAvailable = bottlesAvailable;
		this.currentBottlePrice = currentBottlePrice;
	}

	public int getDbVersion() {
		return dbVersion;
	}

	public int getBottlesAvailable() {
		return bottlesAvailable;
	}

	public int getCurrentBottlePrice() {
		return currentBottlePrice;
	}

	public JsonNode getJsonNode() {
		final ObjectNode jsonNode = Json.newObject();
		jsonNode.put("dbVersion", getDbVersion());
		jsonNode.put("bottlesAvailable", getBottlesAvailable());
		jsonNode.put("currentBottlePrice", getCurrentBottlePrice() / 100.0);
		
		return jsonNode;
	}

}
