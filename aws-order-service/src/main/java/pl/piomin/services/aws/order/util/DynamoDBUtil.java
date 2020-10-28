package pl.piomin.services.aws.order.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DynamoDBUtil {

	public static void addList(String key, JsonObjectBuilder objectBuilder, List<JsonObject> items) {
		if (!items.isEmpty()) {
			JsonArrayBuilder builder = Json.createArrayBuilder();
			items.forEach(i -> builder.add(i));
			objectBuilder.add(key, builder.build());
		}

	}

	public static JsonArray toJson(List<AttributeValue> attributeValues) {
		if (attributeValues == null) {
			return null;
		}
		JsonArrayBuilder valueBuilder = Json.createArrayBuilder();
		for (AttributeValue a : attributeValues) {
			add(toJson(a), valueBuilder);
		}
		return valueBuilder.build();
	}

	public static JsonObject toJson(Map<String, AttributeValue> attributeValues) {
		if (attributeValues == null) {
			return null;
		}
		JsonObjectBuilder valueBuilder = Json.createObjectBuilder();
		for (Map.Entry<String, AttributeValue> a : attributeValues.entrySet()) {
			add(a.getKey(), toJson(a.getValue()), valueBuilder);
		}
		return valueBuilder.build();
	}

	public static void add(String key, Object value, JsonObjectBuilder object) {
		if (value instanceof JsonValue) {
			object.add(key, (JsonValue) value);
			// with json-p 1.0 can't create JsonString or JsonNumber so simply setting JsonValue not an option.
		} else if (value instanceof String) {
			object.add(key, (String) value);
		} else if (value instanceof BigDecimal) {
			object.add(key, (BigDecimal) value);
		} else if (value instanceof Boolean) {
			object.add(key, (Boolean) value);
		} else if (value.equals(JsonValue.NULL)) {
			object.addNull(key);
		}

	}

	public static void add(Object value, JsonArrayBuilder array) {
		if (value instanceof JsonValue) {
			array.add((JsonValue) value);
		} else if (value instanceof String) {
			array.add((String) value);
		} else if (value instanceof BigDecimal) {
			array.add((BigDecimal) value);
		} else if (value instanceof Boolean) {
			array.add((Boolean) value);
		} else if (value.equals(JsonValue.NULL)) {
			array.addNull();
		}

	}

	public static Object toJson(AttributeValue attributeValue) {
		// with json-p 1.1 Json.createValue() can be used.
		if (attributeValue == null) {
			return null;
		}
		if (attributeValue.s() != null) {
			return attributeValue.s();
		}
		if (attributeValue.m() != null) {
			return toJson(attributeValue.m());
		}
		if (attributeValue.l() != null) {
			return toJson(attributeValue.l());
		}
		if (attributeValue.n() != null) {
			return new BigDecimal(attributeValue.n());
		}
		if (attributeValue.bool() != null) {
			// return attributeValue.bool() ? JsonValue.TRUE : JsonValue.FALSE;
			return attributeValue.bool();
		}
		if (attributeValue.nul() != null && attributeValue.nul()) {
			return JsonValue.NULL;
		}
		/* if (attributeValue.b() != null) {
		 * return Base64.getEncoder().encodeToString(attributeValue.b().array());
		 * } */
		return null;
	}

	public static Map<String, AttributeValue> toAttribute(JsonObject jsonObject) {
		Map<String, AttributeValue> attribute = new HashMap<>();
		jsonObject.entrySet().forEach(e -> {
			attribute.put(e.getKey(), toAttribute(e.getValue()));
		});
		return attribute;
	}

	public static List<AttributeValue> toAttribute(JsonArray jsonArray) {
		List<AttributeValue> attributes = new LinkedList<>();
		jsonArray.forEach(e -> {
			attributes.add(toAttribute(e));
		});
		return attributes;
	}

	public static AttributeValue toAttribute(JsonValue jsonValue) {
		if (jsonValue == null) {
			return null;
		}
		switch (jsonValue.getValueType()) {
		case STRING:
			return AttributeValue.builder().s(((JsonString) jsonValue).getString()).build();
		case OBJECT:
			return AttributeValue.builder().m(toAttribute((JsonObject) jsonValue)).build();
		case ARRAY:
			return AttributeValue.builder().l(toAttribute((JsonArray) jsonValue)).build();
		case NUMBER:
			return AttributeValue.builder().n(((JsonNumber) jsonValue).toString()).build();
		case TRUE:
			return AttributeValue.builder().bool(true).build();
		case FALSE:
			return AttributeValue.builder().bool(false).build();
		case NULL:
			return AttributeValue.builder().nul(true).build();
		}

		return null;
	}

}