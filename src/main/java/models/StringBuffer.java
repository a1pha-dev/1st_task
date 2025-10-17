package models;

public class StringBuffer {
    private final String original;
    private String modified;

    public StringBuffer(String original) {
        if (original.length() <= 50) {
            throw new IllegalArgumentException("Строка должна быть длиннее 50 символов.");
        }
        this.original = original;
    }

    public void reverse() {
        modified = new StringBuilder(original).reverse().toString();
    }

    public void concatenate(String toConcat) {
        modified = original + toConcat;
    }

    public String getOriginal() {
        return original;
    }

    public  String getModified() {
        return modified;
    }
}
