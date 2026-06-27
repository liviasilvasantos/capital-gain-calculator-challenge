## Description

# Capital Gains Calculator Challenge

## Overview

This challenge requires you to work on an **existing Capital Gains Tax Calculator** for stock market trades, where you will complete and extend the core logic. You will be extending an **existing Java Command Line Interface (CLI) application** that processes a sequence of stock operations (buys and sells) and calculates the corresponding capital gains tax.

---

## The Problem

Your task is to correctly process a stream of operations, maintaining the state of the investor's stock portfolio and calculating the tax due for each sale operation based on the defined rules.

### Core Tax Rules

1. **Tax Rate:** The tax rate is **20%** of the net profit from a sale.
2. **Cost Basis (Weighted Average):** The cost of the stock (used to calculate profit/loss on a sale) must be tracked using a **Weighted Average Cost**. When multiple purchases are made, the cost basis for all held stocks is the weighted average of the prices paid.
3. **Taxable Operations:** Only **sell** operations can generate capital gains tax. **Buy** operations never result in a tax liability.
4. **No Tax on Loss:** If a **sale** results in a net **loss** (the selling price is less than the Weighted Average Cost), **no tax** is due.


### Quick Example (Illustrating the Rules)

|Operation|Unit Cost|Quantity|Holdings Change|Weighted Avg. Cost|Tax Due|
|---|---|---|---|---|---|
|**Buy**|$10.00|10,000|Holdings: 10,000|$10.00|$0.00|
|**Sell**|$20.00|5,000|Profit: 5,000 * ($20.00 - $10.00) = $50,000|$10.00|20% of $50,000 = **$10,000.00**|
|**Sell**|$5.00|3,000|Loss: 3,000 * ($5.00 - $10.00) = -$15,000|$10.00|**$0.00** (No tax on loss)|
|**Buy**|$12.00|8,000|Holdings: 10,000|$11.60|$0.00|
|**Sell**|$15.00|10,000|Profit: 10,000 * ($15.00 - $11.60) = $34,000|N/A|**$6,800.00**|
|**Buy**|$15.00|2,000|Holdings: 2,000|$15.00|$0.00|

---

## Input/Output Specification

### Input Format

Input is read from **stdin** as a single JSON array per line, containing a sequence of operations.

**Example Input:**

```js
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000}, 
{"operation":"sell", "unit-cost":20.00, "quantity": 5000}, 
{"operation":"sell", "unit-cost":5.00, "quantity": 5000}]
```

### Output Format

Output is written to stdout as a JSON array where each object corresponds to the tax calculated for the operation at the same index in the input array.

Example Output:

```js
[{"tax": 0.0}, 
{"tax": 10000.0}, 
{"tax": 0.0}]
```

### Getting Started & Your Task

Your primary task is to explore and complete the existing implementation to ensure it correctly calculates the capital gains tax according to the rules.

Run tests to understand the expected behavior and system flow.

NOTE ON THE INITIAL STATE: The original code contains a bug. A specific unit test is currently failing, which you must use to identify and resolve the issue before proceeding. Be prepared to discuss, improve, or extend the implementation to cover potential edge cases.

Full test cases and detailed examples are located in

`src/test/resources/`

- explore them as needed!


### Project Structure

```
src/main/java/nubank/capitalgain/ 
├── Main.java # Entry point 
├── calculator/CapitalGainCalculator.java 
└── domain/ # Operation, Tax models
```

 
- **[execution time limit] 30 seconds**
- **[memory limit] 4g**

----

## Multiple Stocks

Now we should consider multiple tickers, so the input for an operation will also contain a new information called "ticker" that will be an identifier of which asset you're selling. The profit calculation (through weighted-average price) and quantity validation (if implemented) on selling should also consider the ticker.

When you sell at a loss, you should use the loss from one ticker toward any other ticker.

### Input #1

```
[{"operation":"buy", "unit-cost":10, "quantity": 10000, "ticker":"AAPL"},
{"operation":"buy", "unit-cost":15, "quantity": 10000, "ticker":"MANU"}, {"operation":"sell", "unit-cost":20, "quantity": 10000, "ticker":"AAPL"},
{"operation":"sell", "unit-cost":30, "quantity": 10000, "ticker":"MANU"}]
```

**Output #1**
[{"tax":0}, {"tax":0}, {"tax":20000}, {"tax":30000}]

### Input #2

```
[{"operation":"buy", "unit-cost":10, "quantity": 10000, "ticker":"AAPL"},
{"operation":"buy", "unit-cost":15, "quantity": 10000, "ticker":"MANU"},
{"operation":"sell", "unit-cost":5, "quantity": 10000, "ticker":"AAPL"},
{"operation":"sell", "unit-cost":30, "quantity": 10000, "ticker":"MANU"}]
```

**Output #2**
[{"tax":0}, {"tax":0}, {"tax":0}, {"tax":20000}]

### Input #3

```
[{"operation":"buy", "unit-cost":10, "quantity": 10000, "ticker":"AAPL"},
{"operation":"buy", "unit-cost":15, "quantity": 10000, "ticker":"MANU"},
{"operation":"sell", "unit-cost":30, "quantity": 10000, "ticker":"MANU"},
{"operation":"sell", "unit-cost":5, "quantity": 10000, "ticker":"AAPL"}]
```

**Output #3**
[{"tax":0}, {"tax":0}, {"tax":30000}, {"tax":0}]