package ru.demo.merch.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Compound {
    private String name;
    private Double value;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Compound compound = (Compound) o;
        return Objects.equals(name, compound.name) && Objects.equals(value, compound.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
