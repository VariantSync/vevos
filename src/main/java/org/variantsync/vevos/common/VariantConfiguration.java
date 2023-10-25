package org.variantsync.vevos.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VariantConfiguration implements Map<Object, Boolean> {

    private final Map<Object, Boolean> featureAssignment;

    public VariantConfiguration(Collection<Feature> features) {
        this.featureAssignment = new HashMap<>();
        features.forEach(f -> this.featureAssignment.put(f, true));
    }

    public boolean satisfies(PropositionalFormula formula) {
        return formula.evaluate(this);
    }

    @Override
    public int size() {
        return this.featureAssignment.size();
    }

    @Override
    public boolean isEmpty() {
        return this.featureAssignment.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return this.featureAssignment.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return this.featureAssignment.containsValue(o);
    }

    @Override
    public Boolean get(Object o) {
        Boolean value = this.featureAssignment.get(o);
        return value != null && value;
    }

    @Override
    public Boolean put(Object o, Boolean aBoolean) {
        if (!(o instanceof Feature)) {
            throw new IllegalArgumentException("VariantConfigurations must hold features");
        }
        return this.featureAssignment.put(o, aBoolean);
    }

    @Override
    public Boolean remove(Object o) {
        return this.featureAssignment.remove(o);
    }

    @Override
    public void putAll(Map<?, ? extends Boolean> map) {
        for (Entry<?, ? extends Boolean> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof Feature)) {
                throw new IllegalArgumentException("VariantConfigurations must hold features");
            }
            this.featureAssignment.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        this.featureAssignment.clear();
    }

    @Override
    public Set<Object> keySet() {
        return this.featureAssignment.keySet();
    }

    @Override
    public Collection<Boolean> values() {
        return this.featureAssignment.values();
    }

    @Override
    public Set<Entry<Object, Boolean>> entrySet() {
        return this.featureAssignment.entrySet();
    }
}
