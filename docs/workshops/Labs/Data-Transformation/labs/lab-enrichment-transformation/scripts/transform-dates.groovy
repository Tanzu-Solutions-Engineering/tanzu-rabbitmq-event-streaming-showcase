com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper()

var dateFormat = "M/dd/yyyy";

var payloadMap = objectMapper.readValue(payload,Map.class);

var timestamp = String.valueOf(payloadMap.get("timestamp"));
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(dateFormat);
payloadMap.put("timestamp",sdf.format(new Date(Long.valueOf(timestamp))));

objectMapper.writeValueAsString(payloadMap);

