package io.github.java_projekt_pk.ui;

import java.util.Comparator;
import java.util.Objects;

public class MenuItem implements Comparable<MenuItem> {
    private final String label;
    private final MenuAction action;
    private final boolean selectable;

    public MenuItem(String label, MenuAction action, boolean selectable) {
        this.label = label;
        this.action = action;
        this.selectable = selectable;
    }

    public MenuItem(String label, MenuAction action) {
        this(label, action, true);
    }

    public MenuItem(String label) {
        this(label, () -> {});
    }

    public String getLabel() { return label; }
    public boolean isSelectable() { return selectable; }
    public void executeAction() {
        if(selectable) {
            action.execute();
        }
    }

    @Override
    public int compareTo(MenuItem o) {
        if (o == null) return 1;

        int labelComp = Comparator.<String>nullsLast(Comparator.naturalOrder())
            .compare(this.label, o.label);
        if (labelComp != 0) return labelComp;

        int selectablecomp = Boolean.compare(this.selectable, o.selectable);
        if (selectablecomp != 0) return selectablecomp;


        return Integer.compare(Objects.hashCode(this.action), Objects.hashCode(o.action));
    }

    @FunctionalInterface
    public interface MenuAction {
        void execute();
    }
}
