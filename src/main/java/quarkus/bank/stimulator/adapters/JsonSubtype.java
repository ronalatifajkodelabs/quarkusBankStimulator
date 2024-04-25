package quarkus.bank.stimulator.adapters;

public @interface JsonSubtype {
    Class<?> clazz();

    String name();
}
