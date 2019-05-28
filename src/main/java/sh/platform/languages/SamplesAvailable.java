package sh.platform.languages;

public enum  SamplesAvailable {
    MONGODB("MongoDBSample.java"),MYSQL("MySQLSample.java"),POSTGRESQL("PostgreSQLSample.java"),REDIS("RedisSample.java");

    private final String file;

    SamplesAvailable(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
