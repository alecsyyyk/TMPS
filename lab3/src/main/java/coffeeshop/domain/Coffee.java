package coffeeshop.domain;

// OCP: Coffee is the extension point for new drinks.
// Decorator - Component: add-ons wrap a Coffee and are a Coffee themselves.
public interface Coffee extends Product {
}
