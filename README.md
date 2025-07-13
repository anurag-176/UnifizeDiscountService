# Unifize Discount Service

## Prerequisites
- **Java:** 17 or higher (project uses Java 21 in pom.xml)
- **Maven:** 3.6+
- **Lombok:** No manual install needed for build, but install Lombok plugin in your IDE for best experience (IntelliJ/VSCode)

Follow these steps to install and start the Redis server on macOS:

1. **Install Homebrew** (if not already installed):

    ```bash
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    ```

2. **Install Redis via Homebrew**:

    ```bash
    brew install redis
    ```

3. **Start Redis Server**:

    ```bash
    redis-server
    ```

---

## Install maven

1. **Install Homebrew** (if not already installed):

    ```bash
    brew install maven
    ```


---
## Build Instructions

1. **Clone the repository:**
   ```sh
   git clone https://github.com/anurag-176/UnifizeDiscountService.git
   cd UnifizeDiscountService
   ```

2. **Build the project:**
   ```sh
   mvn clean install
   ```
   This will download dependencies, compile the code, and run all tests.

3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The service will start on the default port (usually 8080).

---

## Test Instructions
3. **Run the tests:**
   ```sh
   mvn test
   ```
---

## Postman Collection (APIs)
All APIs with examples for tests, documentation and beyond in one place.
```
https://drive.google.com/file/d/1286MWGeNra1uWdxIQ8kQUjsVuFsRfNlf/view?usp=sharing
```
---

## Usage

1. To start applying the policies. One must create the first.
All postman examples are already in place under ```create-discount-policies-API```. All one has to do is simply run them.

2. Next to test the implemented APIs, run any example you like or modify them.
These will be available under ```calculate-discount-API```

3. You can make your own policies too even one with compound ones. Check examples under ```create-discount-policies-API``` named as ```* (Compound condition)```

**System behaviour modularity are discussed below along with Data flow chart**

---

## ✅ Features

1. **Multi-Scope Discount Support**  
   • Supports multiple discount scopes:  
   &nbsp;&nbsp;&nbsp;&nbsp;• `CART_ITEM` — Discount applied per eligible cart item  
   &nbsp;&nbsp;&nbsp;&nbsp;• `CUSTOMER_INFO` — Discount based on customer profile (e.g., tier)


2. **a. Condition-Based Discount Application**  
   • Discounts are conditionally applied using expression-based rules.  
   • Supports expressions like:  
   &nbsp;&nbsp;&nbsp;&nbsp;• `$product.brand == "PUMA"`  
   &nbsp;&nbsp;&nbsp;&nbsp;• `$product.category == "T-Shirt"`  
   &nbsp;&nbsp;&nbsp;&nbsp;• `$tier == "GOLD"`  
   &nbsp;&nbsp;&nbsp;&nbsp;• `$bankName == "ICICI"`


2. **b. Compound Conditional queries**  
   • Discounts are conditionally applied using expression-based rules.  
   • Supports expressions like:  
   &nbsp;&nbsp;&nbsp;&nbsp;• `"$product.brandTier == \"PREMIUM\" && ($product.category == \"T-Shirt\" || $product.category == \"Jacket\")",`


3. **Percentage and Fixed Value Discounts**  
   • Discount types:  
   &nbsp;&nbsp;&nbsp;&nbsp;• `PERCENTAGE` — e.g., 10% off  
   &nbsp;&nbsp;&nbsp;&nbsp;• `CURRENCY` — e.g., ₹500 off


4. **Voucher-Based Discounts**  
   • Voucher-triggered discounts applied only when a valid code (e.g., `SUPER69`) is provided.


5. **Discount Targeting Strategies**  
   • Strategies to decide which items should receive discounts:  
   &nbsp;&nbsp;&nbsp;&nbsp;• `FIRST_MATCH`  
   &nbsp;&nbsp;&nbsp;&nbsp;• `MIN_PRICE`  
   &nbsp;&nbsp;&nbsp;&nbsp;• `ALL_MATCH`


6. **Application Limit Controls**  
   • `applicationCount`: Restricts how many items the discount can apply to.  
   • `applyToAll`: Applies discount to all matching items if set to `true`.


7. **Discount Stack Order**  
   • `stackOrder`: Controls order of discount application when multiple are eligible.


8. **Mutual Exclusivity Between Discounts**  
   • Prevents multiple conflicting discounts from being applied simultaneously using `mutuallyExclusiveWith`.


9. **Discount Capping**  
   • `maximumDiscountAmount`: Ensures percentage discounts don’t exceed a configured cap.


10. **Reusable Test Data via `TestDataConfig`**  
    • Clean and structured test data setup for scenarios like:  
    &nbsp;&nbsp;&nbsp;&nbsp;• PUMA brand discount  
    &nbsp;&nbsp;&nbsp;&nbsp;• ICICI bank card offer  
    &nbsp;&nbsp;&nbsp;&nbsp;• T-Shirt category discount  
    &nbsp;&nbsp;&nbsp;&nbsp;• Gold tier customer discount  
    &nbsp;&nbsp;&nbsp;&nbsp;• Voucher-based discount


11. **Unit Testing with JUnit 5 & Mockito**  
    • Modular and isolated testing of service logic with mocked dependencies.


12. **Integration Test Ready**  
    • Framework ready for end-to-end tests using `@SpringBootTest` and `MockMvc`.


## Data flow chart 
Each box here also depicts the scope of modularity for the system to change.

1. **ScopeEvaluator**: What things to put condition upon. Example, CartItem, Product, CustomerInfo, PaymentDetails.

2. **Condition Expression**: Instead of complicated DDLs, a simple string is parsed into a tree to evaluate conditions.

3. **Discount Target Strategy**: Pre filter / treat the CarItem(s) after applying expression (if applicable)

4. **VoucherCode**: Its applicable in all policies if present.





                                      ┌──────────────────────────────┐
                                      │   /api/v1/discounts/calculate│
                                      │     DiscountController       │
                                      └──────────────┬───────────────┘
                                                     │
                                                     ▼
                                      ┌──────────────┴───────────────┐
                                      │        DiscountService       │
                                      │   (core discount processing) │
                                      └────┬──────────────┬──────────┘
                                           │              │
                                           ▼              ▼
               ┌──────────────────────────────┐   ┌────────────────────────────┐
               │ DiscountPolicyRepository     │   │ ScopeEvaluatorFactory      │
               │ (fetches all discount rules) │   │ (returns evaluator by scope│
               └──────────────────────────────┘   └────────────────┬───────────┘
                                                                   │
                                                                   ▼
                                          ┌─────────────────────────────────────┐
                                          │         ScopeEvaluator (interface)  │
                                          │         ├── CartItem                │
                                          │         ├── PaymentInfo             │
                                          │         ├── FinalSum                │
                                          │         └── CustomerInfo            │
                                          └────────────────────┬────────────────┘
                                                               ▼
                                         ┌────────────────────────────────────┐
                                         │  DiscountTargetStrategy            │
                                         │  (e.g. MIN_PRICE, FIRST_MATCH)     │
                                         └────────────────────┬───────────────┘
                                                              ▼
                                    ┌──────────────────────────────────────────┐
                                    │  Final DiscountedPrice DTO is returned   │
                                    │  (originalPrice, finalPrice, discounts)  │
                                    └──────────────────────────────────────────┘


For more details (test execution, API usage, etc.), see further sections or contact the maintainer.
