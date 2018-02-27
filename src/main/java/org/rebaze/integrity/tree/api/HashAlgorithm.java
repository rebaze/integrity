package org.rebaze.integrity.tree.api;

public enum HashAlgorithm
{
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512"),
    MD5("MD5");

    private final String val;

    HashAlgorithm(String val) {
        this.val = val;
    }

    public String value() {
        return val;
    }

    public static HashAlgorithm fromString(String s) {
        for (HashAlgorithm b : HashAlgorithm.values()) {
            if (b.val.equalsIgnoreCase(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException( "Digest " + s + " not valid." );
    }

}
