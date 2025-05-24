# PriceComparator
A simple java web api for comparing prices of products in different shops.
Products and prices are taken from Lidl, Kaufland and Profi stores.

## Technologies used
- Java Liberica 24
- Gradle
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL 8.0

## Running the application

1. Clone the repository

```sql
git clone https://github.com/Sneaky-Sniky/PriceComparator.git
```

2. Create a MySQL database and populate `application-jpa.properties` with the correct credentials.

3. Build the project

```sql
./gradlew build
```

4. Run the application

```sql
./gradlew bootRun
```

Note: Upon running the application, if the database is empty, the application will populate the database with data from Lidl, Kaufland and Profi stores.

## API Endpoints

### Products

#### Get all products
```sql
GET /products
```
Returns a list of all products.

Example response:
```json
[
    {
        "id": "P001",
        "productName": "lapte zuzu",
        "productCategory": "lactate",
        "brand": "Zuzu",
        "packageQuantity": 1,
        "packageUnit": "l"
    },
]
```

#### Get shopping list with best prices
```sql
GET /products/shopping-list?productIds=1,2,3
```
Parameters:
- `productIds` (required): List of product IDs to separate into shopping lists

Returns a map of shops with lists of products to buy from there.

Example response:
```json
{
	"kaufland": [
		{
			"productId": "P006",
			"productName": "ouă mărimea M",
			"bestPrice": 13.5
		},
		{
			"productId": "P012",
			"productName": "pâine albă",
			"bestPrice": 3.55
		}
	],
	"lidl": [
		{
			"productId": "P002",
			"productName": "iaurt grecesc",
			"bestPrice": 11.5
		}
	]
}
```

#### Get better alternatives
```sql
GET /products/alternatives?productId=1
```
Parameters:
- `productId` (required): ID of the product to find alternatives for

Returns a list of better-priced alternatives for the given product, based on base price per unit.

Example response:
```json
[
    {
		"id": "P001",
		"productName": "lapte zuzu",
		"productCategory": "lactate",
		"brand": "Zuzu",
		"packageQuantity": 1,
		"packageUnit": "l",
		"bestPrice": 9.8,
		"shop": "lidl",
		"unitPrice": 9.8
	}
]
```

#### Get price history
```sql
GET /products/price-history?shop=Lidl&category=Dairy&brand=Milk&startDate=2024-01-01&endDate=2024-03-20
```
Parameters:
- `shop` (optional): Filter by shop name
- `category` (optional): Filter by product category
- `brand` (optional): Filter by brand
- `startDate` (optional): Start date in ISO format (YYYY-MM-DD)
- `endDate` (optional): End date in ISO format (YYYY-MM-DD)

Returns price history data for the specified filters. Each key is the product with a list of its price history. The dates are ordered chronologically, across all shops (if not filtered otherwise)

Example response:
```json
{
    "P002": [
		{
			"productId": "P002",
			"productName": "iaurt grecesc",
			"brand": "Lidl",
			"productCategory": "lactate",
			"shop": "lidl",
			"price": 11.6,
			"date": "2025-05-08",
			"packageQuantity": 0.4,
			"packageUnit": "kg",
			"unitPrice": 28.999999999999996
		}
	]
}
```

### Discounts

#### Get all discounts
```sql
GET /discounts
```
Returns a list of all current discounts.

Example response:
```json
[
    {
		"id": 1,
		"product": {
			"id": "P001",
			"productName": "lapte zuzu",
			"productCategory": "lactate",
			"brand": "Zuzu",
			"packageQuantity": 1,
			"packageUnit": "l"
		},
		"fromDate": "2025-05-08",
		"toDate": "2025-05-14",
		"percentageOfDiscount": 9,
		"shop": "kaufland"
	},
]
```

#### Get best discounts
```sql
GET /discounts/best
```
Returns a list of the best current discounts sorted by discount percentage.

Example response:
```json
[
    {
		"id": 19,
		"product": {
			"id": "P037",
			"productName": "detergent lichid",
			"productCategory": "produse de menaj",
			"brand": "Persil",
			"packageQuantity": 2.5,
			"packageUnit": "l"
		},
		"fromDate": "2025-05-06",
		"toDate": "2025-05-12",
		"percentageOfDiscount": 25,
		"shop": "lidl"
    }
]
```

#### Get latest discounts
```sql
GET /discounts/latest
```
Returns a list of discounts added in the last 24 hours.

Example response:
```json
[
    {
		"id": 19,
		"product": {
			"id": "P037",
			"productName": "detergent lichid",
			"productCategory": "produse de menaj",
			"brand": "Persil",
			"packageQuantity": 2.5,
			"packageUnit": "l"
		},
		"fromDate": "2025-05-06",
		"toDate": "2025-05-12",
		"percentageOfDiscount": 25,
		"shop": "lidl"
	}
]
```

### Price Alerts

#### Create price alert
```sql
POST /alerts
```
Parameters (in request body):
```json
{
  "productId": "1",
  "targetPrice": 2.50
}
```
Creates a new price alert for a product reaching a target price.
When a product reaches below a target price, the details are logged to the server console. For this, a task is scheduled to run at fixed intervals and check all prices.

#### Get active alerts
```sql
GET /alerts
```
Returns a list of all active price alerts.

Example response:
```json
[
	{
		"id": 2,
		"product": {
			"id": "P002",
			"productName": "iaurt grecesc",
			"productCategory": "lactate",
			"brand": "Lidl",
			"packageQuantity": 0.4,
			"packageUnit": "kg"
		},
		"targetPrice": 12,
		"createdAt": "2025-05-24T12:28:48.140631"
	}
]
```

#### Delete alert
```sql
DELETE /alerts/{alertId}
```
Parameters:
- `alertId` (path parameter): ID of the alert to delete

Deletes a specific price alert.
