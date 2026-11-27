# Laboratory Work No. 4 - Behavioral Design Patterns

Coffee shop console application, continued from Lab 1 (SOLID), Lab 2
(Factory Method, Builder, Singleton) and Lab 3 (Decorator, Composite, Proxy).
Lab 4 adds three behavioral patterns:

| Pattern      | Where                   | What it does in the app                                                     |
|--------------|-------------------------|-----------------------------------------------------------------------------|
| **State**    | `behavioral/state`      | An order goes New -> Paid -> Preparing -> Ready -> Completed (or Cancelled). Each status is a class that decides which actions are allowed. |
| **Observer** | `behavioral/observer`   | The kitchen screen, the customer SMS and a history log are notified on every status change. The SMS can be turned on/off at runtime. |
| **Command**  | `behavioral/command`    | Add product, remove product and pay are command objects kept in a history, so they can be undone (undo of pay = refund). |

The full report (domain, class diagrams, code snippets, tests, conclusions)
is in [REPORT.md](REPORT.md).

## Requirements

- JDK 17+
- Maven 3.9+

## Build and test

```bash
mvn test
```

## Run

```bash
# interactive coffee shop
mvn -q compile exec:java

# scripted demo: every pattern on its own, then one order through all of them
mvn -q compile exec:java -Dexec.args=demo
```

## Using the app

For every order you get a menu. The current state of the order is shown in
the title, and an action that the state does not allow is rejected with a
message (for example you cannot add a product after paying).

```text
=== Order #1 [New] - 1 item(s), total 40.00 MDL ===
a - add product        c - add custom coffee   d - remove product
p - pay                u - undo last action     h - show undo history
s - start preparing    r - mark ready           k - pick up
x - cancel order       n - customer SMS on/off
```

- `a`, `c`, `d`, `p` run a **Command** (saved in the undo history).
- `u` undoes the last command (remove the last product, or refund the payment).
- `s`, `r`, `k`, `x` move the order to another **State**.
- `n` subscribes / unsubscribes the customer SMS **Observer** while the app runs.
- When the order is picked up it is saved in the repository and the receipt is printed.

## Project structure

```text
src/main/java/coffeeshop
├── domain/                 Product, Coffee, Espresso, Latte, Cappuccino, Croissant, Order
├── payment/                Payment, CardPayment, CashPayment, PaymentDeclinedException
├── creational/
│   ├── factory/            Factory Method (Lab 2): product, combo and payment creators
│   └── builder/            Builder (Lab 2): CustomCoffeeBuilder
├── structural/
│   ├── decorator/          Decorator (Lab 3): coffee add-ons
│   ├── composite/          Composite (Lab 3): ProductBundle
│   └── proxy/              Proxy (Lab 3): PaymentProxy
├── behavioral/
│   ├── state/              State (Lab 4): OrderTicket (context), OrderState, BaseOrderState, 6 states
│   ├── observer/           Observer (Lab 4): OrderObserver, KitchenDisplay, CustomerNotifier, StatusHistoryLog
│   └── command/            Command (Lab 4): Command, CommandHistory, AddItem/RemoveItem/PayOrder commands
├── service/                OrderService, OrderRepository, InMemoryOrderRepository (Singleton), receipts
└── client/                 Main, ConsoleApp (interactive), Demo (scripted), Menu

src/test/java/coffeeshop    StateTest, ObserverTest, CommandTest, BehavioralInteractionTest
                            + Lab 3 tests: DecoratorTest, CompositeTest, ProxyTest, PatternInteractionTest
```
