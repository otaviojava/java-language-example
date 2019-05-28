package sh.platform.languages;

public enum  SamplesAvailable {
    MONGODB("MongoDBSample"),MYSQL("MySQLSample"),POSTGRESQL("PostgreSQLSample"),REDIS("RedisSample");

    private final String file;

    SamplesAvailable(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
