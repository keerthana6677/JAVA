# A4 Serialization Design

For `Hostel -> Room -> Resident`, Java serialization writes reachable objects in the object graph. It is a Java-specific format and should not be used for interoperability or untrusted input.

## resident fields
```java
class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String registerNumber;
    private transient String password;
    private transient Connection databaseConnection;
}
```

`password` is transient because plaintext or reversible password material must not appear in a generic serialized file. Store a secure password hash if persistence is required, and keep authentication secrets in an access-controlled database or secret system. `databaseConnection` is transient because it represents a live socket/resource and cannot be reconstructed by serializing it.

## Object graph
Hostel contains rooms. Rooms contain residents. Serialization follows references and preserves the graph, but live connection fields are excluded. A deserialized object must reconnect when needed.

## Compatibility
Adding fields to a `Serializable` class changes its format. `serialVersionUID` identifies the wire version. Existing old files may deserialize if newly added fields have defaults (`null`, `0`, or `false`), but removing fields or changing types is unsafe. Validate deserialized data and use a schema/migration strategy.

## Format comparison

| Format | Strength | Weakness |
|---|---|---|
| Java serialization | Convenient object graph support | Java-only, not secure for untrusted data, compatibility-sensitive |
| CSV | Simple, portable | No object graph and weak type/schema information |
| JSON | Interoperable, readable | Requires explicit graph mapping and schema validation |
